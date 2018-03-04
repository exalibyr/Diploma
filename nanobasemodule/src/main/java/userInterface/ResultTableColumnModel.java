package userInterface;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class ResultTableColumnModel extends DefaultTableColumnModel {

    public ResultTableColumnModel(int columnCount) {
        for (int i = 0; i < columnCount ; i++) {
            tableColumns.add(new TableColumn(i));
        }
    }
}
