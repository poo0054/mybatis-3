/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * TypeHandler 是 Mybatis 中所有类型转换器的顶层接口，
 * 主要用于实现数据从 Java 类型 到 JdbcType 类型 的相互转换。
 *
 * @author Clinton Begin
 */
public interface TypeHandler<T> {

  /**
   * 通过 PreparedStatement 为 SQL语句 绑定参数时，将数据从 Java类型 转换为 JDBC类型
   */
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  /**
   * Gets the result.
   * <p>
   * 从结果集获取数据时，将数据由 JDBC类型 转换成 Java类型
   *
   * @param rs         the rs
   * @param columnName Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
   * @return the result
   * @throws SQLException the SQL exception
   */
  T getResult(ResultSet rs, String columnName) throws SQLException;

  /**
   * 从结果集获取数据时，将数据由 JDBC类型 转换成 Java类型
   */
  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}
