package top.wboost.common.base;

public class Sync3 {

    static Object obj = new Object();

    public void run(int begin) {
        new Thread(() -> {
            synchronized (obj) {
                for (int i = begin; i < 100; i += 2) {
                    System.out.println(i);
                    try {
                        this.notify();
                        this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.notifyAll();
            }
        }).start();
    }

    public static void main(String[] args) {
        Sync3 s = new Sync3();
        s.run(0);
        s.run(1);
    }

}
