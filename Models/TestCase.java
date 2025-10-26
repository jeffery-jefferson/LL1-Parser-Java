package Models;

public class TestCase {
    private String name;
    private String[] inputs;

    public TestCase(String name, String[] inputs) {
        this.name = name;
        this.inputs = inputs;
    }

    public String GetName() {
        return this.name;
    }

    public String[] GetInputs() {
        return this.inputs;
    }
}
