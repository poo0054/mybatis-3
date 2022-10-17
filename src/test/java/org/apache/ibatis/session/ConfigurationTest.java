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
package org.apache.ibatis.session;

import com.poo0054.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/17 15:46
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class ConfigurationTest {
  private InputStream inputStream;
  private Configuration configuration;

  @BeforeEach
  void before() throws IOException {
    this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
    configuration = new Configuration();
  }


}
