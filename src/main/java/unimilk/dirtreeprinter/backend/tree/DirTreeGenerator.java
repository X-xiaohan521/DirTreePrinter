package unimilk.dirtreeprinter.backend.tree;

import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.tree.IDirTreeGenerator;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirTreeGenerator implements IDirTreeGenerator {
    public DirTreeGenerator() {

    }

    @Override
    public TreeNode generateTree(Path root, ISettings settings) throws IOException {
        return generateTreeNode(root, settings);
    }

    private TreeNode generateTreeNode(Path path, ISettings settings) throws IOException {
        List<TreeNode> childrenNodes = new ArrayList<>();
        boolean isEnabled = pathFilter(path, settings);

        if (!Files.isDirectory(path)) {
            return new TreeNode(path, new ArrayList<>(), isEnabled);
        }

        List<Path> childrenPaths;
        try (Stream<Path> stream = Files.list(path)) {
            childrenPaths = stream
                    .sorted(directoryFirst())
                    .collect(Collectors.toList());
        }

        for (Path childPath : childrenPaths) {
            TreeNode childNode = generateTreeNode(childPath, settings);
            childrenNodes.add(childNode);
            if (settings.getFilterMode().equals(FilterMode.WHITELIST)) {
                if (isEnabled || childNode.isEnabled()) {
                    isEnabled = true;
                }
            } else {
                if (!isEnabled) {
                    childNode.setEnabled(false);
                }
            }
        }

        return new TreeNode(path, childrenNodes, isEnabled);
    }

    private Comparator<Path> directoryFirst() {
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

    private boolean pathFilter(Path path, ISettings settings) {
        boolean isRulesContainDir = settings.getRules().contains(path.getFileName().toString());
        if (settings.getFilterMode().equals(FilterMode.BLACKLIST)) {
            return !isRulesContainDir;
        } else {
            return isRulesContainDir;
        }
    }
}
