package Models;

public class TestCase {
    public record TestCaseResult(String name, Object output, Object expectedOutput) {
        public String getResult() {
            return output.equals(expectedOutput) ? "PASS" : "FAILED";
        }
        @Override
        public String toString() {
            return "TEST CASE: " + name 
            + "\nExpected output: " + expectedOutput 
            + "\nOutput: " + output 
            + "\n" + getResult();
        }
    }

    private Object input;
    private Object expectedOutput;
    private String name;

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
