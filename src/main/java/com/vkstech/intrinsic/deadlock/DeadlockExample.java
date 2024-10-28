package com.vkstech.intrinsic.deadlock;

public class DeadlockExample {

    private final Object resourceA = new Object();
    private final Object resourceB = new Object();

    public void methodA() {
        synchronized (resourceA) {
            System.out.println("Thread 1 locked resourceA");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (resourceB) {
                System.out.println("Thread 1 locked resourceB");
            }
        }
    }

    public void methodB() {
        synchronized (resourceB) {
            System.out.println("Thread 2 locked resourceA");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (resourceA) {
                System.out.println("Thread 2 locked resourceB");
            }
        }
    }

    public static void main(String[] args) {
        DeadlockExample de = new DeadlockExample();

        Thread t1 = new Thread(de::methodA);
        t1.start();

        Thread t2 = new Thread(de::methodB);
        t2.start();
    }

}
