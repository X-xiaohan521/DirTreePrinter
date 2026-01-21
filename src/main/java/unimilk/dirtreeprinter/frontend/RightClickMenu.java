package unimilk.dirtreeprinter.frontend;

import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.nio.file.Files;

public class RightClickMenu extends JPopupMenu {

    private final ISettingsManager settingsManager;
    private final JMenuItem openItem = new JMenuItem("Open in file explorer");
    private final JMenuItem ignoreItem = new JMenuItem("Add to ignore rules");
    private TreeNode node;

    public RightClickMenu(ISettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        openItem.addActionListener(e -> onOpenRequested());
        ignoreItem.addActionListener(e -> onAddIgnore());
        add(openItem);
        add(ignoreItem);
    }

    private void setMenuFor(TreeNode node) {
        this.node = node;
        openItem.setText(Files.isDirectory(node.getPath()) ? "Open Folder" : "Open File");
        ignoreItem.setEnabled(node.isEnabled());
    }

    private void onOpenRequested() {
        try {
            if (!Desktop.isDesktopSupported()) {
                MainFrontend.showError(this, "Desktop API not supported on this system.");
                return;
            }

            Desktop desktop = Desktop.getDesktop();

            if (!desktop.isSupported(Desktop.Action.OPEN)) {
                MainFrontend.showError(this, "Open action not supported on this system.");
                return;
            }

            desktop.open(node.getPath().toFile());

        } catch (Exception ex) {
            MainFrontend.showError(this, ex);
        }
    }

    private void onAddIgnore() {
        String name = node.getPath().getFileName().toString();
        ISettings settingsCopy = settingsManager.getSettings().copy();
        settingsCopy.addRule(name);
        settingsManager.applyAndSaveSettingsFrom(settingsCopy);
    }

    public void showFor(Component invoker, MouseEvent e, DefaultMutableTreeNode uiNode) {
        TreeNode backendNode = (TreeNode) uiNode.getUserObject();
        setMenuFor(backendNode);
        show(invoker, e.getX(), e.getY());
    }
}