package unimilk.dirtreeprinter.backend.tree;

import unimilk.dirtreeprinter.DirTreeApp;
import unimilk.dirtreeprinter.api.settings.FilterMode;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirTreeGenerator {

    public static String generateTree(Path root) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(root.getFileName()).append("/\n");

        TreeNode rootNode = generateTreeNode(root);
        buildTree(rootNode, "", sb);

        return sb.toString();
    }

    private static TreeNode generateTreeNode(Path dir) throws IOException{
        List<TreeNode> childrenNodes = new ArrayList<>();
        boolean isEnabled = dirFilter(dir);

        if (!Files.isDirectory(dir)) {
            return new TreeNode(dir, new ArrayList<>(), isEnabled);
        }

        List<Path> childrenPaths;
        try (Stream<Path> stream = Files.list(dir)) {
            childrenPaths = stream
                    .sorted(directoryFirst())
                    .collect(Collectors.toList());
        }

        for (Path childPath : childrenPaths) {
            TreeNode childNode = generateTreeNode(childPath);
            childrenNodes.add(childNode);
            if (DirTreeApp.getSettingsManager().getSettings().getFilterMode().equals(FilterMode.WHITELIST)) {
                if (isEnabled || childNode.isEnabled()) {
                    isEnabled = true;
                }
            }
        }

        return new TreeNode(dir, childrenNodes, isEnabled);
    }

    private static void buildTree(TreeNode node, String prefix, StringBuilder sb) throws IOException {
        if (node.getChildren().isEmpty()) {
            return;
        }

        List<TreeNode> enabledChildren = node.getChildren()
                .stream()
                .filter(TreeNode::isEnabled)
                .collect(Collectors.toList());

        for (int i = 0; i < enabledChildren.size(); i++) {
            TreeNode child = enabledChildren.get(i);
            boolean isLast = (i == enabledChildren.size() - 1);

            sb.append(prefix)
              .append(isLast ?  "└── " : "├── ")
              .append(child.getPath().getFileName())
              .append("\n");

            if (!child.getChildren().isEmpty()) {
                String newPrefix = prefix + (isLast ? "    " : "│   ");
                buildTree(child, newPrefix, sb);
            }
        }
    }

    private static Comparator<Path> directoryFirst() {
        return (a, b) -> {
            try {
                boolean ad = Files.isDirectory(a);
                boolean bd = Files.isDirectory(b);
                if (ad != bd) return ad ? -1 : 1;
            } catch (Exception ignored) {}
            return a.getFileName().toString()
                    .compareToIgnoreCase(b.getFileName().toString());
        };
    }

    private static Boolean dirFilter(Path dir) {
        boolean isRulesContainDir = DirTreeApp.getSettingsManager().getSettings().getRules().contains(dir.getFileName().toString());
        if (DirTreeApp.getSettingsManager().getSettings().getFilterMode().equals(FilterMode.BLACKLIST)) {
            return !isRulesContainDir;
        } else {
            return isRulesContainDir;
        }
    }
}
