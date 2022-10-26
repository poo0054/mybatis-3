/**
 * Copyright 2009-2022 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.builder;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Clinton Begin
 */
public class SqlSourceBuilder extends BaseBuilder {

    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        //构建参数并把 #{} 替换为 ?
        @Override
        public String handleToken(String content) {
            //构建出 ParameterMapping
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String content) {
            //获取参数map  并构建 parameterMappings 每个参数都会构建成一个 parameterMappings
            Map<String, String> propertiesMap = parseParameterMapping(content);
            //取出
            String property = propertiesMap.get("property");
            //获取类型
            Class<?> propertyType;
            if (metaParameters.hasGetter(property)) { // issue #448 get type from additional params
                propertyType = metaParameters.getGetterType(property);
            } else if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
                propertyType = parameterType;
            } else if (JdbcType.CURSOR.name().equals(propertiesMap.get("jdbcType"))) {
                propertyType = java.sql.ResultSet.class;
            } else if (property == null || Map.class.isAssignableFrom(parameterType)) {
                propertyType = Object.class;
            } else {
                //反射获取 parameterType 类型的简单处理类
                MetaClass metaClass = MetaClass.forClass(parameterType, configuration.getReflectorFactory());
                //set方法是否包含property
                if (metaClass.hasGetter(property)) {
                    //包含就获取get返回的类型就是 propertyType
                    propertyType = metaClass.getGetterType(property);
                } else {
                    propertyType = Object.class;
                }
            }
            //创建 ParameterMapping的Builder对象准备进行构建
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            Class<?> javaType = propertyType;
            String typeHandlerAlias = null;
            //循环
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if ("javaType".equals(name)) {
                    javaType = resolveClass(value);
                    builder.javaType(javaType);
                } else if ("jdbcType".equals(name)) {
                    builder.jdbcType(resolveJdbcType(value));
                } else if ("mode".equals(name)) {
                    // IN, OUT, INOUT三种类型
                    builder.mode(resolveParameterMode(value));
                } else if ("numericScale".equals(name)) {
                    builder.numericScale(Integer.valueOf(value));
                } else if ("resultMap".equals(name)) {
                    builder.resultMapId(value);
                } else if ("typeHandler".equals(name)) {
                    typeHandlerAlias = value;
                } else if ("jdbcTypeName".equals(name)) {
                    builder.jdbcTypeName(value);
                } else if ("property".equals(name)) {
                    // Do Nothing
                } else if ("expression".equals(name)) {
                    throw new BuilderException("Expression based parameters are not supported yet");
                } else {
                    throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + parameterProperties);
                }
            }
            if (typeHandlerAlias != null) {
                builder.typeHandler(resolveTypeHandler(javaType, typeHandlerAlias));
            }
            return builder.build();
        }

        private Map<String, String> parseParameterMapping(String content) {
            try {
                return new ParameterExpression(content);
            } catch (BuilderException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
            }
        }
    }

}
