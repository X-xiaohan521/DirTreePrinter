package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseEvent;

import java.util.function.Consumer;

public class TreeDisplay extends JTree {

    private Consumer<TreeNode> ignoreHandler;

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
        return uiNode;
    }

    public void setIgnoreHandler(Consumer<TreeNode> ignoreHandler) {
        this.ignoreHandler = ignoreHandler;
    }

    private void showPopupMenu(MouseEvent e, TreeNode node) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem ignoreItem = new JMenuItem("Add to ignore rules");
        ignoreItem.addActionListener(ev -> {
            if (ignoreHandler != null) {
                ignoreHandler.accept(node);
            }
        });
        menu.add(ignoreItem);
        menu.show(this, e.getX(), e.getY());
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
