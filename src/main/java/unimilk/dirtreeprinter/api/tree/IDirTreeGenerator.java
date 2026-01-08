package unimilk.dirtreeprinter.api.tree;

import unimilk.dirtreeprinter.api.settings.ISettings;

import java.io.IOException;
import java.nio.file.Path;

public interface IDirTreeGenerator {
    String generateTree(Path root, ISettings settings) throws IOException;
}
