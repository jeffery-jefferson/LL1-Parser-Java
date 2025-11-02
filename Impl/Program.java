package Impl;
import java.util.Scanner;

import Impl.Runners.Runner;
import Impl.Runners.TestCaseRunner;

public class Program {
    public static void main(String[] args) {
        String runTests = args.length >= 1 ? args[0] : "false";
        if (Boolean.parseBoolean(runTests)) {
            TestCaseRunner.Run();
        } else {
            System.out.println("LL1 Parser - Mark Ureta 2025");
            System.out.println("Please enter input: ");
            
            var scanner = new Scanner(System.in);
            var input = scanner.nextLine();
            scanner.close();

            Runner.Run(input);
        }
    }
}
