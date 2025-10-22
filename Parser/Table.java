package Parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Exceptions.InvalidTableOperationException;

public class Table<TRow, TCol, TVal> {
    /*
     * I'm thinking this is just a wrapper for a map with easier access
     * Rows and columns won't be tokens anymore, they will just be strings.
     * 
     * In this way, we can more easily compare stuff and it's just more flexible.
     * The upper implementing class should determine which columns and rows.
     */
    private record Pair<A, B>(A first, B second) {}

    private HashMap<Pair<TRow, TCol>, TVal> parsingTable;

    public Table() {
        this.parsingTable = new HashMap<Pair<TRow, TCol>, TVal>();
    }

    public void Add(TRow row, TCol col, TVal value) {
        var key = new Pair<TRow, TCol>(row, col);
        if (!this.parsingTable.keySet().contains(key)) {
            this.parsingTable.put(key, value);
        }
    }

    public HashSet<TRow> GetRows() {
        var result = new HashSet<TRow>();
        for (var rowColPair : this.parsingTable.keySet()) {
            result.add(rowColPair.first);
        }
        return result;
    }
    public HashSet<TCol> GetColumns() {
        var result = new HashSet<TCol>();
        for (var rowColPair : this.parsingTable.keySet()) {
            result.add(rowColPair.second);
        }
        return result;
    }
}