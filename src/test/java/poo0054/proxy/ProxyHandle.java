package poo0054.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/11/1 10:51
 */

public class ProxyHandle implements InvocationHandler {
    private Object tarAge;

    private Method method;

    public ProxyHandle(Object tarAge) {
        this.tarAge = tarAge;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("我是代理前面------");
        return this.method.invoke(tarAge, args);
    }

    public ProxyHandle(Object tarAge, Method method) {
        this.tarAge = tarAge;
        this.method = method;
    }


}