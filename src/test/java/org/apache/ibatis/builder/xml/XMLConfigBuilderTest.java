package org.apache.ibatis.builder.xml;

import com.poo0054.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/13 17:32
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class XMLConfigBuilderTest {
  private InputStream inputStream;

  @BeforeEach
  void before() throws IOException {
    this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
  }

  @Test
  void initTest() {
    /*
    BaseBuilder是所有Builder的父类  里面必须要有一个 Configuration 最初的是从 XMLConfigBuilder new出来的
    new Configuration同时 typeAliasRegistry注册一些默认的别名
    而new typeAliasRegistry TypeAliasRegistry中会默认注册java数据类型的别名
     */
    XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream, null, null);
    /*
     核心代码 构建出全局唯一的Configuration
     */
    Configuration parse = xmlConfigBuilder.parse();

  }

}
