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
package org.apache.ibatis.parsing;

import java.util.Properties;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class PropertyParser {

    private static final String KEY_PREFIX = "org.apache.ibatis.parsing.PropertyParser.";
    /**
     * The special property key that indicate whether enable a default value on placeholder.
     * <p>
     * The default value is {@code false} (indicate disable a default value on placeholder)
     * If you specify the {@code true}, you can specify key and default value on placeholder (e.g. {@code ${db.username:postgres}}).
     * </p>
     * 指示是否启用占位符上的默认值的特殊属性键。 默认值为false（表示禁用占位符上的默认值）如果指定true
     * ，则可以在占位符（例如${db.username:postgres}）上指定键和默认值。
     *
     * @since 3.4.2
     */
    public static final String KEY_ENABLE_DEFAULT_VALUE = KEY_PREFIX + "enable-default-value";

    /**
     * The special property key that specify a separator for key and default value on placeholder.
     * <p>
     * The default separator is {@code ":"}.
     * </p>
     * 为占位符上的键和默认值指定分隔符的特殊属性键。 默认分隔符为“：”。
     *
     * @since 3.4.2
     */
    public static final String KEY_DEFAULT_VALUE_SEPARATOR = KEY_PREFIX + "default-value-separator";

    private static final String DEFAULT_VALUE_SEPARATOR = ":";
    private static final String ENABLE_DEFAULT_VALUE = "false";

    private PropertyParser() {
        // Prevent Instantiation
    }

    /**
     * 把 string 根据  variables解析
     *
     * @param string    待处理的值
     * @param variables 公共 variables
     * @return 解析后的 String
     */
    public static String parse(String string, Properties variables) {
        //创建处理器
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        //处理 string
        return parser.parse(string);
    }

    private static class VariableTokenHandler implements TokenHandler {
        /**
         * 公共 variables
         */
        private final Properties variables;
        private final boolean enableDefaultValue;
        private final String defaultValueSeparator;

        /**
         * 根据 variables 创建 TokenHandler
         *
         * @param variables 公共 variables
         */
        private VariableTokenHandler(Properties variables) {
            this.variables = variables;
            this.enableDefaultValue = Boolean.parseBoolean(getPropertyValue(KEY_ENABLE_DEFAULT_VALUE, ENABLE_DEFAULT_VALUE));
            this.defaultValueSeparator = getPropertyValue(KEY_DEFAULT_VALUE_SEPARATOR, DEFAULT_VALUE_SEPARATOR);
        }

        /**
         * 根据 key 在 variables 中查询 不存在则使用 defaultValue默认值
         *
         * @param key          key
         * @param defaultValue 默认值
         * @return 解析后的值 不存在就取默认值
         */
        private String getPropertyValue(String key, String defaultValue) {
            return (variables == null) ? defaultValue : variables.getProperty(key, defaultValue);
        }

        @Override
        public String handleToken(String content) {
            if (variables != null) {
                String key = content;
                if (enableDefaultValue) {
                    final int separatorIndex = content.indexOf(defaultValueSeparator);
                    String defaultValue = null;
                    if (separatorIndex >= 0) {
                        key = content.substring(0, separatorIndex);
                        defaultValue = content.substring(separatorIndex + defaultValueSeparator.length());
                    }
                    if (defaultValue != null) {
                        return variables.getProperty(key, defaultValue);
                    }
                }
                if (variables.containsKey(key)) {
                    return variables.getProperty(key);
                }
            }
            return "${" + content + "}";
        }
    }

}
