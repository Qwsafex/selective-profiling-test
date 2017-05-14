package org.jetbrains.test.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
public class MethodCall implements Serializable{
    private static final String PADDING_STRING = "-";
    @NotNull private final String threadName;
    @NotNull private String methodName;
    @NotNull private List<Argument> args;
    @NotNull private List<MethodCall> childrenCalls;
    @Nullable private MethodCall parentCall;

    public void saveToFile() throws IOException {
        String filename = threadName + "-0";
        int i = 0;
        while (Files.exists(Paths.get(filename))) {
            i++;
            filename = threadName + "-" + i;
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename));
        outputStream.writeObject(this);
        outputStream.close();
    }
    public static MethodCall readFromFile(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
        return (MethodCall) inputStream.readObject();
    }

    @Override
    public String toString() {
        return toStringRecursively(0);
    }


    MethodCall(@NotNull String methodName, @NotNull Object[] args) {
        this(methodName, args, null);
    }


    MethodCall(@NotNull String methodName, @NotNull Object[] args, @Nullable MethodCall parentCall) {
        this.threadName = Thread.currentThread().getName();
        this.methodName = methodName;
        this.args = Arrays.stream(args)
                .map(arg -> new Argument(arg.getClass().getSimpleName(), arg.toString()))
                .collect(Collectors.toList());
        childrenCalls = new ArrayList<>();
        this.parentCall = parentCall;
    }

    @Nullable
    MethodCall getParentCall() {
        return parentCall;
    }


    void addChild(MethodCall lastCall) {
        childrenCalls.add(lastCall);
    }

    private String toStringRecursively(int depth) {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append(String.join("", Collections.nCopies(depth, PADDING_STRING)));
        stringRepresentation.append(" ");
        stringRepresentation.append(methodName);
        stringRepresentation.append("(");
        stringRepresentation.append(String.join(", ",
                args.stream()
                        .map(arg -> arg.type + ": " + arg.value)
                        .collect(Collectors.toList())));
        stringRepresentation.append(")\n");
        childrenCalls.forEach(call -> stringRepresentation.append(call.toStringRecursively(depth + 1)));
        return stringRepresentation.toString();
    }
}
