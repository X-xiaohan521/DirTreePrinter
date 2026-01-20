package unimilk.dirtreeprinter.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class LoadingOverlayPanel extends JPanel {

    private final JProgressBar progressBar;
    private final JLabel label;

    public LoadingOverlayPanel(String message) {
        setOpaque(false);
        setLayout(new GridBagLayout());

        // prepare gbc constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        // add message label
        label = new JLabel(message);
        label.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(label, gbc);

        // add progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(220, 18));
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(progressBar, gbc);

        //block mouse events
        addMouseListener(new MouseAdapter() {});
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
