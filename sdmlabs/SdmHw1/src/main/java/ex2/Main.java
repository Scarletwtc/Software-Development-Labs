package ex2;

public class Main {
    public static void main(String[] args) {
        for(int i = 0; i<5; i++){
            int steps = 10 + (int)(Math.random() * 41);
            HwThread thread = new HwThread(i, steps);
            thread.start();
        }
    }
}
