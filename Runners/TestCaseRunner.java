package Runners;

import Models.TestCase;

public class TestCaseRunner {
    private static TestCase[] testCases = new TestCase[] {
        new TestCase("Simple", new String[] {
            "1",
            "x",
            "(1)",
            "(x)",
            "(+ 1 1)",
        }),
    };

    public static void Run() {
        for (var testCase : testCases) {
            for (var testInput : testCase.GetInputs()) {
                Runner.Run(testInput);
            }
        }
    }
}
