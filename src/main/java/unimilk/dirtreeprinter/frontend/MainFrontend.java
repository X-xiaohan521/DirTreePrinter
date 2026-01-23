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
            setLayout(new BorderLayout());
            add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.NORTH);
            add(createToolBar(), BorderLayout.CENTER);
            add(createButtonPanel(), BorderLayout.SOUTH);
        }

        JToolBar createToolBar() {
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            toolBar.setRollover(true);

            toolBar.add(createFileButton());
            toolBar.add(createEditButton());

            return toolBar;
        }

        JPanel createButtonPanel() {
            JButton openFolderButton = new JButton("Open Folder");
            JButton exportButton = new JButton("Export");
            JButton clearButton = new JButton("Clear All");
            JButton rescanButton = new JButton("Rescan");

            openFolderButton.addActionListener(e -> selectFolderToScan());
            exportButton.addActionListener(e -> exportToFile());
            clearButton.addActionListener(e -> clearOutput());
            rescanButton.addActionListener(e -> onRescan());

            // left group
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
            leftPanel.add(openFolderButton);
            leftPanel.add(exportButton);
            leftPanel.add(clearButton);

            // right group
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
            rightPanel.add(rescanButton);

            // build the whole row
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(leftPanel, BorderLayout.WEST);
            panel.add(rightPanel, BorderLayout.EAST);

            return panel;
        }


        JButton createFileButton() {
            JButton fileButton = new JButton(" File ");

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

        JButton createEditButton() {
            JButton editButton = new JButton(" Edit ");

            JPopupMenu menu = new JPopupMenu();

            JMenuItem undoItem = new JMenuItem("Undo");
            JMenuItem redoItem = new JMenuItem("Redo");

            menu.add(undoItem);
            menu.add(redoItem);

            editButton.addActionListener(e -> menu.show(editButton, 0, editButton.getHeight()));
            return editButton;
        }
    }

    private void onRescan() {
        if (treeDisplay.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please first choose a folder to scan.",
                    "Nothing to Rescan",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        startScanning(rootNode.getPath());
    }

    void selectFolderToScan() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path dir = chooser.getSelectedFile().toPath();
            startScanning(dir);
        }
    }

    private void startScanning(Path dir) {
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