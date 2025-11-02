package Impl.Runners;

import java.util.Scanner;

import Exceptions.ExpressionException;
import Exceptions.InvalidNodeException;
import Exceptions.NumberException;

public class MainRunner {
    public static void Run() {
        try {
            System.out.println("Please enter input: ");
            
            var scanner = new Scanner(System.in);
            var input = scanner.nextLine();
            scanner.close();

            Runner.Run(input);
        } catch (NumberException ex) {
            System.out.println("Malformed number: " + ex.getMessage());
        } catch (ExpressionException ex) {
            System.out.println("Invalid expression: " + ex.getMessage());
        } catch (InvalidNodeException ex) {
            System.out.println("An error occurred during parse tree generation: " + ex.getMessage());
        }
    }
}
