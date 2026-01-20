package unimilk.dirtreeprinter.frontend;

import javax.swing.*;
import java.awt.*;

public class LoadingOverlayPanel extends JPanel {

    private final JProgressBar progressBar;
    private final JLabel label;

    public LoadingOverlayPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        label = new JLabel("Scanning folders...");
        label.setForeground(Color.WHITE);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(220, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        add(label, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(progressBar, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
