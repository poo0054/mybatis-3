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

import java.util.HashMap;

/**
 * Inline parameter expression parser. Supported grammar (simplified):
 *
 * <pre>
 * inline-parameter = (propertyName | expression) oldJdbcType attributes
 * propertyName = /expression language's property navigation path/
 * expression = '(' /expression language's expression/ ')'
 * oldJdbcType = ':' /any valid jdbc type/
 * attributes = (',' attribute)*
 * attribute = name '=' value
 * </pre>
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class ParameterExpression extends HashMap<String, String> {

    private static final long serialVersionUID = -2417552199605158680L;

    public ParameterExpression(String expression) {
        parse(expression);
    }

    private void parse(String expression) {
        //从下标p开始的第一个不为空格的下标
        int p = skipWS(expression, 0);
        //如果有( - 暂时未支持
        if (expression.charAt(p) == '(') {
            expression(expression, p + 1);
        } else {
            //处理
            property(expression, p);
        }
    }

    private void expression(String expression, int left) {
        int match = 1;
        int right = left + 1;
        while (match > 0) {
            if (expression.charAt(right) == ')') {
                match--;
            } else if (expression.charAt(right) == '(') {
                match++;
            }
            right++;
        }
        put("expression", expression.substring(left, right - 1));
        jdbcTypeOpt(expression, right);
    }

    private void property(String expression, int left) {
        if (left < expression.length()) {
            //从下标left开始后的第一个 ,: 值
            int right = skipUntil(expression, left, ",:");
            //从 left到 ,: 中间的值
            put("property", trimmedStr(expression, left, right));
            //jdbc类型
            jdbcTypeOpt(expression, right);
        }
    }

    /**
     * 从下标p开始的第一个不为空格的下标
     *
     * @param expression 待处理的字符串
     * @param p          从哪个下标开始
     * @return 返回从p开始后的不为空格的下标
     */
    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
            if (expression.charAt(i) > 0x20) {
                return i;
            }
        }
        return expression.length();
    }

    /**
     * 从下标p开始 查找包含endChars的值
     *
     * @param expression str
     * @param p          开始下标
     * @param endChars   是否包含的值
     * @return 包含该值的下标
     */
    private int skipUntil(String expression, int p, final String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > -1) {
                return i;
            }
        }
        return expression.length();
    }

    private void jdbcTypeOpt(String expression, int p) {
        //从下标p开始的第一个不为空格的下标
        p = skipWS(expression, p);
        if (p < expression.length()) {
            //是否为：
            if (expression.charAt(p) == ':') {
                //：后的值为jabcType
                jdbcType(expression, p + 1);
            } else if (expression.charAt(p) == ',') {
                //其他类型
                option(expression, p + 1);
            } else {
                throw new BuilderException("Parsing error in {" + expression + "} in position " + p);
            }
        }
    }

    /**
     * 处理jdbc类型
     *
     * @param expression 待处理的str
     * @param p          开始下标
     */
    private void jdbcType(String expression, int p) {
        int left = skipWS(expression, p);
        int right = skipUntil(expression, left, ",");
        //去除空格，找到有边界后进行put
        if (right > left) {
            put("jdbcType", trimmedStr(expression, left, right));
        } else {
            throw new BuilderException("Parsing error in {" + expression + "} in position " + p);
        }
        option(expression, right + 1);
    }

    /**
     * 其它类型处理
     *
     * @param expression str
     * @param p          开始下标
     */
    private void option(String expression, int p) {
        int left = skipWS(expression, p);
        if (left < expression.length()) {
            int right = skipUntil(expression, left, "=");
            String name = trimmedStr(expression, left, right);
            left = right + 1;
            right = skipUntil(expression, left, ",");
            String value = trimmedStr(expression, left, right);
            put(name, value);
            option(expression, right + 1);
        }
    }

    /**
     * 去除空格
     *
     * @param str   待处理的字符串
     * @param start 开始下标
     * @param end   结束下标
     * @return 去除空格后的str
     */
    private String trimmedStr(String str, int start, int end) {
        while (str.charAt(start) <= 0x20) {
            start++;
        }
        while (str.charAt(end - 1) <= 0x20) {
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }

}
