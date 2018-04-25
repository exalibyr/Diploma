package logic;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

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

    public static void registerMySQLDriver(){
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean validate(String enteredLogin, char[] enteredPassword){
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            if(resultSet.first()){
                if(enteredLogin.equals(resultSet.getString("login"))){
                    enteredLogin = "";
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

    public static JTable getResultTable(String dBViewName){
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

    public static String getCompositeShapeAndObtainingMethodData(){
        return "ghdgh";
    }

    public static CategoryDataset getDataset(String matrixName, String propertyName){
        final String DELIMITER = ";";
        DefaultCategoryDataset dataset = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT FILL_NAME, ANSWER_TEXT, ANSWER_NAME\n" +
                            "FROM target_properties\n" +
                            "WHERE MATRIX_NAME = '" + matrixName + "'\n" +
                            "AND QUESTION_NAME_ENG = '" + propertyName + "'");
            if(resultSet.first()){
                String value, measure, fillName;
                dataset = new DefaultCategoryDataset();
                do{
                    fillName = resultSet.getString(1);
                    value = resultSet.getString(2);
                    measure = resultSet.getString(3);
                    if(value.contains(DELIMITER) && measure.contains(DELIMITER)){
                        String[] values = value.split(DELIMITER);
                        String[] conditions = measure.split(DELIMITER);
                        for (int i = 0; i < values.length; i++) {
                            dataset.addValue(Double.parseDouble(values[i]),
                                    conditions[0] + ", " + conditions[i + 1],
                                    fillName);
                        }
                    }
                    else {
                        dataset.addValue(Double.parseDouble(value), measure, fillName);
                    }
                }while (resultSet.next());
            }

            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(dataset == null){
            throw new RuntimeException();
        }
        else {
            return dataset;
        }
    }

    public static Vector<String> getMatrixKinds(){
        Vector<String> matrixKinds = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM matrix_kinds");
            if(resultSet.first()){
            boolean sdf = resultSet.isFirst();
                matrixKinds = new Vector<>();
                do{
                    matrixKinds.add(resultSet.getString(1));
                }while (resultSet.next());
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(matrixKinds == null){
            throw new RuntimeException();
        }
        else {
            return matrixKinds;
        }
    }

    public static Vector<String> getPropertiesList(String matrixName){
        Vector<String> properties = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("CALL update_questions('" + matrixName + "')");
            if(resultSet.first()){
                properties = new Vector<>();
                do{
                    properties.add(resultSet.getString("QUESTION_NAME_ENG"));
                }while (resultSet.next());
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        if(properties == null){
            throw new RuntimeException();
        }
        else {
            return properties;
        }
    }

}
