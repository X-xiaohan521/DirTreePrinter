package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.api.tree.TreeNode;
import unimilk.dirtreeprinter.frontend.RightClickMenu;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TreeDisplay extends JTree {

    private final ISettingsManager settingsManager;
    private RightClickMenu rightClickMenu;
    private final List<TreePath> pathsToExpand = new ArrayList<>();

    public TreeDisplay(ISettingsManager settingsManager) {
        this.settingsManager = settingsManager;

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
        DefaultMutableTreeNode rootUiNode = generateUiTreeNode(rootNode, 0);
        setModel(new DefaultTreeModel(rootUiNode));
        for (TreePath path : pathsToExpand) {
            expandPath(path);
        }
        pathsToExpand.clear();
        setRootVisible(true);
    }

    private DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode, int depth) {
        DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);

        if (depth <= settingsManager.getSettings().getDefaultExpandedLayers()) {
            pathsToExpand.add(new TreePath(uiNode.getPath()));
        }

        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child, depth - 1));
        }

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
