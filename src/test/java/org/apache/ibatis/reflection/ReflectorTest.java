package org.apache.ibatis.reflection;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/14 9:33
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class ReflectorTest {

  @BeforeEach
  void before() {

  }

  @Test
  void createReflector() {
    Class<ReflectorTestClass> clazz = ReflectorTestClass.class;
    Reflector reflector = new Reflector(clazz);
  }


  @Data
  static class ReflectorTestClass {
    private String name;
    private String age;

    public void getPrint() {
      System.out.println("hhh  想不到吧 我是一个输出语句");
    }
  }
}
