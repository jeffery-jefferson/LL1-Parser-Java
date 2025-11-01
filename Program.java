import Runners.Runner;
import Runners.TestCaseRunner;

public class Program {
    public static void main(String[] args) {
        // based on args we should choose test case runner or normal runner
        TestCaseRunner.Run();
        Runner.Run("(+ 1 1)");
    }
}
