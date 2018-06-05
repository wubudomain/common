package top.wboost.common.base;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Sync4 {

    static volatile Object obj = new ReentrantLock();

    public void run(int begin) {
        new Thread(new run1()).start();
        new Thread(new run1()).start();
        AtomicInteger i = new AtomicInteger(10);
        System.out.println(i.compareAndSet(8, 11));
        System.out.println(i.getAndSet(11));
        System.out.println(i.getAndSet(12));
    }

    public static void main(String[] args) {
        Sync4 s = new Sync4();
        s.run(0);
    }

    class run1 implements Runnable {

        @Override
        public synchronized void run() {
            System.out.println("run1");
            System.out.println(Thread.currentThread());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread());
            System.out.println("run1");
        }
    }

}
