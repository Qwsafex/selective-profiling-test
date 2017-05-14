package org.jetbrains.test;

import org.jetbrains.test.logger.MethodCall;

import java.io.IOException;


/**
 * Class that provides main() method which reads
 * call tress from files provided in command line arguments.
 */
public class CallTreeReader {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        for (String filename : args) {
            System.out.println(MethodCall.readFromFile(filename));
        }
    }
}
