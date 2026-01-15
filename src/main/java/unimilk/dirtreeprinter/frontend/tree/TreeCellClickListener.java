package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TreeCellClickListener implements MouseListener {
    private final JTree tree;

    public TreeCellClickListener(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tree.getRowForLocation(e.getX(), e.getY());
        if (row < 0) return;

        TreePath path = tree.getPathForRow(row);
        Rectangle bounds = tree.getRowBounds(row);

        int checkboxWidth = 20;
        if (e.getX() > bounds.x && e.getX() < bounds.x + checkboxWidth) {

            DefaultMutableTreeNode uiNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            TreeNode backendNode = (TreeNode) uiNode.getUserObject();

            if (!backendNode.isEnabled()) return;

            boolean newState = !backendNode.isSelected();
            backendNode.setSelected(newState);

            if (newState) {
                checkParents(uiNode);
                checkChildren(uiNode);
            } else {
                uncheckChildren(uiNode);
            }

            ((DefaultTreeModel) tree.getModel()).nodeChanged(uiNode);
            tree.repaint();
        }
    }

    private void checkParents(DefaultMutableTreeNode uiNode) {
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();
        backendNode.setSelected(true);
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) uiNode.getParent();
        if (parent != null) {
            checkParents(parent);
        }
    }

    private void uncheckChildren(DefaultMutableTreeNode uiNode) {
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();
        backendNode.setSelected(false);
        uncheckChildren(backendNode);
    }

    private void uncheckChildren(TreeNode rootNode) {
        if (!rootNode.getChildren().isEmpty()) {
            for (TreeNode child : rootNode.getChildren()) {
                child.setSelected(false);
                uncheckChildren(child);
            }
        }
    }

    private void checkChildren(DefaultMutableTreeNode uiNode) {
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();
        backendNode.setSelected(true);
        checkChildren(backendNode);
    }

    private void checkChildren(TreeNode rootNode) {
        if (!rootNode.getChildren().isEmpty()) {
            for (TreeNode child : rootNode.getChildren()) {
                child.setSelected(true);
                checkChildren(child);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
