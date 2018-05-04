package logic;

import userInterface.ResultTableColumnModel;
import userInterface.ResultTableModel;
import userInterface.ResultTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.sql.*;

public class DataManager {

    private static final String URL = "jdbc:mysql://localhost:3306/nanobase" +
            "?verifyServerCertificate=false" +
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "";

    public DataManager(){
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public JTable getResultTable(String dBViewName){
        JTable resultTable = null;
        try {
            Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM " + dBViewName);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                resultSet.last();

                int rowCount = resultSet.getRow();
                int columnCount = resultSetMetaData.getColumnCount();
                //resultTable = createResultTable(rowCount, columnCount);
                String[][] resultsData = new String[rowCount][columnCount];
                String[] headers = new String[columnCount];

                int row = -1;
                if(resultSet.first()){
                    row = 0;
                    for (int i = 0; i < columnCount; i++) {
                        headers[i] = resultSetMetaData.getColumnLabel(i + 1);
                        resultsData[row][i] = resultSet.getString(i + 1);
//                        resultTable.getColumn(i).setHeaderValue(resultSet.getMetaData().getColumnLabel(i));
//                        resultTable.setValueAt(resultSet.getString(i + 1), row, i);
                    }
                }
                while (resultSet.next()){
                    row++;
                    for (int i = 0; i < columnCount; i++) {
                        resultsData[row][i] = resultSet.getString(i + 1);
//                        resultTable.setValueAt(resultSet.getString(i + 1), row, i);
                    }
                }
                resultTable = create(resultsData, headers);
                statement.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                connection.close();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(resultTable == null){
            throw new RuntimeException();
        }
        else {
            return resultTable;
        }
    }

//    private JTable createResultTable(int rowCount, int columnCount){
//        JTable resultTable = new JTable(new ResultTableModel(rowCount, columnCount), new ResultTableColumnModel(columnCount));
//        resultTable.setDefaultRenderer(String.class, new ResultTableRenderer());
//        resultTable.setGridColor(Color.BLACK);
//        return resultTable;
//    }

    private JTable create(String[][] data, String[] headers){
        JTable resultTable = new JTable(data, headers);
        resultTable.setGridColor(Color.BLACK);
        return resultTable;
    }

}
