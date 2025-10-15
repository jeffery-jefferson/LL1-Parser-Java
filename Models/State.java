package Models;

import java.util.Objects;

public class State {
    public String Name;

    public State(String name) {
        this.Name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;
        return Objects.equals(Name, state.Name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name);
    }

    @Override
    public String toString() {
        return Name;
    }
}
