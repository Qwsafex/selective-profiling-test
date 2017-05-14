package org.jetbrains.test;

import org.jetbrains.test.logger.MultiThreadLogger;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
    private final List<String> args;
    private Random random = new Random(System.nanoTime());

    public DummyApplication(List<String> args) {
        this.args = args;
    }

    private boolean nextBoolean() {
        return random.nextBoolean();
    }

    private boolean stop() {
        return random.nextDouble() < 0.05;
    }

    private String nextArg() {
        int idx = random.nextInt(args.size());
        return args.get(idx);
    }

    private void sleep() {
        try {
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException ignored) {

        }
    }

    private void abc(String s) {
        MultiThreadLogger.enter(s);
        //your code here

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            def(nextArg());
        }
        else {
            xyz(nextArg());
        }

        MultiThreadLogger.exit();
    }

    private void def(String s) {
        MultiThreadLogger.enter(s);
        //your code here

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            xyz(nextArg());
        }

        MultiThreadLogger.exit();
    }

    private void xyz(String s) {
        MultiThreadLogger.enter(s);
        //your code here

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            def(nextArg());
        }

        MultiThreadLogger.exit();
    }

    public void start() {
        abc(nextArg());
    }
}
