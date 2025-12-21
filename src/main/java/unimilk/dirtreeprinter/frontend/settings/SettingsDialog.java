package unimilk.dirtreeprinter.frontend.settings;

import unimilk.dirtreeprinter.DirTreeApp;
import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;

import javax.swing.*;
import java.awt.*;

public class SettingsDialog extends JDialog {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private final ISettings settingsCopy;

    protected SettingsDialog(JFrame owner, ISettings settingsCopy) {
        super(owner, "Settings", false);
        setSize(600, 400);
        setLocationRelativeTo(owner);

        this.settingsCopy = settingsCopy;

        // create right cards set
        JPanel rightPanel = new JPanel(new BorderLayout());
        createCardPanel();
        rightPanel.add(cardPanel, BorderLayout.CENTER);

        // create OK, Cancel button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        JButton applyButton = new JButton("Apply");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        applyButton.addActionListener(e -> onApply());
        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        buttonPanel.add(applyButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        rightPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // create left scroll categories
        JList<String> categoryList = createCategoryList();
        JScrollPane leftScroll = new JScrollPane(categoryList);

        // construct the whole dialog with left and right part
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftScroll,
                rightPanel
        );
        splitPane.setDividerLocation(180);
        splitPane.setResizeWeight(0);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);
    }

    private JList<String> createCategoryList() {
        JList<String> categoryList = new JList<>(new String[]{
                SettingsCards.GENERAL,
                SettingsCards.IGNORE_RULES,
                SettingsCards.APPEARANCE,
                SettingsCards.ADVANCED
        });
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setFixedCellHeight(20);
        categoryList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        categoryList.setSelectedIndex(0);   // default selection

        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = categoryList.getSelectedValue();
                cardLayout.show(cardPanel, selected);
            }
        });

        return categoryList;
    }

    private void createCardPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(createGeneralPanel(), SettingsCards.GENERAL);
        cardPanel.add(new IgnoreRulesCard(settingsCopy), SettingsCards.IGNORE_RULES);
        cardPanel.add(createAppearancePanel(), SettingsCards.APPEARANCE);
        cardPanel.add(createAdvancedPanel(), SettingsCards.ADVANCED);

    }

    private JPanel createGeneralPanel() {
        // TODO
        return new JPanel();
    }

    private JPanel createAppearancePanel() {
        // TODO
        return new JPanel();
    }

    private JPanel createAdvancedPanel() {
        // TODO
        return new JPanel();
    }

    public static void openSettingsDialog(JFrame owner) {
        ISettings settingsCopy = DirTreeApp.getSettingsManager().getSettings().copy();
        SettingsDialog settingsDialog = new SettingsDialog(owner, settingsCopy);
        settingsDialog.setVisible(true);
    }

    public void onApply() {
        DirTreeApp.getSettingsManager().applySettingsFrom(settingsCopy);
    }

    public void onOK() {
        DirTreeApp.getSettingsManager().applyAndSaveSettingsFrom(settingsCopy);
        dispose();
    }

    public void onCancel() {
        dispose();
    }

}
