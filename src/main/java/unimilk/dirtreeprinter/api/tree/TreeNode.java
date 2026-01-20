package unimilk.dirtreeprinter.api.tree;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class TreeNode {
    private final Path path;
    private final List<TreeNode> children;
    private boolean enabled;
    private boolean selected = true;

    public TreeNode(Path path, List<TreeNode> children, boolean enabled) {
        this.path = path;
        this.children = children;
        this.enabled = enabled;
    }

    public TreeNode(Path path, List<TreeNode> children, boolean enabled, boolean selected) {
        this.path = path;
        this.children = children;
        this.enabled = enabled;
        this.selected = selected;
    }

    public TreeNode() {
        this.path = null;
        this.children = null;
        this.enabled = false;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TreeNode)) return false;
        TreeNode treeNode = (TreeNode) o;
        return isEnabled() == treeNode.isEnabled() && isSelected() == treeNode.isSelected() && Objects.equals(getPath(), treeNode.getPath()) && Objects.equals(getChildren(), treeNode.getChildren());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getChildren(), isEnabled(), isSelected());
    }
}
