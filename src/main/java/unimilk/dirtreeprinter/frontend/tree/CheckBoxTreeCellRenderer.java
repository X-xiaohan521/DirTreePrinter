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

        if (backendNode != null) {
            boolean isEnabled = backendNode.isEnabled();

            checkBox.setSelected(isEnabled && backendNode.isSelected());
            label.setText(" " + backendNode.getPath().getFileName().toString());

            if (!isEnabled) {
                label.setForeground(UIManager.getColor("Label.disabledForeground"));
            } else {
                label.setForeground(UIManager.getColor("Tree.textForeground"));
            }
        }

        return this;
    }
}
