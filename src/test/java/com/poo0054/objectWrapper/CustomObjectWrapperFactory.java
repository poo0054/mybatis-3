package com.poo0054.objectWrapper;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.CollectionWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Collections;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/14 16:46
 */
public class CustomObjectWrapperFactory implements ObjectWrapperFactory {
  @Override
  public boolean hasWrapperFor(Object object) {
    return false;
  }

  @Override
  public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
    return new CollectionWrapper(metaObject, Collections.singleton(object));
  }

}
