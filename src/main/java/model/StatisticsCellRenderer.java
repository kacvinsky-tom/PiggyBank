package model;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class StatisticsCellRenderer extends JLabel implements TableCellRenderer     // TODO REPAIR TO SHOW COLORS IN STATISTICS
{

    public StatisticsCellRenderer()
    {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        return this;
    }
}
