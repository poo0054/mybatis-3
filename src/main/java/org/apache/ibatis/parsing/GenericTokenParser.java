/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.parsing;

/**
 * @author Clinton Begin
 */
public class GenericTokenParser {


  /**
   * 开始标记
   */
  private final String openToken;
  /**
   * 结束标记
   */
  private final String closeToken;
  /**
   * 标记处理器
   */
  private final TokenHandler handler;

  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  public String parse(String text) {
    // 判断是否空
    if (text == null || text.isEmpty()) {
      return "";
    }
    // search open token
    // 指定的openToken开始下标
    int start = text.indexOf(openToken);
    if (start == -1) {
      return text;
    }
    //转换为小写
    char[] src = text.toCharArray();
    //偏移量
    int offset = 0;
    //最后转换后的值
    final StringBuilder builder = new StringBuilder();
    //开始与结束符号中间的值
    StringBuilder expression = null;

    // 循环处理         assertEquals("James T Kirk reporting.", parser.parse("${first_name} ${initial} ${last_name} reporting."));
    // 将${} 转换成正常文本
    do {
      //跳过
      if (start > 0 && src[start - 1] == '\\') {
        // `\` 忽略这个参数
        // this open token is escaped. remove the backslash and continue.
        builder.append(src, offset, start - offset - 1).append(openToken);
        // offset 重新计算进行下一步循环
        offset = start + openToken.length();
      } else {
        // found open token. let's search close token.
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        //加上原来值
        builder.append(src, offset, start - offset);
        //当前准备处理的偏移量
        offset = start + openToken.length();
        //当前结束标记
        int end = text.indexOf(closeToken, offset);
        //从当前偏移量开始寻找 直到找到结束符号
        while (end > -1) {
          //跳过
          if (end > offset && src[end - 1] == '\\') {
            // 遇到`\`该参数不需要处理
            // this close token is escaped. remove the backslash and continue.
            expression.append(src, offset, end - offset - 1).append(closeToken);
            //偏移量
            offset = end + closeToken.length();
            //寻找下一个偏移量  一般不会进入
            end = text.indexOf(closeToken, offset);
          } else {
            //加入 expression-开始与结束符号中间的值
            expression.append(src, offset, end - offset);
            break;
          }
        }
        if (end == -1) {
          // close token was not found.
          // end == -1 closeToken 不存在,获取后面的所有字符串, openToken - closeToken 之间的内容
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          // closeToken存在 继续执行
          builder.append(handler.handleToken(expression.toString()));
          offset = end + closeToken.length();
        }
      }
      start = text.indexOf(openToken, offset);
    } while (start > -1);
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    // 返回的是一个替换后的sql脚本
    return builder.toString();
  }
}
