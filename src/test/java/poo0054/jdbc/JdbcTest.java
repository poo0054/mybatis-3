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
package poo0054.jdbc;

import org.junit.Test;

import java.sql.*;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/12 17:56
 */

public class JdbcTest {
    public static void main(String[] args) {
        System.out.println("我无语");
    }

    @Test
    public void query() throws ClassNotFoundException, SQLException {
        System.out.println("我");
        //使用类加载注册Driver
//    DriverManager.registerDriver(new Driver());
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.56.10:3306/yidian_oms", "root", "root");

        String sql = " select * from sys_user ";

        //preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
//        preparedStatement.setString(/*1, "");
//        preparedStatement.setString(*/2, "");
        ResultSet resultSet1 = preparedStatement.executeQuery();
        print(resultSet1);

        //statement
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        print(resultSet);
    }

    public void print(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String loginName = resultSet.getString("loginName");
            String userName = resultSet.getString("userName");

            System.out.printf("id --- %s \t loginName --- %s userName --- %s \n", id, loginName, userName);
        }
    }

}
