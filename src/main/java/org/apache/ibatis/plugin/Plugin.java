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
package org.apache.ibatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Clinton Begin
 */
public class Plugin implements InvocationHandler {

    /**
     * 原始对象
     */
    private final Object target;
    private final Interceptor interceptor;
    private final Map<Class<?>, Set<Method>> signatureMap;

    private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    /**
     * mybatis拦截器的关键方法
     * <p>
     * 根据target对象判断是否属于 {@code interceptor }参数Executor的子类
     *
     * @param target      {@link Executor}  {@link ParameterHandler}  {@link ResultSetHandler}
     *                    {@link StatementHandler}
     * @param interceptor 注解
     * @return 执行后的结果
     */
    public static Object wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        //根据target匹配type 匹配成功就创建一个代理对象
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        if (interfaces.length > 0) {
            //jdk动态代理创建
            return Proxy.newProxyInstance(
                    //用哪个类加载器去加载代理对象
                    type.getClassLoader(),
                    //动态代理类需要实现的接口
                    interfaces,
                    //动态代理方法在执行时，会调用里面的invoke方法去执行
                    new Plugin(target, interceptor, signatureMap));
        }
        return target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            //返回表示声明由该对象表示的可执行文件的类或接口的Class对象。
            Set<Method> methods = signatureMap.get(method.getDeclaringClass());
            //如果方法匹配成功 就进入拦截器中执行拦截器方法
            if (methods != null && methods.contains(method)) {
                return interceptor.intercept(new Invocation(target, method, args));
            }
            //不匹配就执行原来方法
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }

    /**
     * 解析interceptor注解
     *
     * @param interceptor 注解
     * @return 返回 key: class  value : 根据 method的参数args确认方法
     * 允许存在多个
     */
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        //获取Intercepts注解
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        // issue #251
        if (interceptsAnnotation == null) {
            throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }
        Signature[] sigs = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
        for (Signature sig : sigs) {
            //key :type value 根据 method的args参数匹配方法
            Set<Method> methods = signatureMap.get(sig.type());
            if (methods == null) {
                methods = new HashSet<Method>();
                signatureMap.put(sig.type(), methods);
            }
            try {
                //根据type获取 method 并确认有哪些参数
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
            }
        }
        //返回 key: class  value : 根据 method的参数args确认方法
        return signatureMap;
    }

    /**
     * signatureMap的class是否匹配type接口
     *
     * @param type         需要原始类
     * @param signatureMap 接口解析后的map
     * @return 匹配成功的类
     */
    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            //获取tyoe的接口
            for (Class<?> c : type.getInterfaces()) {
                //只要包含此类
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            //type父类
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

}
