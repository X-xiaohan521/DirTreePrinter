package unimilk.dirtreeprinter.frontend;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.stream.Stream;

public class SettingsDialog extends JDialog {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    protected SettingsDialog(JFrame owner) {
        super(owner, "Settings", false);
        setSize(600, 400);
        setLocationRelativeTo(owner);

        // create right cards set
        createCardPanel();

        // create left scroll categories
        JList<String> categoryList = createCategoryList();
        JScrollPane leftScroll = new JScrollPane(categoryList);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftScroll,
                cardPanel
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
        cardPanel.add(createIgnoreRulesPanel(), SettingsCards.IGNORE_RULES);
        cardPanel.add(createAppearancePanel(), SettingsCards.APPEARANCE);
        cardPanel.add(createAdvancedPanel(), SettingsCards.ADVANCED);

    }

    private JPanel createGeneralPanel() {
        // TODO
        return new JPanel();
    }

    private JPanel createIgnoreRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // mode selection
        JRadioButton blacklistButton = new JRadioButton("Blacklist", true);
        JRadioButton whitelistButton = new JRadioButton("Whitelist");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(blacklistButton);
        buttonGroup.add(whitelistButton);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
        top.add(blacklistButton);
        top.add(whitelistButton);
        panel.add(top, BorderLayout.PAGE_START);

        // rules list
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> ruleList = new JList<>(model);
        panel.add(new JScrollPane(ruleList), BorderLayout.CENTER);

        // add/remove buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        bottom.add(new JButton("Add"));
        bottom.add(new JButton("Remove"));
        panel.add(bottom, BorderLayout.PAGE_END);

        return panel;
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
        SettingsDialog settingsDialog = new SettingsDialog(owner);
        settingsDialog.setVisible(true);
    }
}
