# LL1 Parser
By Mark Ureta
@ 2025

---

This is a simple LL(1) Parser implementation that recognises the following MiniLisp grammar.

```
1. <program> → <expr>
2. <expr> → NUMBER
3. <expr> → IDENTIFIER
4. <expr> → '(' <paren-expr> ')'
5. <paren-expr> → '+' <expr> <expr>
6. <paren-expr> → '×' <expr> <expr>
7. <paren-expr> → '=' <expr> <expr>
8. <paren-expr> → '−' <expr> <expr>
9. <paren-expr> → '?' <expr> <expr> <expr>
10. <paren-expr> → 'λ' IDENTIFIER <expr>
11. <paren-expr> → '≜' IDENTIFIER <expr> <expr>
12. <paren-expr> → <expr> <expr>*
13. <expr>* → <expr> <expr>* (expanded rule from 12)
14. <expr>* → 𝛆 (expanded rule from 12)
```

# Overview

## Lexer
The lexer is responsible for tokenisation and relies on the same DFA architecture from the previous Assignment 1. 

This does not have any of the grammar validation and simply determines the valid alphabet, simple transitions and acceptance states.

For example, it can reject strings with "identifiers" that start with the digit 0, as required by the specification. However, it cannot reject strings with a mismatched parentheses or any invalid pattern between expression components.

## Parser
The parser is the main source of logic for the project. This uses a parsing table generated from the FIRST and FOLLOW sets of the grammar to successfully predict the necessary production rules needed to generate the string. It returns a parse tree.

## Runner
This orchestrates all the moving parts by tying together the Lexer and the Parser. The tokenised output from the Lexer is passed onto the Parser for parse tree generation, and the runner returns this parse tree.

## Test Runner
This runs test cases provided in the `testCases.json` file. For simplicity, it flattens the parse tree into a more readable nested list format and uses this for validation.

## Program
This is the sole entry point for the whole program. It is executed by the `run` scripts since we have no compilation frameworks like Gradle or Maven, and we use external libraries for JSONifying the test cases; to compile and run the program manually would be a pain to remember and execute.

# Execution

Depending on your OS, the base command will change.

 - For MacOS use `./run.sh`
 - For Windows use `./run.bat`

Going forward, I will refer to the MacOS shell execution for simplicity. All parameters used are the same in both versions, so only the base command will be different.

## Parameters
```
./run.sh --test
./run.sh -t
```
Either of the above is used to run the tests in `testCases.json`

# Test Execution
To view the tests, look at the `testCases.json` file. You can add your own tests to this file by copying the first "TEST EXAMPLE". 

Once the tests have run, another JSON file will be created in this directory called `testResults.json`. This is much similar to the `testCases.json` file but has an additional "pass" boolean property to determine whether the test was successful or not.