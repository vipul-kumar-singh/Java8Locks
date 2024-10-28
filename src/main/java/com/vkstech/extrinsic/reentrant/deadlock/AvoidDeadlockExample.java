package com.vkstech.extrinsic.reentrant.deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AvoidDeadlockExample {
    private final Lock lockA = new ReentrantLock();
    private final Lock lockB = new ReentrantLock();

    public void methodA() {
        try {
            if (lockA.tryLock(5, TimeUnit.SECONDS)) {

                try {
                    System.out.println("LockA acquired by Thread-1");
                    Thread.sleep(5000);

                    if (lockB.tryLock(5, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("LockB acquired by Thread-1");
                            Thread.sleep(5000);
                        } finally {
                            lockB.unlock();
                        }
                    } else {
                        System.out.println("Thread-1 could not acquire LockB, avoiding deadlock.");
                    }

                } finally {
                    lockA.unlock();
                }

            } else {
                System.out.println("Thread-1 could not acquire LockA, avoiding deadlock.");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void methodB() {
        try {
            if (lockB.tryLock(5, TimeUnit.SECONDS)) {

                try {
                    System.out.println("LockB acquired by Thread-2");
                    Thread.sleep(5000);

                    if (lockA.tryLock(5, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("LockA acquired by Thread-2");
                            Thread.sleep(5000);
                        } finally {
                            lockA.unlock();
                        }
                    } else {
                        System.out.println("Thread-2 could not acquire LockA, avoiding deadlock.");
                    }

                } finally {
                    lockB.unlock();
                }

            } else {
                System.out.println("Thread-2 could not acquire LockB, avoiding deadlock.");
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {
        AvoidDeadlockExample example = new AvoidDeadlockExample();

        Thread t1 = new Thread(example::methodA);
        Thread t2 = new Thread(example::methodB);

        t1.start();
        t2.start();
    }
}
