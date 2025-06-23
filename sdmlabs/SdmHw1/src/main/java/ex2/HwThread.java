package ex2;
import java.util.Random;

public class HwThread extends Thread {
    private final int id;
    private final int steps;
    private final Random random = new Random();

    public HwThread(int id, int steps) {
        this.id = id;
        this.steps = steps;
    }

    @Override
    public void run() {
        for(int i = 0; i < steps; i++) {
            System.out.println("Thread with ID " + id + " is at step " + i + " out of " + steps + ".");
            try {
                Thread.sleep(10 + random.nextInt(100)); // sleep for a random time between 10 and 109 milliseconds

            }catch (InterruptedException e) {
                System.out.println("Thread with ID " + id + " was interrupted.");
                return;
            }
        }
        System.out.println("Thread with ID " + id + " has completed all steps.");
    }
}
