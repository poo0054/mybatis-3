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
package com.poo0054;

import com.poo0054.dao.SysUserDao;
import com.poo0054.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author ZhangZhi
 * @version 1.0
 * @since 2022/7/15 15:46
 */
@Slf4j
public class Test {

  @org.junit.Test
  public void test() throws IOException {
    exec();
  }

  /**
   * mybatis 初始化
   */
  private static void exec() {
    String resource = "mybatis-config.xml";
    //使用ClassLoader获得当前项目路径
    try {
      InputStream inputStream = Resources.getResourceAsStream(resource);
      //获取DefaultSqlSessionFactory对象
      SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

      /*//使用另外一套数据源
      InputStream resourceAsStream = Resources.getResourceAsStream(resource);
      SqlSessionFactory testSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream, "test");
      SqlSession sqlSession1 = testSessionFactory.openSession();
      SysUserDao sysUserDao = sqlSession1.getMapper(SysUserDao.class);
      List<SysUser> sysUsers = sysUserDao.queryAllByLimit(new SysUser());
      log.debug(sysUsers.toString());*/


      //每次都是一个新的sqlSession
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        SysUserDao roleMapper = sqlSession.getMapper(SysUserDao.class);
        List<SysUser> sysUser = roleMapper.queryAllByLimit(new SysUser());
        log.debug(sysUser.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
