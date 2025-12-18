package unimilk.dirtreeprinter;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirTreeGenerator {

    public static String generateTree(Path root) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        sb.append(root.getFileName()).append("/\n");
        printTree(root, "", sb);
        return sb.toString();
    }

    private static void printTree(Path dir, String prefix, StringBuilder sb) throws IOException {
        if (!Files.isDirectory(dir)) {
            return;
        }

        List<Path> children;
        try (Stream<Path> stream = Files.list(dir)) {
            children = stream
                    .sorted(directoryFirst())
                    .collect(Collectors.toList());
        }

        for (int i = 0; i < children.size(); i++) {
            Path child = children.get(i);
            boolean isLast = (i == children.size() - 1);

            sb.append(prefix)
              .append(isLast ?  "└── " : "├── ")
              .append(child.getFileName())
              .append("\n");

            if (Files.isDirectory(child)) {
                String newPrefix = prefix + (isLast ? "    " : "│   ");
                printTree(child, newPrefix, sb);
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
}
