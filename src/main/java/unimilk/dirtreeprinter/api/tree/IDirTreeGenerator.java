package unimilk.dirtreeprinter.api.tree;

import unimilk.dirtreeprinter.api.settings.ISettings;

import java.io.IOException;
import java.nio.file.Path;

public interface IDirTreeGenerator {
    /**
     * Generate a tree structure of the Path specified according to the settings.
     * @param root the root Path of the directory tree
     * @param settings the settings defining how to generate the tree
     * @return the root node containing the tree
     * @throws IOException if any child of the root path cannot be reached
     */
    TreeNode generateTree(Path root, ISettings settings) throws IOException;
}
