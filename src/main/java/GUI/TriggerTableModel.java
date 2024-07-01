package GUI;

import model.Employee;
import model.Trigger;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TriggerTableModel extends AbstractTableModel
{
    private final String[] columnNames = {"ID", "Bezeichnung", "Intervall","Intervall-Option","Uhrzeit"};
    private Trigger[] triggers;

    public TriggerTableModel(Trigger[] triggers) {
        this.triggers = triggers;
    }

    @Override
    public int getRowCount() {
        return triggers.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Trigger getTriggerAt(int row)
    {
        return triggers[row];
    }

    public void updateTriggersTable(Trigger[] triggers)
    {
        this.triggers = triggers;
        fireTableDataChanged();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Trigger trigger = triggers[rowIndex];
        switch(columnIndex)
        {
            case 0:
                return trigger.getId();
            case 1:
                return trigger.getScheduleName();
            case 2:
                return trigger.getScheduleInterval();
            case 3:
                return trigger.getScheduleIntervalOption();
            case 4:
                return trigger.getGetScheduleTime();
            default:
                return null;
        }
    }

    public void adjustColumnWidths(JTable table)
    {
        final TableModel model = table.getModel();

        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            tableColumn.setCellRenderer(new CenteredTableCellRenderer());

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We überschreiten nicht die Maximalbreite
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private class CenteredTableCellRenderer extends DefaultTableCellRenderer
    {
        public CenteredTableCellRenderer()
        {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

}
