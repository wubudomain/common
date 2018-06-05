package top.wboost.common.base;

public class Sync2 {

    static Object obj = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (obj) {
                for (int i = 0; i < 100; i += 2) {
                    System.out.println(i);
                    try {
                        obj.notify();
                        obj.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                obj.notifyAll();
            }
        }).start();
        new Thread(() -> {
            synchronized (obj) {
                for (int i = 1; i < 100; i += 2) {
                    System.out.println(i);
                    try {
                        obj.notify();
                        obj.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                obj.notifyAll();
            }
        }).start();
    }

}
