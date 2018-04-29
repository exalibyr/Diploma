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
        final String HYPHEN = "-";
        DefaultCategoryDataset dataset = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT name, FILL_NAME, ANSWER_TEXT, ANSWER_NAME\n" +
                            "FROM target_properties\n" +
                            "WHERE MATRIX_NAME = '" + Converter.convertToDatabaseMatrixName(matrixName) + "'\n" +
                            "AND QUESTION_NAME_ENG = '" + propertyName + "'");
            if(resultSet.first()){
                String value, measure, fillName, compositeName;
                dataset = new DefaultCategoryDataset();
                do{
                    compositeName = resultSet.getString(1);
                    fillName = resultSet.getString(2);
                    value = resultSet.getString(3);
                    measure = resultSet.getString(4);
                    if(value.contains(DELIMITER) && measure.contains(DELIMITER)){
                        String[] values = value.split(DELIMITER);
                        String[] conditions = measure.split(DELIMITER);
                        for (int i = 0; i < values.length; i++) {
                            dataset.addValue(Double.parseDouble(values[i]),
                                    conditions[0] + "(" + conditions[i + 1] + ")",
                                    compositeName + '(' + fillName + ')');
                        }
                    }
                    else {
                        if(value.contains(HYPHEN)){
                            String[] values = value.split(HYPHEN);
                            dataset.addValue(Double.parseDouble(values[0]),
                                    measure + "(Мин.)",
                                    compositeName + '(' + fillName + ')');
                            dataset.addValue(Double.parseDouble(values[1]),
                                    measure + "(Макс.)",
                                    compositeName + '(' + fillName + ')');
                        }
                        else {
                            dataset.addValue(Double.parseDouble(value),
                                    measure,
                                    compositeName + '(' + fillName + ')');
                        }
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
                    matrixKinds.add(Converter.convertToLocalMatrixName(resultSet.getString(1)));
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

    public static Properties getPropertiesList(String matrixName){
        Properties properties = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("CALL update_questions('" + Converter.convertToDatabaseMatrixName(matrixName) + "')");
            if(resultSet.first()){
                properties = new Properties();
                do{
                    properties.addPair(
                            resultSet.getString("QUESTION_NAME"),
                            resultSet.getString("QUESTION_NAME_ENG")
                    );
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
