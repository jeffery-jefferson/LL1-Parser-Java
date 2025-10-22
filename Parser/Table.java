package Parser;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Table<TRow, TCol, TVal> {
    /*
     * I'm thinking this is just a wrapper for a map with easier access
     * Rows and columns won't be tokens anymore, they will just be strings.
     * 
     * In this way, we can more easily compare stuff and it's just more flexible.
     * The upper implementing class should determine which columns and rows.
     */
    private record Pair<A, B>(A first, B second) {}

    private HashMap<Pair<TRow, TCol>, TVal> table;

    public Table() {
        this.table = new HashMap<Pair<TRow, TCol>, TVal>();
    }

    public void Add(TRow row, TCol col, TVal value) {
        var key = new Pair<TRow, TCol>(row, col);
        if (!this.table.keySet().contains(key)) {
            this.table.put(key, value);
        }
    }

    public List<TRow> GetRows() {
        var result = new ArrayList<TRow>();
        for (var rowColPair : this.table.keySet()) {
            result.add(rowColPair.first);
        }
        return result;
    }
    public HashMap<TCol, TVal> GetRow(TRow row) {
        var result = new HashMap<TCol, TVal>();
        for (var kvp : this.table.keySet()) {
            if (kvp.first == row) {
                result.put(kvp.second, this.table.get(kvp));
            }
        }
        return result;
    }

    public List<TCol> GetColumns() {
        var result = new ArrayList<TCol>();
        for (var rowColPair : this.table.keySet()) {
            result.add(rowColPair.second);
        }
        return result;
    }

    public TVal Get(TRow row, TCol col) {
        return this.table.get(new Pair<TRow, TCol>(row, col));
    }
}