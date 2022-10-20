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