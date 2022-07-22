package com.poo0054.example;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.Collection;
import java.util.Properties;

/**
 * @author ZhangZhi
 * @version 1.0
 * @since 2022/7/21 11:40
 */
public class ExampleObjectFactory extends DefaultObjectFactory {
  @Override
  public Object create(Class type) {
    return super.create(type);
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
  }

  @Override
  public <T> boolean isCollection(Class<T> type) {
    return Collection.class.isAssignableFrom(type);
  }
}
