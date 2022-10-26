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
package poo0054.ibatis.parsing;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;
import poo0054.constant.FileConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/20 10:04
 */
@Slf4j
public class XPathParserTest {

    InputStream inputStream;
    XPathParser xPathParser;
    XNode root;
    /**
     * 创建并缓存 Reflector对象
     */
    private final ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();

    @Before
    public void before() throws IOException {
        this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
    /*
    解析xml工具类
     */
        xPathParser = new XPathParser(inputStream, true, null, new XMLMapperEntityResolver());
        root = xPathParser.evalNode("/configuration");
    }

    /**
     * log解析
     */
    @Test
    public void settingsAsPropertiesTest() {
        XNode context = root.evalNode("settings");
        if (context != null) {
            Properties props = context.getChildrenAsProperties();
            // Check that all settings are known to the configuration class
            //检查配置类是否知道所有设置
            //检查该类是否存在配置中属性
            MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
            for (Object key : props.keySet()) {
                //使用set方法进行判断
                if (!metaConfig.hasSetter(String.valueOf(key))) {
                    throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
                }
            }
            log.info("props：{}", props);
        }
    }

    /**
     * properties解析
     *
     * @throws IOException
     */
    @Test
    public void propertiesElementTest() throws IOException {
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