package unimilk.dirtreeprinter.frontend;

import unimilk.dirtreeprinter.api.settings.ISettingsManager;
import unimilk.dirtreeprinter.api.tree.IDirTreeGenerator;
import unimilk.dirtreeprinter.api.tree.ITreeRenderer;
import unimilk.dirtreeprinter.api.tree.TreeNode;
import unimilk.dirtreeprinter.frontend.export.ExportPreviewDialog;
import unimilk.dirtreeprinter.frontend.settings.SettingsDialog;
import unimilk.dirtreeprinter.frontend.tree.TreeDisplay;
import unimilk.dirtreeprinter.frontend.worker.ScanWorker;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;

public class MainFrontend extends JFrame {

    private final ISettingsManager settingsManager;
    private final IDirTreeGenerator dirTreeGenerator;
    private final ITreeRenderer treeRenderer;

    private String rootFolder;
    private TreeNode rootNode;
    private final TreeDisplay treeDisplay;

    private final LoadingOverlayPanel loadingOverlay;

    public MainFrontend(
            ISettingsManager settingsManager,
            IDirTreeGenerator dirTreeGenerator,
            ITreeRenderer treeRenderer) {
        this.settingsManager = settingsManager;
        this.dirTreeGenerator = dirTreeGenerator;
        this.treeRenderer = treeRenderer;

        setTitle("Directory Tree Generator");
        List<Image> icons = List.of(
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon16.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon32.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon48.png"))).getImage(),
                new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/icon64.png"))).getImage()
        );
        setIconImages(icons);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        treeDisplay = new TreeDisplay();
        treeDisplay.setRightClickMenu(new RightClickMenu(settingsManager));

        add(new TopContainer(this), BorderLayout.PAGE_START);
        add(new JScrollPane(treeDisplay), BorderLayout.CENTER);

        loadingOverlay =  new LoadingOverlayPanel("Scanning Folders...");
        loadingOverlay.setVisible(false);
        setGlassPane(loadingOverlay);
    }

    private class TopContainer extends JPanel {

        private final MainFrontend mainFrontend;

        TopContainer(MainFrontend mainFrontend) {
            this.mainFrontend = mainFrontend;
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(new JSeparator(SwingConstants.HORIZONTAL));
            add(createToolBar());
            add(createButtonPanel());
        }

        JToolBar createToolBar() {
            JToolBar toolBar = new JToolBar();
            toolBar.setLayout(new BorderLayout());

            toolBar.add(createFileButton(), BorderLayout.LINE_START);

            return toolBar;
        }

        JPanel createButtonPanel() {
            JButton openFolderButton = new JButton("Open Folder");
            JButton exportButton = new JButton("Export");
            JButton clearButton = new JButton("Clear All");

            openFolderButton.addActionListener(e -> selectFolderToScan());
            exportButton.addActionListener(e -> exportToFile());
            clearButton.addActionListener(e -> clearOutput());

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            panel.add(openFolderButton);
            panel.add(exportButton);
            panel.add(clearButton);
            return panel;
        }

        JButton createFileButton() {
            JButton fileButton = new JButton("File");

            JPopupMenu menu = new JPopupMenu();

            JMenuItem openItem = new JMenuItem("Open Folder");
            JMenuItem saveProjectItem = new JMenuItem("Save Project");
            JMenuItem saveAsItem = new JMenuItem("Save As...");
            JMenuItem exportItem = new JMenuItem("Export");
            JMenuItem settingsItem = new JMenuItem("Settings...");
            JMenuItem exitItem = new JMenuItem("Exit");

            openItem.addActionListener(e -> selectFolderToScan());
            exportItem.addActionListener(e -> exportToFile());
            settingsItem.addActionListener(e -> SettingsDialog.openSettingsDialog(mainFrontend, settingsManager));
            exitItem.addActionListener(e -> System.exit(0));

            menu.add(openItem);
            menu.addSeparator();
            menu.add(saveProjectItem);
            menu.add(saveAsItem);
            menu.addSeparator();
            menu.add(exportItem);
            menu.addSeparator();
            menu.add(settingsItem);
            menu.addSeparator();
            menu.add(exitItem);

            fileButton.addActionListener(e -> menu.show(fileButton, 0, fileButton.getHeight()));

            return fileButton;
        }

    }

    void selectFolderToScan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path dir = chooser.getSelectedFile().toPath();
            rootFolder = dir.getFileName().toString();
            ScanWorker scanWorker = new ScanWorker(
                    dirTreeGenerator,
                    dir,
                    settingsManager.getSettings(),
                    () -> loadingOverlay.setVisible(true),
                    rootNode -> {
                        loadingOverlay.setVisible(false);
                        this.rootNode = rootNode;
                        treeDisplay.generateUiTree(this.rootNode);
                    },
                    ex -> {
                        loadingOverlay.setVisible(false);
                        setGlassPane(new JRootPane());
                        showError(this, ex);
                    });
            scanWorker.execute();
        }
    }

    void exportToFile() {
        if (treeDisplay.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please first choose a folder to scan.",
                    "Nothing to Export",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        ExportPreviewDialog.showExportPreviewDialog(this, treeRenderer, rootNode, rootFolder);
    }

    void clearOutput() {
        if (treeDisplay.isEmpty()) {
            return;
        }
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Do you wish to clear the output?",
                "Clear All",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            treeDisplay.clear();
        }
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showError(Component parent, Exception ex) {
        JOptionPane.showMessageDialog(
                parent,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}