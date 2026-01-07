package unimilk.dirtreeprinter.frontend;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SaveDialog extends JFileChooser {
    public SaveDialog(MainFrontend mainFrontend) {
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new java.util.Date());
        this.setSelectedFile(new File("DirTree-" + mainFrontend.getRootFolder() + "-" + timeStamp + ".txt"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        this.setFileFilter(filter);
        this.setAcceptAllFileFilterUsed(false);
    }

    @Override
    public void approveSelection() {
        File file = getSelectedFile();
        String name = file.getName();

        if (!name.toLowerCase().endsWith(".txt")) {
            file = new File(file.getParentFile(), name + ".txt");
        }

        if (file.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "File already exists. Overwrite?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }

        this.setSelectedFile(file);
        super.approveSelection();
    }
}
