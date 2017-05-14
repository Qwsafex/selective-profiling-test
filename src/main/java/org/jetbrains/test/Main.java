package org.jetbrains.test;

import org.jetbrains.test.logger.MethodCall;
import org.jetbrains.test.logger.MultiThreadLogger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        ExecutorService service = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 5; i++) {
            int start = 100 * i;
            List<String> arguments = IntStream.range(start, start + 10)
                    .mapToObj(Integer :: toString)
                    .collect(Collectors.toList());
            service.submit(() -> new DummyApplication(arguments).start());
        }

        service.shutdown();
        while (!service.isTerminated()) {
            service.awaitTermination(1, TimeUnit.SECONDS);
        }
        MultiThreadLogger.getCallTrees().forEach(System.out::println);

        for (MethodCall call : MultiThreadLogger.getCallTrees()) {
            call.saveToFile();
        }
    }
}
