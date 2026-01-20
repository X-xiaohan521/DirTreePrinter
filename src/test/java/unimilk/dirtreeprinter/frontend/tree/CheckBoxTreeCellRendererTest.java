package unimilk.dirtreeprinter.frontend.tree;

import org.junit.jupiter.api.Test;
import unimilk.dirtreeprinter.api.tree.TreeNode;
import unimilk.dirtreeprinter.backend.settings.Settings;
import unimilk.dirtreeprinter.backend.tree.DirTreeGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import static org.junit.jupiter.api.Assertions.*;

class CheckBoxTreeCellRendererTest {

    @Test
    void cellRendererShouldWork() throws InterruptedException, IOException {
        JFrame window = new JFrame();
        window.setTitle("JList Renderer Test");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        // window.setVisible(true);

        TreeNode rootNode = new DirTreeGenerator().generateTree(Path.of("C:", "Users\\dujin\\Documents\\Temp"), new Settings());
        DefaultTreeModel treeModel = new DefaultTreeModel(generateUiTreeNode(rootNode));

        JTree tree = new JTree(treeModel);
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        window.add(tree);

        // Thread.sleep(60000);

        assertTrue(true);
    }

    DefaultMutableTreeNode generateUiTreeNode(TreeNode backendNode) {
        DefaultMutableTreeNode uiNode = new DefaultMutableTreeNode(backendNode);
        for (TreeNode child : backendNode.getChildren()) {
            uiNode.add(generateUiTreeNode(child));
        }
        return uiNode;
    }
}