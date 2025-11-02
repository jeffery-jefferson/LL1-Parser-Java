package Impl;

import Impl.Runners.MainRunner;
import Impl.Runners.TestCaseRunner;

public class Program {
    public static void main(String[] args) {
        String runTests = args.length >= 1 ? args[0] : "false";
        System.out.println("LL1 Parser - Mark Ureta 2025");
        if (Boolean.parseBoolean(runTests)) {
            TestCaseRunner.Run();
        } else {
            MainRunner.Run();
        }
    }
}
