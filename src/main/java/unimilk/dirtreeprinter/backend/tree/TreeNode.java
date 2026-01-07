package unimilk.dirtreeprinter.backend.tree;

import java.nio.file.Path;
import java.util.List;

public class TreeNode {
    private Path path;
    private List<TreeNode> children;
    private boolean enabled;

    public TreeNode(Path path, List<TreeNode> children, boolean enabled) {
        this.path = path;
        this.children = children;
        this.enabled = enabled;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
