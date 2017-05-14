package org.jetbrains.test.logger;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiThreadLogger {
    @NotNull private static final Map<String, SingleThreadLogger> loggers = new HashMap<>();

    @NotNull
    public static List<MethodCall> getCallTrees() {
        return loggers.values().stream().flatMap(logger -> logger.getRoots().stream()).collect(Collectors.toList());
    }
    public static void enter(Object... args) {
        getThreadLogger().enter(1, args);
    }
    public static void exit() {
        getThreadLogger().exit();
    }

    @NotNull
    private static SingleThreadLogger getThreadLogger() {
        synchronized (loggers) {
            String threadName = getThreadName();
            if (!loggers.containsKey(threadName)) {
                loggers.put(threadName, new SingleThreadLogger(threadName));
            }
            return loggers.get(getThreadName());
        }
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }
}
