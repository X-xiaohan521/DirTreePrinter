package unimilk.dirtreeprinter.backend.tree;

import java.nio.file.Path;
import java.util.List;

public class TreeNode {
    private final Path path;
    private final List<TreeNode> children;
    private boolean enabled;

    public TreeNode(Path path, List<TreeNode> children, boolean enabled) {
        this.path = path;
        this.children = children;
        this.enabled = enabled;
    }

    public Path getPath() {
        return path;
    }


    public List<TreeNode> getChildren() {
        return children;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
