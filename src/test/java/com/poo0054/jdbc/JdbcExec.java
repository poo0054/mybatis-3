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
package com.poo0054.jdbc;

import java.sql.*;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/12 17:56
 */
public class JdbcExec {
  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    //使用类加载注册Driver
//    DriverManager.registerDriver(new Driver());
    Class.forName("com.mysql.cj.jdbc.Driver");

    Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.10:3306/yidian_oms", "root", "root");

    String sql = " select * from sys_user";

    //preparedStatement
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet1 = preparedStatement.executeQuery();
    print(resultSet1);

    //statement
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);
    print(resultSet);
  }

  private static void print(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
      String id = resultSet.getString("id");
      String loginName = resultSet.getString("loginName");
      String userName = resultSet.getString("userName");

      System.out.printf("id --- %s \t loginName --- %s userName --- %s \n", id, loginName, userName);
    }
  }

}
