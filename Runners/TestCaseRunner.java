package Runners;

import java.io.FileWriter;
import java.io.IOException;

import Models.TestCase;
import Models.TestGroup;

public class TestCaseRunner {
    private static TestGroup[] testCases = new TestGroup[] {
        new TestGroup("Simple Valid", new String[] {
            "1",
            "x",
            "xxx",
            "(1)",
            "(x)",
            "(xxx)",
            "(+ 1 1)",
        }),
    };

    private static TestCase[] tests = new TestCase[] {
        new TestCase("SIMPLE NUMBER", "1", ""),
    };

    public static void Run() {
        var fileName = "testOutput.txt";
        for (var testCase : testCases) {
            for (var testInput : testCase.GetInputs()) {
                System.out.println("TEST RUN: " + testCase.GetName());
                var result = Runner.Run(testInput, false);
                System.out.println(result != "" ? "PASSED" : "FAILED");
                System.out.println("\n");

                try (FileWriter writer = new FileWriter(fileName, false)) {
                    writer.write("TEST RUN: " + testCase.GetName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try (FileWriter writer = new FileWriter(fileName, true)) { 
                    writer.write(result + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
