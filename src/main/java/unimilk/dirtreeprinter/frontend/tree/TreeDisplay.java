package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;
import unimilk.dirtreeprinter.frontend.RightClickMenu;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;

public class TreeDisplay extends JTree {

    private RightClickMenu rightClickMenu;

    public TreeDisplay() {
        setModel(createEmptyModel());
        setRootVisible(false);
        setShowsRootHandles(true);
        setEditable(false);
        setToggleClickCount(0);
        setCellRenderer(new CheckBoxTreeCellRenderer());

        TreeCellClickListener treeCellClickListener = new TreeCellClickListener(this);
        treeCellClickListener.setPopupMenuHandler(this::showPopupMenu);
        addMouseListener(treeCellClickListener);
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
        this.setExpandedState(new TreePath(uiNode.getPath()), true);
        return uiNode;
    }

    public void setRightClickMenu(RightClickMenu rightClickMenu) {
        this.rightClickMenu = rightClickMenu;
    }

    private void showPopupMenu(MouseEvent e, DefaultMutableTreeNode uiNode) {
        if (rightClickMenu != null) {
            rightClickMenu.showFor(this, e, uiNode);
        }
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
