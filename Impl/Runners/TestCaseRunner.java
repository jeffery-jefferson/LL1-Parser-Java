package Impl.Runners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Exceptions.*;
import Impl.Parser.ParseTreeSimplifier;

import com.fasterxml.jackson.core.type.TypeReference;

import Models.*;

public class TestCaseRunner {
    private static final String testOutputFile = "testResults.json";
    private static final String testInputFile = "testCases.json";

    public static void Run() {
        var mapper = new ObjectMapper();
        try (var writer = new FileWriter(testOutputFile)) {
            // fetch tests from JSON file
            var tests = mapper.readValue(
                new File(testInputFile), 
                new TypeReference<List<TestCase>>() {}
            );

            var testResults = new ArrayList<TestCase.TestCaseResult>();
            for (var test : tests) {
                ParseTree<Token> resultTree = null;
                String exceptionMessage = null;

                try {
                    resultTree = Runner.Run(test.getInput().toString(), false);
                } catch (NumberException | ExpressionException | InvalidNodeException ex) {
                    exceptionMessage = ex.getMessage();
                    System.out.println("An exception occurred");
                }
                
                TestCase.TestCaseResult testResult = null;
                if (exceptionMessage == null || exceptionMessage == "") {
                    // simplify parse tree for ease of testing
                    String result = ParseTreeSimplifier.simplifyToString(resultTree.getRoot());
                    testResult = new TestCase.TestCaseResult(test.getName(), test.getInput(), result, test.getExpectedOutput());
                } else {
                    testResult = new TestCase.TestCaseResult(test.getName(), test.getInput(), exceptionMessage, test.getExpectedOutput());
                }
                testResults.add(testResult);
                
                System.out.println(testResult + "\n");
            }
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            var results = mapper.writeValueAsString(testResults);
            writer.write(results);

            System.out.println("PASSED " + testResults.stream().filter(x -> x.isPass()).count() + "/" + testResults.size());
            System.out.println("Test results outputted to " + System.getProperty("user.dir") + "/" + testOutputFile);

        } catch (IOException ex) {
            System.out.println("Could not JSONify test results: " + ex.getMessage() + "\n" + ex.getStackTrace());
        }
    }
}
