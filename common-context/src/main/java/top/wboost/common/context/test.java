package top.wboost.common.context;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import top.wboost.common.context.classLoader.NetContextLoader;

public class test extends NetContextLoader {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        /*System.out.println(Thread.currentThread().getContextClassLoader());
        URL url = new File("/Users/jwsun/work/test").toURI().toURL();
        URL url1 = new URL("http://192.168.16.11:18080/testLoader.jar");
        URL[] urls = new URL[1];
        urls[0] = url1;
        URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        
        Thread.currentThread().setContextClassLoader(loader);
        System.out.println(Thread.currentThread().getContextClassLoader());
        Method m = ReflectUtil.findMethod(
                Thread.currentThread().getContextClassLoader().loadClass("top.wboost.loader.test.TestLoader"), "test");
        System.out.println(m.invoke(null));
        call();
        System.out.println(test.class.getClassLoader());
        //Class<?> clazz = Class.forName("top.wboost.loader.test.TestLoader");
        Class<?> clazz = Class.forName("top.wboost.loader.test.TestLoader");
        
        Method m1 = ReflectUtil.findMethod(clazz, "test");
        System.out.println(m);*/

    }

    public static void call() {
        Class<?> caller = sun.reflect.Reflection.getCallerClass();
        System.out.println(caller);
    }

}
