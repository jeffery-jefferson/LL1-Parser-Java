package Models;

import java.util.HashSet;

import Exceptions.InvalidNodeException;

public class ParseTree<T> {
    private TreeNode<T> root;
    private TreeNode<T> currentNode;
    private HashSet<TreeNode<T>> nodes;

    public ParseTree(TreeNode<T> root) {
        this.root = root;
        this.currentNode = this.root;
        this.nodes = new HashSet<TreeNode<T>>();
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
    }
    public void Add(TreeNode<T> fromNode, TreeNode<T> toNode) throws InvalidNodeException {
        if (nodes.contains(fromNode)) {
            fromNode.AddChild(toNode);
        } else {
            throw new InvalidNodeException("Cannot add toNode to fromNode because fromNode does not exist.");
        }
    }
}
