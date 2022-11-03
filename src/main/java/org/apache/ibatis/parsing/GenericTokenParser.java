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

/**
 * 解析器  如果匹配 openToken 和  closeToken 就处理 handler
 *
 * @author Clinton Begin
 */
public class GenericTokenParser {

    private final String openToken;
    private final String closeToken;
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }

    /**
     * 匹配并进行 handler处理
     *
     * @param text 待处理的值
     * @return 处理后的值
     */
    public String parse(String text) {
        //为空直接返回
        if (text == null || text.isEmpty()) {
            return "";
        }
        // search open token 搜索打开标记
        //开始的下标
        int start = text.indexOf(openToken, 0);
        if (start == -1) {
            return text;
        }
        //转换为char数组
        char[] src = text.toCharArray();
        //当前处理的偏移量  在处理 开始字符的时候的下标   处理结束字符的时候为 结束字符的下标
        int offset = 0;
        //处理后的值
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        //开始下标不能为-1
        while (start > -1) {
            //如果前一个为 \\
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue. 开始字符这个的令牌被转义。删除反斜杠并继续。
                //删除一个\ + 开始下标
                builder.append(src, offset, start - offset - 1).append(openToken);
                //偏移量为 开始下标的下一个 跳过当前的 开始字符
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token. 找到打开的标记。让我们搜索close标记。
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                //添加字符之前的所有值
                builder.append(src, offset, start - offset);
                //偏移量移动为 开始下标的 + 开始字符的长度
                offset = start + openToken.length();
                //找到结束下标
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue. 此关闭标记被转义。删除反斜杠并继续。
                        //删除一个 \  +  结束字符
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        //偏移量为 结束下标 + 结束字符长度
                        offset = end + closeToken.length();
                        //找到下一个结束符
                        end = text.indexOf(closeToken, offset);
                    } else {
                        //取出指定符号中间的值
                        expression.append(src, offset, end - offset);
                        //偏移量为结束下标
                        offset = end + closeToken.length();
                        break;
                    }
                }
                //是否存在结束下标
                if (end == -1) {
                    //不存在结束下标
                    // close token was not found. 未找到关闭标记 取出后面所有的进行匹配
                    //不做修改 添加后面所有的值
                    builder.append(src, start, src.length - start);
                    //跳过
                    offset = src.length;
                } else {
                    //添加处理后的值
                    builder.append(handler.handleToken(expression.toString()));
                    //当前偏移量
                    offset = end + closeToken.length();
                }
            }
            //寻找下个 开始下标
            start = text.indexOf(openToken, offset);
        }
        //如果后面还有没有处理的  就append
        if (offset < src.length) {
            //添加偏移量后面所有的值
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
