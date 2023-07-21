package org.example;

class Philosopher extends Thread {
    private static int eatingCount = 0;
    private static int waitingCount = 0;
    private static final Object lock = new Object();//

    private int id;
    private Object leftFork;
    private Object rightFork;

    public Philosopher(int id, Object leftFork, Object rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void eat() {
        try {
            synchronized (lock) {
                while (eatingCount >= 2) {
                    lock.wait(); // המתנה כל עוד יש שני סועדים
                }

                eatingCount++;
                waitingCount--;
                System.out.println("Philosopher " + id + " is eating. (Eating: " + eatingCount + ", Waiting: " + waitingCount + ")");
            }

            Thread.sleep(500);

            synchronized (lock) {
                eatingCount--;
                lock.notifyAll(); // התראה לפילוסופים הממתינים
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void think() {
        try {
            synchronized (lock) {
                waitingCount++;
                System.out.println("Philosopher " + id + " is thinking. (Eating: " + eatingCount + ", Waiting: " + waitingCount + ")");
            }

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        think(); // חשיבה ראשונית

        while (true) {
            synchronized (leftFork) {
                synchronized (rightFork) {
                    eat();
                }
            }
            think(); // חשיבה אחרי אוכל
        }
    }
}

class DiningPhilosophers {
    private static final int NUM_PHILOSOPHERS = 5;

    public static void main(String[] args) {
        Object[] forks = new Object[NUM_PHILOSOPHERS];
        Philosopher[] philosophers = new Philosopher[NUM_PHILOSOPHERS];

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Object(); // יצירת מנע לכל מזלג
        }

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % NUM_PHILOSOPHERS];

            philosophers[i] = new Philosopher(i, leftFork, rightFork);
            philosophers[i].start();
        }
    }
}
