package Runners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Models.*;
import Models.Token.TokenType;

public class TestCaseRunner {
    private static TestCase[] tests = new TestCase[] {
        new TestCase("SIMPLE NUMBER", "1", "[1]"),
        new TestCase("SIMPLE IDENTIFIER", "x", "[x]"),
    };

    public static void Run() {
        var fileName = "testOutput.txt";
        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try (var writer = new FileWriter(fileName)) {
            for (var test : tests) {
                var resultTree = Runner.Run(test.getInput().toString(), false);
                var result = simplify(resultTree.getRoot()).toString().trim();
                var testResult = new TestCase.TestCaseResult(test.getName(), result, test.getExpectedOutput());
                System.out.println(testResult + "\n");

                var testJson = mapper.writeValueAsString(testResult);
                writer.write(testJson + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Could not JSONify test result: " + ex.getMessage() + "\n" + ex.getStackTrace());
        }
    }

    private static List<Object> simplify(TreeNode<Token> node) {
        if (node == null) return null;

        var value = node.getVal() == null ? null : node.getVal().toString();

        // leaf nodes -> also the same as non-terminal symbols
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            if (node.getVal().Type != TokenType.EMPTY) {
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
