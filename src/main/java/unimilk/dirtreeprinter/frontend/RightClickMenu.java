package unimilk.dirtreeprinter.frontend;

import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseEvent;

public class RightClickMenu extends JPopupMenu {

    private final ISettingsManager settingsManager;
    private final JMenuItem openItem = new JMenuItem("Open in file explorer");
    private final JMenuItem ignoreItem = new JMenuItem("Add to ignore rules");
    private TreeNode node;

    public RightClickMenu(ISettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        ignoreItem.addActionListener(e -> onAddIgnore());
        add(openItem);
        add(ignoreItem);
    }

    private void setIgnoreButtonFor(TreeNode node) {
        this.node = node;
        ignoreItem.setEnabled(node.isEnabled());
    }

    private void onAddIgnore() {
        String name = node.getPath().getFileName().toString();
        ISettings settingsCopy = settingsManager.getSettings().copy();
        settingsCopy.addRule(name);
        settingsManager.applyAndSaveSettingsFrom(settingsCopy);
    }

    public void showFor(Component invoker, MouseEvent e, DefaultMutableTreeNode uiNode) {
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();
        setIgnoreButtonFor(backendNode);
        show(invoker, e.getX(), e.getY());
    }
}