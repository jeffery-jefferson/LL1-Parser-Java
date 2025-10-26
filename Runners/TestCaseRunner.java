package Runners;

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

    public static void Run() {
        for (var testCase : testCases) {
            for (var testInput : testCase.GetInputs()) {
                System.out.println("TEST RUN: " + testCase.GetName());
                var result = Runner.Run(testInput, false);
                System.out.println(result ? "PASSED" : "FAILED");
            }
        }
    }
}
