package top.wboost.common.boost;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

//@WebListener
public class DestoryThreadContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // TODO Auto-generated method stub
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /**
         * 增加清除所有注册的JDBC驱动
         */
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        removeThreadLocals();
    }

    /**
     * 移除各种清除不掉的线程
     */
    public Integer removeThreadLocals() {
        int count = 0;
        try {
            final Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            final Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");
            inheritableThreadLocalsField.setAccessible(true);
            for (final Thread thread : Thread.getAllStackTraces().keySet()) {
                count += clear(threadLocalsField.get(thread));
                count += clear(inheritableThreadLocalsField.get(thread));
                /**
                 * 在tomcat容器里，每个线程的ClassLoader归根结底都被默认设置成webapp ClassLoader。
                 * 当某些线程无法及时关闭时，webapp classloader就会因为这些线程拥有的强引用，无法正常gc
                 * 将ClassLoader设置为null 删除强引用则可以gc
                 */
                if (thread != null) {
                    thread.setContextClassLoader(null);
                }
            }
            System.out.println("remove " + count + " values in ThreadLocals");
        } catch (Exception e) {
            throw new Error("removeThreadLocals()", e);
        }
        return count;
    }

    /**
     * ThreadLocal的报错 估计是某些线程的ThreadLocal无法释放，为什么无法释放，因为那些线程还没停掉，
     * 每个ThreadLocal都是被一个Thread的ThreadMap下以<ThreadLocalObject, Object>的entry形式维护着，
     * 这些entry继承了WeakReference，以上代码应该是将每个thread的threadMap的entry设成null，
     * 这样原来的entry没有引用源，作为一个WeakReference会在GC中被清除掉。
     * @param threadLocalMap
     * @return
     * @throws Exception
     */
    private int clear(final Object threadLocalMap) throws Exception {
        if (threadLocalMap == null)
            return 0;
        int count = 0;
        final Field tableField = threadLocalMap.getClass().getDeclaredField("table");
        tableField.setAccessible(true);
        final Object table = tableField.get(threadLocalMap);
        for (int i = 0, length = Array.getLength(table); i < length; ++i) {
            final Object entry = Array.get(table, i);
            if (entry != null) {
                final Object threadLocal = ((WeakReference<?>) entry).get();
                if (threadLocal != null) {
                    Array.set(table, i, null);
                    ++count;
                }
            }
        }
        return count;
    }

}
