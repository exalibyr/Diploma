package logic;

import userInterface.ResultTableColumnModel;
import userInterface.ResultTableModel;
import userInterface.ResultTableRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.io.Reader;
import java.sql.*;

public class DataManager {

    private final String URL = "jdbc:mysql://localhost:3306/nanobase" +
            "?verifyServerCertificate=false" +
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    private final String LOGIN = "root";
    private final String PASSWORD = "";

    public void registerMySQLDriver(){
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean validate(String enteredLogin, char[] enteredPassword){
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            if(resultSet.first()){
                if(enteredLogin.equals(resultSet.getString("login"))){
                    StringBuilder enteredPasswordString = new StringBuilder();
                    for (int i = 0; i < enteredPassword.length; i++) {
                        enteredPasswordString.append(enteredPassword[i]);
                        enteredPassword[i] = '\0';
                    }
                    return enteredPasswordString.toString().equals(resultSet.getString("password"));
                }
                else return false;
            }
            else return false;
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public JTable getResultTable(String dBViewName){
        JTable resultTable = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + dBViewName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            resultSet.last();

            int rowCount = resultSet.getRow();
            int columnCount = resultSetMetaData.getColumnCount();
            String[][] resultsData = new String[rowCount][columnCount];
            String[] headers = new String[columnCount];

            int row = -1;
            if(resultSet.first()){
                row = 0;
                for (int i = 0; i < columnCount; i++) {
                    headers[i] = resultSetMetaData.getColumnLabel(i + 1);
                    resultsData[row][i] = resultSet.getString(i + 1);
                }
            }
            while (resultSet.next()){
                row++;
                for (int i = 0; i < columnCount; i++) {
                    resultsData[row][i] = resultSet.getString(i + 1);
                }
            }
            resultTable = new JTable(resultsData, headers);
            resultTable.setGridColor(Color.BLACK);
            statement.close();
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

}
