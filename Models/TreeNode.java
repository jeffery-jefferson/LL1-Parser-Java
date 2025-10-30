package Models;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private T val;
    private List<TreeNode<T>> children;

    public TreeNode() {}
    public TreeNode(T value) {
        this.val = value;
        this.children = new ArrayList<TreeNode<T>>();
    }

    public T getVal() {
        return this.val;
    }
    public void SetValue(T val) {
        this.val = val;
    }

    public List<TreeNode<T>> getChildren() {
        return this.children;
    }

    public void AddChild(TreeNode<T> child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }
}
