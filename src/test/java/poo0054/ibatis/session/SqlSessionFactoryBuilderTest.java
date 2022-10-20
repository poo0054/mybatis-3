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
package poo0054.ibatis.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import poo0054.constant.FileConstant;
import poo0054.dao.TableAttributeDao;
import poo0054.entity.TableAttribute;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/13 15:08
 */
@Slf4j
public class SqlSessionFactoryBuilderTest {

    private SqlSessionFactory sqlSessionFactory;

    private SqlSessionFactory testSessionFactory;

    @Before
    public void init() {
        try {
            InputStream inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
            //获取DefaultSqlSessionFactory对象
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, null, null);

            //使用另外一套数据源
            InputStream resourceAsStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
            testSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream, "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void queryAllByLimit() {
        //每次都是一个新的sqlSession
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            TableAttributeDao roleMapper = sqlSession.getMapper(TableAttributeDao.class);
            List<TableAttribute> sysUser = roleMapper.queryAllByLimit(new TableAttribute(null, null, null));
            log.info(sysUser.toString());
        }
    }

}
