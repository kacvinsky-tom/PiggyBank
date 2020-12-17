package ui;

import enums.TableType;
import ui.filter.FilterManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class ToolBar extends JToolBar {
    private final AddManager addManager;
    private final DeleteAction deleteAction;
    private final EditManager editManager;
    private final FilterManager filterManager;
    private int selectedTabIndex = 0;

    public ToolBar(JFrame frame, TablesManager tablesManager){
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        MessageDialog messageDialog = new MessageDialog(frame);
        this.addManager = new AddManager(frame, tablesManager, messageDialog);
        this.deleteAction = new DeleteAction(tablesManager, messageDialog);
        this.editManager = new EditManager(frame, tablesManager, messageDialog);
        this.filterManager = new FilterManager(tablesManager, messageDialog);
        setToolBar();
    }

    public void updateSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
        addManager.updateSelectedTabIndex(selectedTabIndex);
        deleteAction.updateSelectedTabIndex(selectedTabIndex);
        editManager.updateSelectedTabIndex(selectedTabIndex);
        filterManager.updateSelectedTabIndex(selectedTabIndex);
    }

    private void setToolBar(){
        this.setOrientation(SwingConstants.HORIZONTAL);
        this.add(addManager);
        this.add(deleteAction);
        this.add(editManager);
        this.add(filterManager.getFilterPanel());
        this.setFloatable(false);
        this.setVisible(true);
    }

    public void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();

        deleteAction.setEnabled(
                selectionModel.getSelectedItemsCount() != 0
                && selectedTabIndex != TableType.STATISTICS_TABLE.ordinal()
        );

        editManager.setEnabled(
                selectionModel.getSelectedItemsCount() == 1
                && selectedTabIndex != TableType.STATISTICS_TABLE.ordinal()
        );
    }
}
