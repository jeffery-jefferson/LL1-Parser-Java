package Automata;

import java.util.List;

import Exceptions.ExpressionException;
import Models.State;

public interface IAutomata {
    public Character Next() throws ExpressionException;
    public boolean IsFinished();
    public Character GetCurrentCharacter();
    public void AddState(State state);
    public void AddStates(List<State> states);
    public void AddAcceptState(State state);
    public void AddAcceptState(String stateName);
    public void AddAcceptStates(List<State> states);
    public boolean IsAcceptState(State state);
    public boolean CanAccept();
    public State GetState(String stateName);
    public void AddTransition(State fromState, State toState, Character inputCharacter);
    public void AddTransition(String fromStateName, String toStateName, Character inputCharacter);
    public void SetStartState(String stateName);
    public void SetStartState(State state);
}
