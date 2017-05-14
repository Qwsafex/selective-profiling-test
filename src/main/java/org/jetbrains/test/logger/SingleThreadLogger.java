package org.jetbrains.test.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class SingleThreadLogger {
    @NotNull private final String threadName;
    @NotNull private List<MethodCall> rootCalls;
    @Nullable private MethodCall currentCall = null;

    SingleThreadLogger(@NotNull String threadName) {
        this.threadName = threadName;
        rootCalls = new ArrayList<>();
    }


    void enter(int depthFromCaller, Object... args) {
        // method that called us is (getStackTrace() + this.enter + depthFromCaller)
        // away from the top of the stack
        String methodName = Thread.currentThread().getStackTrace()[2 + depthFromCaller].getMethodName();

        if (currentCall == null) {
            currentCall = new MethodCall(methodName, args);
            rootCalls.add(currentCall);
        }
        else {
            MethodCall lastCall = new MethodCall(methodName, args, currentCall);
            currentCall.addChild(lastCall);
            currentCall = lastCall;
        }
    }

    void exit() {
        assert currentCall != null;
        currentCall = currentCall.getParentCall();
    }

    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        int taskNum = 0;
        for (MethodCall call : rootCalls) {
            taskNum++;
            //noinspection StringConcatenationInsideStringBufferAppend
            stringRepresentation.append("Task " + taskNum + " in thread " + threadName + "\n");
            stringRepresentation.append(call.toString());
        }
        return stringRepresentation.toString();
    }

    List<MethodCall> getRoots() {
        return rootCalls;
    }
}
