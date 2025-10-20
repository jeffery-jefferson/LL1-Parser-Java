package Automata;

import java.util.Stack;

public class Pda extends Dfa {

    // PDA has a stack
    private Stack<Character> stack;

    public Pda(String inputString) {
        super(inputString);
        this.stack = new Stack<Character>();
    }
    
    
}
