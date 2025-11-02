package Impl.Runners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import Models.*;
import Models.Token.TokenType;

public class TestCaseRunner {
    private static final String testOutputFile = "testResults.json";
    private static final String testInputFile = "testCases.json";

    public static void Run() {
        var mapper = new ObjectMapper();
        try (var writer = new FileWriter(testOutputFile)) {
            var tests = mapper.readValue(
                new File(testInputFile), 
                new TypeReference<List<TestCase>>() {}
            );
            var testResults = new ArrayList<TestCase.TestCaseResult>();
            for (var test : tests) {
                var resultTree = Runner.Run(test.getInput().toString(), false);
                var simplified = simplify(resultTree.getRoot());
                String result = null;
                if (simplified.size() == 1 && simplified.get(0) instanceof List<?>) {
                    result = simplified.get(0).toString().trim();
                } else {
                    result = simplified.toString().trim();
                }
                var testResult = new TestCase.TestCaseResult(test.getName(), result, test.getExpectedOutput());
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

    private static List<Object> simplify(TreeNode<Token> node) {
        if (node == null) return null;

        var value = node.getVal() == null ? null : node.getVal().toString();

        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            if (node.getVal().Type != TokenType.EMPTY && node.getVal().Type != TokenType.OPEN_PARENTHESES && node.getVal().Type != TokenType.CLOSE_PARENTHESES) {
                return List.of(value);
            } else {
                return new ArrayList<>();
            }
        }
        var mergedChildren = new ArrayList<>();
        for (var child : node.getChildren()) {
            var simplifiedChildren = simplify(child);
            if (!simplifiedChildren.isEmpty()) {
                if (simplifiedChildren.size() == 1) {
                    mergedChildren.addAll(simplifiedChildren);
                } else {
                    mergedChildren.add(simplifiedChildren);
                }
            }
        }
        return mergedChildren;
    }
}
