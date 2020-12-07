package model;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class StatisticsCellRenderer extends JLabel implements TableCellRenderer
{

    public StatisticsCellRenderer()
    {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        super.setForeground((Color)value);
        return this;
    }
}
