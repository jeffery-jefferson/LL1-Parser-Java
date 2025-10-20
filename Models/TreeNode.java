package Models;

import java.util.HashSet;
import java.util.UUID;

public class TreeNode<T> {
    private UUID id;
    private T value;
    private HashSet<TreeNode<T>> children;

    public TreeNode(T value) {
        this.value = value;
        this.children = new HashSet<TreeNode<T>>();
        this.id = UUID.randomUUID();
    }

    public UUID GetId() {
        return this.id;
    }

    public T GetValue() {
        return this.value;
    }

    public HashSet<TreeNode<T>> GetChildren() {
        return this.children;
    }

    public void AddChild(TreeNode<T> child) {
        this.children.add(child);
    }
}
