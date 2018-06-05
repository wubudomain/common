package top.wboost.common.base;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {

    public static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Condition confitionA = lock.newCondition();
        Condition confitionB = lock.newCondition();
        new Thread(() -> {
            for (int i = 0; i < 100; i += 2) {
                try {
                    lock.lock();
                    System.out.println(i);
                    confitionA.signal();
                    if (i + 2 < 100) {
                        confitionB.await();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 1; i < 100; i += 2) {
                try {
                    lock.lock();
                    System.out.println(i);
                    confitionB.signal();
                    if (i + 2 < 100) {
                        confitionA.await();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }).start();
    }

}
