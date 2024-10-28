package com.vkstech.extrinsic.stamped;

import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {

    private final StampedLock lock = new StampedLock();
    private double x, y;

    // Method to read the coordinates optimistically
    public double[] readCoordinates() {
        long stamp = lock.tryOptimisticRead();
        double currentX = x;
        double currentY = y;

        // Validate if there was a write during the optimistic read
        if (!lock.validate(stamp)) {
            // Fallback to pessimistic read if validation fails
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return new double[]{currentX, currentY};
    }

    // Method to write the coordinates exclusively
    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock(); // Acquire an exclusive write lock
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            lock.unlockWrite(stamp); // Release the write lock
        }
    }

    public static void main(String[] args) {
        StampedLockExample point = new StampedLockExample();

        // Writer thread to modify coordinates
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                point.move(Math.random(), Math.random());
                System.out.println("Writer updated the point.");
                try {
                    Thread.sleep(2000); // Simulate delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Reader thread to read coordinates
        Thread readerThread = new Thread(() -> {
            for (int i = 0; i < 8; i++) {
                double[] coordinates = point.readCoordinates();
                System.out.println("Reader read coordinates: (" + coordinates[0] + ", " + coordinates[1] + ")");
                try {
                    Thread.sleep(1000); // Simulate delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // Start the threads
        writerThread.start();
        readerThread.start();
    }
}
