package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer {

    private final JCheckBox checkBox = new JCheckBox();
    private final JLabel label = new JLabel();

    public CheckBoxTreeCellRenderer() {
        setLayout(new BorderLayout());
        add(checkBox, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
        setOpaque(false);
    }

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        DefaultMutableTreeNode uiNode = (DefaultMutableTreeNode) value;
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();

        checkBox.setSelected(backendNode.isSelected());
        label.setText(backendNode.getPath().getFileName().toString());

        return this;
    }
}
