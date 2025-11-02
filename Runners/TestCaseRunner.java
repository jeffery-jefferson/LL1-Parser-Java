package Runners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Models.*;
import Models.Token.TokenType;

public class TestCaseRunner {
    private static TestCase[] tests = new TestCase[] {
        new TestCase("SIMPLE NUMBER", "1", "[1]"),
        new TestCase("SIMPLE IDENTIFIER", "x", "[x]"),
        new TestCase("SIMPLE PAREN PLUS", "(+ 2 3)", "[+, 2, 3]"),
        new TestCase("SIMPLE PAREN MULTIPLY", "(× x 5)", "[×, x, 5]"),
        new TestCase("NESTED NORMAL", "(+ (× 2 3) 4)", "[+, [×, 2, 3], 4]"),
        new TestCase("NESTED CONDITIONAL", "(? (= x 0) 1 0)", "[?, [=, x, 0], 1, 0]"),
        new TestCase("LAMBDA", "(λ x x)", "[λ, x, x]"),
        new TestCase("LET", "(≜ y 10 y)", "[≜, y, 10, y]"),
        new TestCase("NESTED LAMBDA", "((λ x (+ x 1)) 5)", "[[λ, x, [+, x, 1]], 5]"),
    };

    public static void Run() {
        var fileName = "testOutput.json";
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

        try (var writer = new FileWriter(fileName)) {
            var mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            var results = mapper.writeValueAsString(testResults);
            writer.write(results);
        } catch (IOException ex) {
            System.out.println("Could not JSONify test results: " + ex.getMessage() + "\n" + ex.getStackTrace());
        }
        System.out.println("PASSED " + testResults.stream().filter(x -> x.isPass()).count() + "/" + testResults.size());
        System.out.println("Test results outputted to " + System.getProperty("user.dir") + "/" + fileName);
    }

    private static List<Object> simplify(TreeNode<Token> node) {
        if (node == null) return null;

        var value = node.getVal() == null ? null : node.getVal().toString();

        // leaf nodes -> also the same as non-terminal symbols
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            if (node.getVal().Type != TokenType.EMPTY && node.getVal().Type != TokenType.OPEN_PARENTHESES && node.getVal().Type != TokenType.CLOSE_PARENTHESES) {
                return List.of(value);
            } else {
                return new ArrayList<>();
            }
        }
        // non terminals always have children
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
