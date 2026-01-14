package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreeDisplayPanel extends JPanel {

    private JTree tree;

    public TreeDisplayPanel() {
        setFont(new Font("Monospaced", Font.PLAIN, 13));
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        add(tree);
    }

    private DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
        DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);
        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child));
        }
        return uiNode;
    }

    public void generateUiTree(TreeNode rootTreeNode) {
        DefaultMutableTreeNode rootUiTreeNode = generateUiTreeNode(rootTreeNode);
        this.tree = new JTree(rootUiTreeNode);
    }

    public boolean isEmpty() {
        return this.tree.equals(new JTree());
    }

    public void clear() {
        this.tree = new JTree();
    }
}
