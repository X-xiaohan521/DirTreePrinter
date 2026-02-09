package unimilk.dirtreeprinter.frontend.worker;

import unimilk.dirtreeprinter.api.settings.ISettings;
import unimilk.dirtreeprinter.api.tree.IDirTreeGenerator;
import unimilk.dirtreeprinter.api.tree.TreeNode;

import javax.swing.*;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ScanWorker extends SwingWorker<TreeNode, Void> {

    private final IDirTreeGenerator dirTreeGenerator;
    private final Path rootPath;
    private final ISettings settings;

    private final Runnable onStart;
    private final Consumer<TreeNode> onSuccess;
    private final Consumer<Exception> onError;

    public ScanWorker(
            IDirTreeGenerator dirTreeGenerator,
            Path rootPath,
            ISettings settings,
            Runnable onStart,
            Consumer<TreeNode> onSuccess,
            Consumer<Exception> onError
    ) {
        this.dirTreeGenerator = dirTreeGenerator;
        this.rootPath = rootPath;
        this.settings = settings;

        this.onStart = onStart;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    protected TreeNode doInBackground() throws Exception {
        onStart.run();
        return dirTreeGenerator.generateTree(rootPath, settings);
    }

    @Override
    protected void done() {
        try {
            TreeNode rootNode = get();
            onSuccess.accept(rootNode);
        } catch (Exception e) {
            onError.accept(e);
        }
    }
}
