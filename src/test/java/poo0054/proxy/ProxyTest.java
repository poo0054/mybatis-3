package poo0054.proxy;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/11/1 10:53
 */
public class ProxyTest {

    public void test() {
        System.out.println("我是test");
    }

    @Test
    public void t() throws NoSuchMethodException {
        Method method1 = ProxyTest.class.getMethod("test");
        ProxyInterface proxyHandle =
                (ProxyInterface) Proxy.newProxyInstance(ProxyHandle.class.getClassLoader(),
                        new Class[]{ProxyInterface.class},
                        new ProxyHandle(new ProxyTest(), method1));
        proxyHandle.t();
    }
}