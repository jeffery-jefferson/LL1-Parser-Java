package Runners;

import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import Models.TestCase;
import Models.TestGroup;

public class TestCaseRunner {
    private static TestGroup[] testCases = new TestGroup[] {
        new TestGroup("Simple Valid", new String[] {
            "(x)",
        }),
    };

    private static TestCase[] tests = new TestCase[] {
        new TestCase("SIMPLE NUMBER", "1", ""),
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
}
