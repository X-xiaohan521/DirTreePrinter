package unimilk.dirtreeprinter.frontend.settings;

import unimilk.dirtreeprinter.api.settings.ISettings;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ListReorderTransferHandler extends TransferHandler {

    private int fromIndex = -1;
    private final ISettings settingsCopy;

    public ListReorderTransferHandler(ISettings settingsCopy) {
        this.settingsCopy = settingsCopy;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JList<?> list = (JList<?>) c;
        fromIndex = list.getSelectedIndex();
        return new StringSelection(list.getSelectedValue().toString());
    }

    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop();
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!support.isDrop()) return false;

        JList<?> list = (JList<?>) support.getComponent();
        DefaultListModel model = (DefaultListModel) list.getModel();

        JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
        int toIndex = dl.getIndex();

        if (fromIndex == toIndex) return false;

        Object value = model.get(fromIndex);
        model.remove(fromIndex);
        String removedRule = settingsCopy.removeRule(fromIndex);

        if (fromIndex < toIndex) toIndex--;

        model.add(toIndex, value);
        settingsCopy.addRule(removedRule, toIndex);

        list.setSelectedIndex(toIndex);

        return true;
    }
}
