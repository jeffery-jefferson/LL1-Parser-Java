package Models;

import java.util.HashSet;
import java.util.List;

import Exceptions.InvalidNodeException;

public class ParseTree<T> {
    private TreeNode<T> root;
    private TreeNode<T> currentNode;
    private HashSet<TreeNode<T>> nodes;

    public ParseTree(TreeNode<T> root) {
        this.root = root;
        this.currentNode = this.root;
        this.nodes = new HashSet<TreeNode<T>>();
        this.nodes.add(this.root);
    }

    public void SetCurrentNode(TreeNode<T> node) throws InvalidNodeException  {
        if (nodes.contains(node)) {
            this.currentNode = node;
        } else {
            throw new InvalidNodeException("Cannot set current node because node does not exist in the tree.");
        }
    }

    public void Add(TreeNode<T> node) {
        this.currentNode.AddChild(node);
        this.nodes.add(node);
    }
    public void Add(TreeNode<T> fromNode, TreeNode<T> toNode) throws InvalidNodeException {
        if (nodes.contains(fromNode)) {
            fromNode.AddChild(toNode);
            this.nodes.add(toNode);
        } else {
            throw new InvalidNodeException("Cannot add toNode to fromNode because fromNode does not exist.");
        }
    }

    public TreeNode<T> GetTreeNodeByValue(T value) {
        TreeNode<T> result = null;
        for (var node : this.nodes) {
            if (node.GetValue() == value) {
                result = node;
                break;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        var curr = this.root;
        var nextChildren = new HashSet<TreeNode<T>>();
        nextChildren.addAll(curr.GetChildren());
        result += curr.GetValue() + "\n";
        return result;
    }
}
