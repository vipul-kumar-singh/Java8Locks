package com.vkstech.extrinsic.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OddEvenPrinter {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final int max;
    private boolean isOddTurn = true;


    public OddEvenPrinter(int max) {
        this.max = max;
    }

    public void printOdd() {

        for (int i = 1; i <= max; i += 2) {
            lock.lock();

            try {
                while (!isOddTurn) {
                    condition.await();
                }

                System.out.println(i);
                isOddTurn = false;
                condition.signalAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public void printEven() {

        for (int i = 2; i <= max; i += 2) {
            lock.lock();

            try {
                while (isOddTurn) {
                    condition.await();
                }

                System.out.println(i);
                isOddTurn = true;
                condition.signalAll();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        OddEvenPrinter printer = new OddEvenPrinter(10);

        Thread oddThread = new Thread(printer::printOdd);
        Thread evenThread = new Thread(printer::printEven);

        oddThread.start();
        evenThread.start();
    }
}
