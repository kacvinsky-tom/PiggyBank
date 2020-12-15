package ui;

import model.TableType;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

public class ToolBar extends JToolBar {
    private final AddAction addAction;
    private final DeleteAction deleteAction;
    private final EditAction editAction;
    private final Filter filter;
    private int selectedTabIndex = 0;

    public ToolBar(JFrame frame, TablesManager tablesManager){
        MessageDialog messageDialog = new MessageDialog(frame);
        this.addAction = new AddAction(frame, tablesManager, messageDialog);
        this.deleteAction = new DeleteAction(tablesManager, messageDialog);
        this.editAction = new EditAction(frame, tablesManager, messageDialog);
        setToolBar();
        this.filter = new Filter(this, tablesManager, messageDialog);
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        addAction.setSelectedTabIndex(selectedTabIndex);
        deleteAction.setSelectedTabIndex(selectedTabIndex);
        editAction.setSelectedTabIndex(selectedTabIndex);
        filter.setSelectedTabIndex(selectedTabIndex);
    }

    private void setToolBar(){
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.add(addAction);
        this.add(deleteAction);
        this.add(editAction);
        this.setFloatable(false);
        this.setVisible(true);
    }

    public void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();

        deleteAction.setEnabled(
                selectionModel.getSelectedItemsCount() != 0
                && selectedTabIndex != TableType.STATISTICS_TABLE.ordinal()
        );

        editAction.setEnabled(
                selectionModel.getSelectedItemsCount() == 1
                && selectedTabIndex != TableType.STATISTICS_TABLE.ordinal()
        );
    }
}
