package org.apache.ibatis.parsing;

import com.poo0054.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/13 18:00
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class XPathParserTest {
  InputStream inputStream;
  XPathParser xPathParser;

  @BeforeEach
  void before() throws IOException {
    this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
    /*
    解析xml
     */
    xPathParser = new XPathParser(inputStream, true, null, new XMLMapperEntityResolver());
  }

  @Test
  void evalNodeTest() throws IOException {
    XNode root = xPathParser.evalNode("/configuration");
    XNode context = root.evalNode("properties");
    String resource = context.getStringAttribute("resource");
    String url = context.getStringAttribute("url");

    Properties defaults = context.getChildrenAsProperties();
    if (resource != null) {
      defaults.putAll(Resources.getResourceAsProperties(resource));
    } else if (url != null) {
      defaults.putAll(Resources.getUrlAsProperties(url));
    }

    log.info(url);

  }

}
