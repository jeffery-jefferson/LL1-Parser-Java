package Models;

import java.util.Objects;

public class TransitionPair {

    public State FromState;
    public Character TransitionCharacter;

    public TransitionPair(State fromState, Character transitionCharacter) {
        this.FromState = fromState;
        this.TransitionCharacter = transitionCharacter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionPair that = (TransitionPair) o;
        return Objects.equals(FromState, that.FromState) &&
               Objects.equals(TransitionCharacter, that.TransitionCharacter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FromState, TransitionCharacter);
    }
}
