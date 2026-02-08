package unimilk.dirtreeprinter.frontend.settings;

import unimilk.dirtreeprinter.api.settings.ISettings;

import javax.swing.*;
import java.awt.*;

public class GeneralCard extends JPanel {
    private final ISettings settingsCopy;

    protected GeneralCard(ISettings settingsCopy) {
        this.settingsCopy = settingsCopy;
        setLayout(new BorderLayout());

        // mainPanel for sections to flow vertically
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        TreeSettingsSection treeSettingsSection = new TreeSettingsSection();
        treeSettingsSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, treeSettingsSection.getPreferredSize().height));
        mainPanel.add(treeSettingsSection);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private class TreeSettingsSection extends JPanel {
        private final JSpinner defaultExpandedLayerSpinner = new JSpinner(
                new SpinnerNumberModel(0, 0, 100, 1)
        );

        TreeSettingsSection() {
            defaultExpandedLayerSpinner.addChangeListener(e -> onExpandedLayerChange());

            // create header (Title ----)
            JPanel header = new JPanel();
            header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
            JLabel title = new JLabel("Tree Display");
            JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);
            header.add(title);
            header.add(Box.createHorizontalStrut(8));
            header.add(line);

            // create option units
            JPanel options = new JPanel(new FlowLayout(FlowLayout.LEADING));
            options.add(new JLabel("Default Expanded Layers: "));
            options.add(defaultExpandedLayerSpinner);

            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            add(header);
            add(Box.createVerticalStrut(5));
            add(options);
        }

        private void onExpandedLayerChange() {
            int expandedLayer = (Integer) defaultExpandedLayerSpinner.getValue();
            if (expandedLayer >= 0 && expandedLayer <= 100) {
                settingsCopy.setDefaultExpandedLayers(expandedLayer);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid value: 0 ~ 100.",
                        "Invalid Value",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

}
