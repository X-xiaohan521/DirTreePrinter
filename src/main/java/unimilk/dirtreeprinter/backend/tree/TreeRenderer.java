package unimilk.dirtreeprinter.backend.tree;

import unimilk.dirtreeprinter.api.tree.ITreeRenderer;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import java.util.List;
import java.util.stream.Collectors;

public class TreeRenderer implements ITreeRenderer {

    public TreeRenderer() {}

    @Override
    public String renderTree(TreeNode rootNode) {
        StringBuilder sb = new StringBuilder();
        sb.append(rootNode.getPath().getFileName()).append("/\n");
        buildTree(rootNode, "", sb);
        return sb.toString();
    }

    private void buildTree(TreeNode node, String prefix, StringBuilder sb) {
        if (node.getChildren().isEmpty()) {
            return;
        }

        List<TreeNode> enabledChildren = node.getChildren()
                .stream()
                .filter(TreeNode::isEnabled)
                .collect(Collectors.toList());

        for (int i = 0; i < enabledChildren.size(); i++) {
            TreeNode child = enabledChildren.get(i);
            boolean isLast = (i == enabledChildren.size() - 1);

            sb.append(prefix)
                    .append(isLast ?  "└── " : "├── ")
                    .append(child.getPath().getFileName())
                    .append("\n");

            if (!child.getChildren().isEmpty()) {
                String newPrefix = prefix + (isLast ? "    " : "│   ");
                buildTree(child, newPrefix, sb);
            }
        }
    }
}
