package com.poo0054;

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
