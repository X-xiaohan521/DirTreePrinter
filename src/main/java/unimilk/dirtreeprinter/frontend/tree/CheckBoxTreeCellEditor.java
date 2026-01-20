package unimilk.dirtreeprinter.frontend.tree;

import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.util.Objects;

public class CheckBoxTreeCellEditor extends AbstractCellEditor
        implements TreeCellEditor {

    private final JCheckBox checkBox = new JCheckBox();
    private TreeNode currentNode;
    private final JTree tree;

    public CheckBoxTreeCellEditor(JTree tree) {
        this.tree = tree;

        checkBox.addActionListener(e -> onCheck());
    }

    @Override
    public Component getTreeCellEditorComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row) {

        DefaultMutableTreeNode uiNode =
                (DefaultMutableTreeNode) value;

        currentNode = (TreeNode) uiNode.getUserObject();

        checkBox.setSelected(currentNode.isSelected());
        checkBox.setEnabled(currentNode.isEnabled());

        return checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return currentNode;
    }

    private void onCheck() {
        if (!currentNode.isEnabled()) {
            cancelCellEditing();
            return;
        }

        boolean newState = checkBox.isSelected();
        currentNode.setSelected(newState);

        // applyPropagation(currentNode, newState);

        ((DefaultTreeModel) tree.getModel())
                .nodeStructureChanged(
                        (javax.swing.tree.TreeNode) Objects.requireNonNull(tree.getSelectionPath()).getLastPathComponent()
                );

        stopCellEditing();
    }
}
