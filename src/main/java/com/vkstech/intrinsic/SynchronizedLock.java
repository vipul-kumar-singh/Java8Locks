package com.vkstech.intrinsic;

public class SynchronizedLock {

    private boolean isOdd = true;

    public synchronized void printOdd() {
        try {
            for (int i = 1; i <= 10; i += 2) {
                while (!isOdd) {
                    wait();

                }
                System.out.println(i);
                isOdd = false;
                notifyAll();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void printEven() {
        try {
            for (int i = 2; i <= 10; i += 2) {
                while (isOdd) {
                    wait();

                }
                System.out.println(i);
                isOdd = true;
                notifyAll();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SynchronizedLock synchronizedLock = new SynchronizedLock();

        Thread t1 = new Thread(synchronizedLock::printEven);
        Thread t2 = new Thread(synchronizedLock::printOdd);

        t1.start();
        t2.start();
    }
}


