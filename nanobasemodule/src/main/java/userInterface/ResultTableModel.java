package userInterface;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ResultTableModel extends DefaultTableModel implements TableModel {

    private String[][] data;

    public ResultTableModel(int rowCount, int columnCount){
        super();
        data = new String[rowCount][columnCount];
        setColumnCount(columnCount);
        setRowCount(rowCount);
    }


    public void setValue(int rowIndex, int columnIndex, String value){
        data[rowIndex][columnIndex] = value;
    }

    @Override
    public String getValueAt(int row, int column) {
        return data[row][column];
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        data[row][column] = (String) aValue;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public void setData(String[][] data) {
        this.data = data;
    }


}
