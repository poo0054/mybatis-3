package com.poo0054.example;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.*;

/**
 * @author ZhangZhi
 * @version 1.0
 * @since 2022/7/21 11:35
 */
@MappedJdbcTypes(JdbcType.DATE)
public class ExampleTypeHandler extends BaseTypeHandler<String> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    ps.setDate(i, Date.valueOf(parameter));
  }

  @Override
  public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
    java.sql.Date date = rs.getDate(columnName);
    if (date != null) {
      return date.toString();
    }
    return null;
  }

  @Override
  public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getDate(columnIndex).toString();
  }

  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getDate(columnIndex).toString();
  }
}
