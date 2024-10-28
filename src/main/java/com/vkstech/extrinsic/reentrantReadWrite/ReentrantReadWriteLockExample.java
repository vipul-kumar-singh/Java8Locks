package com.vkstech.extrinsic.reentrantReadWrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int sharedResource = 0;

    public void readResource() {
        lock.readLock().lock();

        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " is reading " + sharedResource);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void writeResource(int value) {
        lock.writeLock().lock();

        try {
            sharedResource = value;
            System.out.println(Thread.currentThread().getName() + " is writing " + sharedResource);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockExample re = new ReentrantReadWriteLockExample();

        Runnable readTask = re::readResource;
        Thread reader1 = new Thread(readTask, "Reader-1");
        Thread reader2 = new Thread(readTask, "Reader-2");
        Thread reader3 = new Thread(readTask, "Reader-3");
        Thread reader4 = new Thread(readTask, "Reader-4");


        Runnable writeTask = () -> re.writeResource(42);
        Thread writer = new Thread(writeTask, "Writer-1");

        reader1.start();
        reader2.start();
        writer.start();
        reader3.start();
        reader4.start();
    }
}