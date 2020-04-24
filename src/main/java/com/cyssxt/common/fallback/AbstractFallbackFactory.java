//package com.cyssxt.common.fallback;
//
//
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//public abstract class AbstractFallbackFactory<T> implements InvocationHandler, FallbackFactory<T> {
//    private T t = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.getClass()}, this);
//    private Throwable throwable;
//
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        String data = null;
//        if (this.throwable != null) {
//            data = this.throwable.getMessage();
//        }
//
//        return ResponseData.fail(data);
//    }
//
//    public T create(Throwable cause) {
//        this.throwable = cause;
//        return this.t;
//    }
//
//    public abstract Class getProxyClasses();
//}
