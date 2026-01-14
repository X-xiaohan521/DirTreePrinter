package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeDisplay extends JTree {

    public TreeDisplay() {
        setModel(createEmptyModel());
        setRootVisible(false);
        setShowsRootHandles(true);
        setCellRenderer(new CheckBoxTreeCellRenderer());
    }

    public void generateUiTree(TreeNode rootNode) {
        DefaultMutableTreeNode rootUiNode = generateUiTreeNode(rootNode);
        setModel(new DefaultTreeModel(rootUiNode));
        setRootVisible(true);
    }

    private DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
        DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);
        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child));
        }
        return uiNode;
    }

    public boolean isEmpty() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        return root == null || root.getChildCount() == 0;
    }

    public void clear() {
        setModel(createEmptyModel());
        setRootVisible(false);
    }

    private static DefaultTreeModel createEmptyModel() {
        return new DefaultTreeModel(new DefaultMutableTreeNode(null));
    }
}
