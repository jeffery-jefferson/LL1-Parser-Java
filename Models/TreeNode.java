package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeNode<T> {
    private T val;
    private List<TreeNode<T>> children;

    public TreeNode(T value) {
        this.val = value;
        this.children = new ArrayList<TreeNode<T>>();
    }

    public T GetValue() {
        return this.val;
    }

    public List<TreeNode<T>> GetChildren() {
        return this.children;
    }

    public void AddChild(TreeNode<T> child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }
}
