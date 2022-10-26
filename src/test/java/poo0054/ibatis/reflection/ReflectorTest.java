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
package poo0054.ibatis.reflection;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/20 10:06
 */
@Slf4j
public class ReflectorTest {

    private final ReflectorFactory reflectorFactory = new DefaultReflectorFactory();

    @Before
    public void before() {

    }

    @Test
    public void test() {
        MetaClass metaConfig = MetaClass.forClass(Configuration.class, reflectorFactory);
        String[] getterNames = metaConfig.getGetterNames();
        log.info(getterNames.toString());
    }

    @Test
    public void createReflector() {
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