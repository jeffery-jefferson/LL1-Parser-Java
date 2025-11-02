package Impl.Parser;

import java.util.ArrayList;
import java.util.List;

import Models.Token;
import Models.Token.TokenType;
import Models.TreeNode;

public class ParseTreeSimplifier {
    public static String simplifyToString(TreeNode<Token> node) {
        var simplified = simplify(node);
        if (simplified.size() == 1 && simplified.get(0) instanceof List<?>) {
            return simplified.get(0).toString().trim();
        } 
        return simplified.toString().trim();
    }

    private static List<Object> simplify(TreeNode<Token> node) {
        if (node == null) return null;

        var value = node.getVal() == null ? null : node.getVal();

        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            if (node.getVal().Type != TokenType.EMPTY && node.getVal().Type != TokenType.OPEN_PARENTHESES && node.getVal().Type != TokenType.CLOSE_PARENTHESES) {
                if (node.getVal().Type == TokenType.NUMBER || node.getVal().Type == TokenType.IDENTIFIER) {
                    return List.of(value.toString());
                }
                return List.of(value.Type.toString());
            } else {
                return new ArrayList<>();
            }
        }
        var mergedChildren = new ArrayList<>();
        for (var child : node.getChildren()) {
            var simplifiedChildren = simplify(child);
            if (!simplifiedChildren.isEmpty()) {
                if (simplifiedChildren.size() == 1) {
                    mergedChildren.addAll(simplifiedChildren);
                } else {
                    mergedChildren.add(simplifiedChildren);
                }
            }
        }
        return mergedChildren;
    }
}
