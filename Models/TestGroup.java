package Models;

public class TestGroup {
    private String name;
    private String[] inputs;

    public TestGroup(String name, String[] inputs) {
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
