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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZhangZhi
 * @version 1.0
 * @since 2022/7/21 18:04
 */
public class MethodHandleTest {

//  ====================string方法

  /**
   * 测试MethodType
   */
  @org.junit.Test
  public void testMethodHandle() {
    //以下代码类似于 "abc".replace("a", "c");
    //可用访问private方法
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    /*
     rtype – 返回类型  如果为空使用void.class
     ptype0 – 第一个参数类型
     ptypes – 剩余的参数类型
     */
    MethodType mt = MethodType.methodType(String.class, CharSequence.class, CharSequence.class);
    try {
      /*
      refc – 从中访问方法的类或接口
      name – 方法的名称
      type – 方法的类型，省略了接收者参数
       */
      MethodHandle mh = lookup.findVirtual(String.class, "replace", mt);
      /*
        调用方法
       */
      String handled_str = (String) mh.invoke("abc", "a", "c");
      System.out.print(handled_str);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  //  ===================================== 私有方法

  @org.junit.Test
  public void testMethodHandleOutStatic() throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodType methodType = MethodType.methodType(void.class, String.class);
    Class aClass = this.getClass();
    MethodHandle methodHandle = lookup.findVirtual(aClass, "out", methodType);
    //第一个参数为实例对象  或者 methodHandle.bindTo(aClass.newInstance()) 是相等的
    methodHandle.invoke(aClass.newInstance(), "大帅比");
  }

  private void out(String msg) {
    System.out.println("我是" + msg);
  }

  //  =================================静态方法


  @org.junit.Test
  public void testMethodHandleOut() throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodType methodType = MethodType.methodType(void.class, String.class, String.class);
    Class aClass = this.getClass();
    MethodHandle out = lookup.findStatic(aClass, "out", methodType);
    out.invoke("大帅比", "123");
  }

  private static void out(String msg, String m) {
    System.out.println(m + "我是" + msg);
  }


//  =====================================集合

  @org.junit.Test
  public void testMethodHandleOutList() throws Throwable {
    MethodHandles.Lookup lookup = MethodHandles.lookup();
    MethodType methodType = MethodType.methodType(void.class, List.class);
    Class aClass = this.getClass();
    MethodHandle out = lookup.findVirtual(aClass, "outList", methodType);
    List<String> strings = Arrays.asList("大帅比", "asd");
    out.invoke(this, strings);
  }

  private void outList(List list) {
    System.out.println("我是" + list);
  }

}
