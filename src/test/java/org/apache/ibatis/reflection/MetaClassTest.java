package org.apache.ibatis.reflection;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/14 9:25
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class MetaClassTest {
  /**
   * 创建  Reflector对象
   */
  private final ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

  @BeforeEach
  void before() {

  }

  @Test
  void test() {
    MetaClass metaConfig = MetaClass.forClass(Configuration.class, reflectorFactory);
    String[] getterNames = metaConfig.getGetterNames();
    log.info(getterNames.toString());


  }
}
