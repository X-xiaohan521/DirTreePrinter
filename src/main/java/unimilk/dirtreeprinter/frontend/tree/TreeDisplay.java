package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeDisplay extends JTree {

    public TreeDisplay() {
        setVisible(false);
        setModel(new DefaultTreeModel(null));
    }

    public void generateUiTree(TreeNode rootNode) {
        DefaultMutableTreeNode rootUiNode = generateUiTreeNode(rootNode);
        setModel(new DefaultTreeModel(rootUiNode));
        setCellRenderer(new CheckBoxTreeCellRenderer());
        setVisible(true);
    }

    private DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
        DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);
        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child));
        }
        return uiNode;
    }

    public boolean isEmpty() {
        // TODO
        return true;
    }

    public void clear() {
        setVisible(false);
    }
}
