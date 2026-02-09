package unimilk.dirtreeprinter.frontend.settings;

import unimilk.dirtreeprinter.api.settings.FilterMode;
import unimilk.dirtreeprinter.api.settings.ISettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class IgnoreRulesCard extends JPanel {

    private final ISettings settingsCopy;
    private final DefaultListModel<String> ruleListModel = new DefaultListModel<>();
    private JList<String> ruleList;
    private final JTextField inlineEditor = new JTextField();

    private int editingIndex = -1;

    protected IgnoreRulesCard(ISettings settingsCopy) {
        this.settingsCopy = settingsCopy;

        setLayout(new BorderLayout());

        // add (top, middle, bottom) parts
        add(createTopFilterModeButtonPanel(), BorderLayout.PAGE_START);
        add(new JScrollPane(createMiddleRuleList()), BorderLayout.CENTER);
        add(createBottomAddRemoveButtonsPanel(), BorderLayout.PAGE_END);
    }

    private JPanel createTopFilterModeButtonPanel() {
        // mode selection buttons
        JRadioButton blacklistButton = new JRadioButton("Blacklist", true);
        JRadioButton whitelistButton = new JRadioButton("Whitelist");

        blacklistButton.addActionListener(e -> this.settingsCopy.setFilterMode(FilterMode.BLACKLIST));
        whitelistButton.addActionListener(e -> this.settingsCopy.setFilterMode(FilterMode.WHITELIST));

        // bind a button group
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(blacklistButton);
        buttonGroup.add(whitelistButton);

        // set button selected status
        if (!settingsCopy.getFilterMode().isBlacklist()) {
            whitelistButton.setSelected(true);
        }

        // build top panel with two buttons as a group
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING));
        top.add(blacklistButton);
        top.add(whitelistButton);

        return top;
    }

    private JList<String> createMiddleRuleList() {
        // create a rules list with its empty model
        ruleList = new JList<>(ruleListModel);
        ruleList.setFixedCellHeight(19);

        // allow drag to reorder
        ruleList.setDragEnabled(true);
        ruleList.setDropMode(DropMode.INSERT);
        ruleList.setTransferHandler(new ListReorderTransferHandler(settingsCopy));

        // add rules to the model
        settingsCopy.getRules().forEach(ruleListModel::addElement);

        return ruleList;
    }

    private JPanel createBottomAddRemoveButtonsPanel() {
        // add/remove buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        addButton.addActionListener(e -> onAddRule());
        removeButton.addActionListener(e -> onRemoveRule());
        bottom.add(addButton);
        bottom.add(removeButton);
        return bottom;
    }

    private void onAddRule() {
        int index = ruleListModel.size();
        ruleListModel.addElement("");   // placeholder
        ruleList.setSelectedIndex(index);
        startEditing(index);
    }

    private void startEditing(int index) {
        Rectangle cellBounds = ruleList.getCellBounds(index, index);
        if (cellBounds == null) return;

        editingIndex = index;

        inlineEditor.setText("");
        inlineEditor.setBounds(cellBounds);
        inlineEditor.selectAll();

        inlineEditor.addActionListener(e -> commitEdit());
        inlineEditor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                commitEdit();
            }
        });

        ruleList.add(inlineEditor);
        ruleList.repaint();

        inlineEditor.requestFocusInWindow();
    }

    private void commitEdit() {
        if (editingIndex < 0) return;

        String inputText = inlineEditor.getText().trim();

        ruleList.remove(inlineEditor);

        if (!inputText.isEmpty()) {
            ruleListModel.set(editingIndex, inputText);
            settingsCopy.addRule(inputText);
        } else {
            ruleListModel.remove(editingIndex);
        }

        editingIndex = -1;
        ruleList.repaint();
    }

    private void onRemoveRule() {
        int index = ruleList.getSelectedIndex();
        if (index == -1) return;

        settingsCopy.removeRule(ruleListModel.get(index));
        ruleListModel.remove(index);
    }
}
