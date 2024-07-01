package GUI;

import model.Employee;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

public class EmployeeTableModel extends AbstractTableModel
{
    private final String[] columnNames = {"M.-Nr.", "Vorname", "Nachname", "E-Mail", "Abteilung", "Trigger", "Status"};
    private Employee[] employees;

    public EmployeeTableModel(Employee[] employees)
    {
        this.employees = employees;
    }

    @Override
    public int getRowCount() {
        return employees.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Employee employee = employees[rowIndex];
        switch (columnIndex)
        {
            case 0:
                return employee.getId();
            case 1:
                return employee.getName();
            case 2:
                return employee.getSurname();
            case 3:
                return employee.getEmail();
            case 4:
                return employee.getDepartment();
            case 5:
                return employee.getCategory();
            case 6:
                return employee.getState();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void updateEmployeesTable(Employee[] employees)
    {
        this.employees = employees;
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int row)
    {
        return employees[row];
    }

    public void adjustColumnWidths(JTable table)
    {
        final TableModel model = table.getModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

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
}
