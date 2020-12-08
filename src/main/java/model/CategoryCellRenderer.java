package model;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CategoryCellRenderer extends JLabel implements TableCellRenderer
{

    public CategoryCellRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        super.setBackground((Color)value);
        return this;
    }
}
