package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeDisplayPanel extends JPanel {

    public TreeDisplayPanel() {
        DefaultMutableTreeNode rootUiTreeNode = generateUiTreeNode(rootTreeNode);
        JTree tree = new JTree(rootUiTreeNode);
    }

    private DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
        DefaultMutableTreeNode uiNode =
                new DefaultMutableTreeNode(backendNode);

        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child));
        }
        return uiNode;
    }

}
