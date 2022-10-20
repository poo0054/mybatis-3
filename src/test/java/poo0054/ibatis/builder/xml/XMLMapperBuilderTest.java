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
package poo0054.ibatis.builder.xml;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;
import poo0054.SimpleBuildUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/17 16:15
 */
@Slf4j
public class XMLMapperBuilderTest {

    private Configuration configuration;
    XMLMapperBuilder xmlMapperBuilder;
    XNode mappers;

    @Before
    public void before() throws IOException {
        configuration = SimpleBuildUtils.buildConfiguration();
        configuration.setVariables(SimpleBuildUtils.getVariables());
        mappers = SimpleBuildUtils.buildConfigXpath().evalNode("/configuration/mappers");
    }

    @Test
    public void mapperElement() throws IOException, ClassNotFoundException {
        //循环所有mapper
        for (XNode child : mappers.getChildren()) {
            //取出mapper的数据
            String resource = child.getStringAttribute("resource");
            String url = child.getStringAttribute("url");
            String mapperClass = child.getStringAttribute("class");

            //url和resource使用XMLMapperBuilder
            if (StrUtil.isNotEmpty(resource)) {
                try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
                    xmlMapperBuilder = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                }
            }
            //mapperClass和package使用mapperRegistry
            if (StrUtil.isNotEmpty(mapperClass)) {
                // 如果 <mapper>节点 指定了 class属性，则向 MapperRegistry 注册 该Mapper接口
                Class<?> mapperInterface = Resources.classForName(mapperClass);
                configuration.addMapper(mapperInterface);
            }
        }
    }
}
