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

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import poo0054.constant.FileConstant;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/13 17:32
 */
@Slf4j
public class XMLConfigBuilderTest {
    private InputStream inputStream;


    @Before
    public void before() throws IOException {
        this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
    }


    @Test
    public void xmlConfigBuilderTest() {
    /*
    BaseBuilder是所有Builder的父类  里面必须要有一个 Configuration 最初的是从 XMLConfigBuilder new出来的
    new Configuration同时 typeAliasRegistry注册一些默认的别名
    而new typeAliasRegistry TypeAliasRegistry中会默认注册java数据类型的别名
     */
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(inputStream, null, null);
    /*
     核心代码 构建出全局唯一的Configuration
     */
        Configuration config = xmlConfigBuilder.parse();
        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(config);

        //构建
        SqlSession sqlSession = defaultSqlSessionFactory.openSession();

   /* TableAttributeDao roleMapper = sqlSession.getMapper(TableAttributeDao.class);
    List<TableAttribute> tableAttributes = roleMapper.queryAllByLimit(new TableAttribute());
    log.info(tableAttributes.toString());*/
    }

}
