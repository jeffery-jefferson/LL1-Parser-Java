package Models;

public class TestCase {
    private Object input;
    private Object expectedOutput;
    private String name;

    public TestCase(String name, Object input, Object expectedOutput) {
        this.name = name;
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    public String GetName() {
        return this.name;
    }

    public boolean GetResult() {
        return this.input == this.expectedOutput;
    }
}
