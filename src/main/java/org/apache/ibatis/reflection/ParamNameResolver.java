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
package org.apache.ibatis.reflection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ParamNameResolver {

    private static final String GENERIC_NAME_PREFIX = "param";

    /**
     * <p>
     * The key is the index and the value is the name of the parameter.<br />
     * The name is obtained from {@link Param} if specified. When {@link Param} is not specified,
     * the parameter index is used. Note that this index could be different from the actual index
     * when the method has special parameters (i.e. {@link RowBounds} or {@link ResultHandler}).
     * </p>
     * <ul>
     * <li>aMethod(@Param("M") int a, @Param("N") int b) -&gt; {{0, "M"}, {1, "N"}}</li>
     * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
     * <li>aMethod(int a, RowBounds rb, int b) -&gt; {{0, "0"}, {2, "1"}}</li>
     * </ul>
     */
    private final SortedMap<Integer, String> names;

    /**
     * 是否有{@link Param }注解
     */
    private boolean hasParamAnnotation;

    public ParamNameResolver(Configuration config, Method method) {
        //方法所有参数
        final Class<?>[] paramTypes = method.getParameterTypes();
        //方法参数所有注解
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();
        final SortedMap<Integer, String> map = new TreeMap<Integer, String>();
        //注解长度
        int paramCount = paramAnnotations.length;
        // get names from @Param annotations 从@Param注释中获取名称
        //循环所有注解
        for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
            //跳过 RowBounds  ResultHandler
            if (isSpecialParameter(paramTypes[paramIndex])) {
                // skip special parameters
                continue;
            }
            String name = null;
            //循环方法参数注解
            for (Annotation annotation : paramAnnotations[paramIndex]) {
                //匹配 @Param 注解
                if (annotation instanceof Param) {
                    hasParamAnnotation = true;
                    name = ((Param) annotation).value();
                    break;
                }
            }
            //没有@param 注解 就使用下标或者名称
            if (name == null) {
                // @Param was not specified. @未指定参数。
                //没有@param注解
                if (config.isUseActualParamName()) {
                    //获取下标为paramIndex的名称
                    name = getActualParamName(method, paramIndex);
                }
                if (name == null) {
                    // use the parameter index as the name ("0", "1", ...)
                    // gcode issue #71
                    //使用下标进行匹配 下标从0开始 直接取size
                    name = String.valueOf(map.size());
                }
            }
            map.put(paramIndex, name);
        }
        names = Collections.unmodifiableSortedMap(map);
    }

    private String getActualParamName(Method method, int paramIndex) {
        if (Jdk.parameterExists) {
            //获取method指定下标的方法参数
            return ParamNameUtil.getParamNames(method).get(paramIndex);
        }
        return null;
    }

    private static boolean isSpecialParameter(Class<?> clazz) {
        return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
    }

    /**
     * Returns parameter names referenced by SQL providers.
     */
    public String[] getNames() {
        return names.values().toArray(new String[0]);
    }

    /**
     * <p>
     * A single non-special parameter is returned without a name.<br />
     * Multiple parameters are named using the naming rule.<br />
     * In addition to the default names, this method also adds the generic names (param1, param2,
     * ...).
     * </p>
     */
    public Object getNamedParams(Object[] args) {
        //方法参数大小
        final int paramCount = names.size();
        if (args == null || paramCount == 0) {
            return null;
        } else if (!hasParamAnnotation && paramCount == 1) {
            //只有一个参数并且没有注解 取出args的第一个最小下标 -- names中已经过滤掉 RowBounds 和 ResultHandler
            return args[names.firstKey()];
        } else {
            final Map<String, Object> param = new ParamMap<Object>();
            int i = 0;
            for (Map.Entry<Integer, String> entry : names.entrySet()) {
                param.put(entry.getValue(), args[entry.getKey()]);
                // add generic param names (param1, param2, ...) 添加通用参数名称（param1，param2，…）
                final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
                // ensure not to overwrite parameter named with @Param  确保不覆盖名为@Param的参数
                if (!names.containsValue(genericParamName)) {
                    param.put(genericParamName, args[entry.getKey()]);
                }
                i++;
            }
            return param;
        }
    }
}
