package Models;

public class TestCase {
    public record TestCaseResult(String name, Object input, Object output, Object expectedOutput) {
        public boolean isPass() {
            return output.equals(expectedOutput);
        }
        @Override
        public String toString() {
            return "TEST CASE: " + name 
            + "\nInput:" + input
            + "\nExpected output: " + expectedOutput 
            + "\nOutput: " + output 
            + "\n" + (isPass() ? "PASSED" : "FAILED");
        }
    }

    private Object input;
    private Object expectedOutput;
    private String name;

    public TestCase() {}
    public TestCase(String name, Object input, Object expectedOutput) {
        this.name = name;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public String getName() {
        return this.name;
    }

    public Object getInput() {
        return this.input;
    }

    public Object getExpectedOutput() {
        return this.expectedOutput;
    }
}
