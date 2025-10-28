package Automata;

import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import Exceptions.ExpressionException;
import Models.State;
import Models.TransitionPair;
// mark was here
public class Dfa implements IAutomata {

    public HashSet<State> States;
    public HashMap<TransitionPair, State> Transitions;
    public HashSet<State> AcceptStates;
    public State StartState;
    public State CurrentState;

    private String inputString;
    private Character currentCharacterIndex;

    public Dfa(String inputString) {
        this.inputString = inputString;
        this.currentCharacterIndex = 0;

        this.States = new HashSet<State>();
        this.Transitions = new HashMap<TransitionPair, State>();
        this.AcceptStates = new HashSet<State>();
        this.StartState = null;
    }

    /*
    Next() goes to the next state if there are inputs to process.
    If it is finished processing it and is not in an accept state we throw an expression exception to symbolise rejecting the input string.
    */
    public Character Next() throws ExpressionException {
        if (this.IsFinished() && !this.IsAcceptState(this.CurrentState)) {
            throw new ExpressionException();
        }

        var transitionPair = new TransitionPair(this.CurrentState, this.inputString.charAt(this.currentCharacterIndex));
        var nextState = this.Transitions.get(transitionPair);

        if (nextState == null) { // i.e. invalid transition pair
            throw new ExpressionException("Invalid transition. No transition exists for state " + transitionPair.FromState.Name + " with input character '" + transitionPair.TransitionCharacter.toString() + "'.");
        }

        this.currentCharacterIndex++;
        this.CurrentState = nextState;
        return this.GetProcessedCharacter();
    }

    public Character GetCurrentCharacter() {
        return this.inputString.charAt(this.currentCharacterIndex);
    }

    public Character GetProcessedCharacter() {
        return this.inputString.charAt(this.currentCharacterIndex - 1);
    }

    public boolean IsFinished() {
        return this.currentCharacterIndex >= this.inputString.length();
    }

    public void AddState(State state) {
        this.States.add(state);
    }

    public boolean CanAccept() {
        return this.IsAcceptState(this.CurrentState) && this.IsFinished();
    }

    public void AddAcceptState(State state) {
        if (!this.States.contains(state)) {
            throw new IllegalArgumentException("State is not registered in this DFA. Please add it to States first.");
        }
        this.AcceptStates.add(state);
    }

    public void AddAcceptState(String stateName) {
        var state = GetState(stateName);
        this.AcceptStates.add(state);
    }

    public boolean IsAcceptState(State state) {
        return this.AcceptStates.contains(state);
    }

    public State GetState(String stateName) {
        State state = null;
        for (State s : this.States) {
            if (s.Name.equals(stateName)) {
                state = s;
                break;
            }
        }
        return state;
    }

    public void AddTransition(State fromState, State toState, Character inputCharacter) {
        var transitionPair = new TransitionPair(fromState, inputCharacter);
        this.Transitions.put(transitionPair, toState);
    }

    public void AddTransition(String fromStateName, String toStateName, Character inputCharacter) {
        var fromState = GetState(fromStateName);
        var toState = GetState(toStateName);
        this.AddTransition(fromState, toState, inputCharacter);
    }

    public void SetStartState(String stateName) {
        var state = this.GetState(stateName);
        this.StartState = state;
        this.CurrentState = state;
    }

    public void SetStartState(State state) {
        if (this.States.contains(state)) {
            this.StartState = state;
            this.CurrentState = state;
        } else {
            throw new UnsupportedOperationException("Cannot set an unregistered state as the start state. Please add this state first using Dfa.AddState(State state).");
        }
    }

    @Override
    public void AddStates(List<State> states) {
        for (var state : states) {
            AddState(state);
        }
    }

    @Override
    public void AddAcceptStates(List<State> states) {
        for (var state : states) {
            AddAcceptState(state);
        }
    }
}
