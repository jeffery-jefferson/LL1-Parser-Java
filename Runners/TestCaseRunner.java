package Runners;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Models.TestCase;
import Models.TestGroup;
import Models.Token;
import Models.Token.TokenType;
import Models.TreeNode;

public class TestCaseRunner {
    private static TestGroup[] testCases = new TestGroup[] {
        new TestGroup("Simple Valid", new String[] {
            "(x)",
            "(+ 1 1)"
        }),
    };

    private static TestCase[] tests = new TestCase[] {
        new TestCase("SIMPLE NUMBER", "1", "[1]"),
    };

    public static void Run() {
        var fileName = "testOutput.txt";
        for (var testCase : testCases) {
            for (var testInput : testCase.GetInputs()) {
                System.out.println("TEST RUN: " + testCase.GetName());
                var result = Runner.Run(testInput, false);
                var mapper = new ObjectMapper();
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
                String jsonResult = "";
                try {
                    jsonResult = mapper.writeValueAsString(result);
                } catch (JsonProcessingException ex) {
                    System.out.println("An error occurred during parse tree JSON conversion: " + ex.getMessage());
                    return;
                }
                System.out.println(simplify(result.getRoot()));
                System.out.println(jsonResult != "" ? "PASSED" : "FAILED");
                System.out.println("\n");

                try (FileWriter writer = new FileWriter(fileName, false)) {
                    writer.write("TEST RUN: " + testCase.GetName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try (FileWriter writer = new FileWriter(fileName, true)) { 
                    writer.write(jsonResult + "\n\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        if (node.getVal().Type == TokenType.NON_TERMINAL) {
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
        }
        return mergedChildren;
    }
}
