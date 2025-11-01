package Models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Exceptions.InvalidNodeException;
import Exceptions.InvalidNodeOperationException;

public class ParseTree<T> {
    private TreeNode<T> root;
    private TreeNode<T> currentNode;
    private List<TreeNode<T>> nodes;

    public ParseTree(TreeNode<T> root) {
        this.root = root;
        this.currentNode = this.root;
        this.nodes = new ArrayList<TreeNode<T>>();
        this.nodes.add(this.root);
    }

    public ParseTree() {} // for deserialization
    public TreeNode<T> getRoot() { return root; }
    @JsonIgnore
    public TreeNode<T> getCurrentNode() { return currentNode; }
    @JsonIgnore
    public List<TreeNode<T>> getNodes() { return nodes; }

    public void SetCurrentNode(TreeNode<T> node) throws InvalidNodeException  {
        if (nodes.contains(node)) {
            this.currentNode = node;
        } else {
            throw new InvalidNodeException("Cannot set current node because node does not exist in the tree.");
        }
    }

    public void Add(TreeNode<T> node) throws InvalidNodeOperationException {
        if (!this.nodes.contains(node)) {
            this.currentNode.AddChild(node);
            this.nodes.add(node);
        } else {
            throw new InvalidNodeOperationException("Cannot add node as it already exists in the tree.");
        }
    }
    public void Add(TreeNode<T> fromNode, TreeNode<T> toNode) throws InvalidNodeException {
        if (nodes.contains(fromNode)) {
            fromNode.AddChild(toNode);
            this.nodes.add(toNode);
        } else {
            throw new InvalidNodeException("Cannot add toNode to fromNode because fromNode does not exist.");
        }
    }

    public TreeNode<T> getTreeNode(T value) {
        TreeNode<T> result = null;
        for (var node : this.nodes) {
            if (node.getVal() == value) {
                result = node;
                break;
            }
        }
        return result;
    }

    @JsonIgnore
    public List<TreeNode<T>> getTerminalNodes() {
        List<TreeNode<T>> terminals = new ArrayList<>();

        for (TreeNode<T> node : nodes) {
            if (node.getChildren() == null || node.getChildren().isEmpty()) {
                terminals.add(node);
            }
        }
        return terminals;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        buildString(this.root, sb, "", true);
        return sb.toString();
    }

    private void buildString(TreeNode<T> node, StringBuilder sb, String prefix, boolean isLast) {
        if (node == null) {
            return;
        }

        sb.append(prefix);
        sb.append(isLast ? "└── " : "├── ");
        sb.append(node.getVal()).append("\n");

        var children = node.getChildren();
        if (children == null || children.isEmpty()) {
            return;
        }

        for (int i = 0; i < children.size(); i++) {
            boolean lastChild = (i == children.size() - 1);
            buildString(children.get(i), sb, prefix + (isLast ? "    " : "│   "), lastChild);
        }
    }
}
