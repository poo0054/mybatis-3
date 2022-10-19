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
