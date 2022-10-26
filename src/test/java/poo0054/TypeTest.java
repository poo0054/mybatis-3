package poo0054;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/25 10:03
 */
@Slf4j
public class TypeTest {
    protected static Set<String> lazyLoadTriggerMethods = new HashSet<String>(Arrays.asList(new String[]{"equals", "clone", "hashCode", "toString"}));

    @Test
    public void test() throws NoSuchMethodException {
        Class<TypeVariableTest> aClass = TypeVariableTest.class;
//        TypeVariableTest<String, Long> variableTest = new TypeVariableTest<>();
        Method[] methods1 = aClass.getMethods();
//        Method[] methods = variableTest.getClass().getMethods();
        for (int i = 0; i < methods1.length; i++) {
            Method aClassMethod = methods1[i];
//            Method method = methods[i];
            if (lazyLoadTriggerMethods.contains(aClassMethod.getName())) {
                continue;
            }
            System.out.println(aClassMethod.getName());
            System.out.println("getGenericReturnType:----------");
            //获取
            Type genericReturnType = aClassMethod.getGenericReturnType();
            System.out.println(genericReturnType.getClass());

            System.out.println("getReturnType:----------");
            Type returnType = aClassMethod.getReturnType();
            System.out.println(returnType.getClass());

            Class<?>[] parameterTypes = aClassMethod.getParameterTypes();
            if (ObjectUtil.isNotEmpty(parameterTypes)) {
                System.out.println("getParameterTypes:----------");
                System.out.println(parameterTypes[0].getClass());
            }

            Type[] genericParameterTypes = aClassMethod.getGenericParameterTypes();
            if (ObjectUtil.isNotEmpty(genericParameterTypes)) {
                System.out.println("getParameterTypes:----------");
                System.out.println(genericParameterTypes[0].getClass());
            }
        }

        /*if (genericReturnType instanceof TypeVariable) {

        } else if (genericReturnType instanceof ParameterizedType) {

        } else if (genericReturnType instanceof GenericArrayType) {

        } else {

        }*/


        System.out.println();
    }


    @Data
    static class TypeVariableTest<T extends TypeTest, V> {
        private T t;
        private V v;
        private List<T> tList;
        private Map<T, V> tvMap;
        private List<?> list;
        private Map<T, ?> tMap;
        private Map<?, V> vMap;

        private List<String> strings;
        private List<String>[] stringsLists;
    }

}