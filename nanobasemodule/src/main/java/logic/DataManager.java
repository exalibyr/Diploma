package logic;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import userInterface.UIBuilder;
import userInterface.ValidationWindow;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Vector;

public class DataManager {

    //URL адресс сервера для СУБД mysql и параметры соединения
    private static final String URL = "jdbc:mysql://localhost:3306/nanobase" +
            "?verifyServerCertificate=false" +
            "&useSSL=false"+
            "&requireSSL=false"+
            "&useLegacyDatetimeCode=false"+
            "&amp"+
            "&serverTimezone=UTC";
    //логин СУБД
    private static final String LOGIN = "root";
    //пароль СУБД
    private static final String PASSWORD = "";

    //метод запускает драйвер для СУБД
    public static void registerMySQLDriver(){
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //метод проверяет введённый при входе логин и пароль на корректность
    public static boolean validate(String enteredLogin, char[] enteredPassword, ValidationWindow validationWindow){
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            resultSet.last();
            int rowCount = resultSet.getRow();
            if(resultSet.first()){
                int row = resultSet.getRow();
                while (row <= rowCount){
                    if(enteredLogin.equals(resultSet.getString("login"))){
                        StringBuilder enteredPasswordString = new StringBuilder();
                        for (int i = 0; i < enteredPassword.length; i++) {
                            enteredPasswordString.append(enteredPassword[i]);
                        }
                        try{
                            if(getMD5Hash(enteredPasswordString.toString()).equals(resultSet.getString("password"))){
                                return true;
                            }
                        }
                        catch (Exception ex){
                            UIBuilder.onMD5ConvertingException(validationWindow);
                            return false;
                        }
                    }
                    resultSet.next();
                    row++;
                }
            }
            UIBuilder.onValidationFailure(validationWindow);
            return false;
        }
        catch (SQLException e){
            UIBuilder.onConnectionFailure(validationWindow);
            return false;
        }
    }

    //метод запрашивает у сервера данные для построения таблиц
    //структурирует полученные данные и передаёт их в метод билдера интерфейса
    public static JTable getResultTable(String dBViewName){
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + dBViewName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            resultSet.last();
            int rowCount = resultSet.getRow();
            if(resultSet.first()){
                int columnCount = resultSetMetaData.getColumnCount();
                String[][] resultsData = new String[rowCount][columnCount];
                String[] headers = new String[columnCount];
                int currentRowIndex = 0;
                for (int i = 0; i < columnCount; i++) {
                    headers[i] = resultSetMetaData.getColumnLabel(i + 1);
                    if(i == 1 || i == 2){
                        resultsData[currentRowIndex][i] = Converter.convertToLocalMatrixName(
                                resultSet.getString(i + 1)
                        );
                    }
                    else {
                        resultsData[currentRowIndex][i] = resultSet.getString(i + 1);
                    }
                }
                while (resultSet.next()){
                    currentRowIndex++;
                    for (int i = 0; i < columnCount; i++) {
                        if(i == 1 || i == 2){
                            resultsData[currentRowIndex][i] = Converter.convertToLocalMatrixName(
                                    resultSet.getString(i + 1)
                            );
                        }
                        else {
                            resultsData[currentRowIndex][i] = resultSet.getString(i + 1);
                        }
                    }
                }
                statement.close();
                //данные передаются в другой метод для построения таблиц
                return UIBuilder.createResultTable(resultsData, headers);
            }
            else {
                statement.close();
                throw new RuntimeException();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    //метод запрашивает у сервера данные для построения гистограмм без учёта состава композитов
    //структурирует полученные данные, при необходимости передавая их в конвертер для преобразования
    //возвращает объект с данными для построения гистрограммы на его основе
    public static CategoryDataset getDataset(String matrixName, String propertyNameEng){
        final String DELIMITER = ";";
        final String HYPHEN = "-";
        DefaultCategoryDataset dataset = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT name, ANSWER_TEXT, ANSWER_NAME\n" +
                            "FROM target_properties\n" +
                            "WHERE MATRIX_NAME = '" + Converter.convertToDatabaseMatrixName(matrixName) + "'\n" +
                            "AND QUESTION_NAME_ENG = '" + propertyNameEng + "'");
            if(resultSet.first()){
                String value, measure, compositeName;
                dataset = new DefaultCategoryDataset();
                do{
                    compositeName = resultSet.getString(1);
                    value = resultSet.getString(2);
                    measure = resultSet.getString(3);
                    if(value.contains(DELIMITER) && measure.contains(DELIMITER)){
                        String[] values = value.split(DELIMITER);
                        String[] conditions = measure.split(DELIMITER);
                        for (int i = 0; i < values.length; i++) {
                            try{
                                dataset.addValue(Double.parseDouble(values[i]),
                                        conditions[0] + "(" + conditions[i + 1] + ")",
                                        compositeName);
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[i])
                                );
                                dataset.addValue(parsableDouble,
                                        conditions[0] + "(" + conditions[i + 1] + ")",
                                        compositeName);
                            }
                        }
                    }
                    else {
                        if(value.contains(HYPHEN)){
                            String[] values = value.split(HYPHEN);
                            try{
                                dataset.addValue(Double.parseDouble(values[0]),
                                        measure + "(Мин.)",
                                        compositeName);
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[0])
                                );
                                dataset.addValue(parsableDouble,
                                        measure + "(Мин.)",
                                        compositeName);
                            }

                            try{
                                dataset.addValue(Double.parseDouble(values[1]),
                                        measure + "(Макс.)",
                                        compositeName);
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[1])
                                );
                                dataset.addValue(parsableDouble,
                                        measure + "(Макс.)",
                                        compositeName);
                            }

                        }
                        else {
                            try{
                                dataset.addValue(Double.parseDouble(value),
                                        measure,
                                        compositeName);
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(value)
                                );
                                dataset.addValue(parsableDouble,
                                        measure,
                                        compositeName);
                            }
                        }
                    }
                }while (resultSet.next());
            }

            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(dataset == null){
            throw new RuntimeException();
        }
        else {
            return dataset;
        }
    }

    //метод запрашивает у сервера данные для построения гистограмм с учётом состава композитов
    //структурирует полученные данные, при необходимости передавая их в конвертер для преобразования
    //возвращает объект с данными для построения гистрограммы на его основе
    public static CategoryDataset getDataset2(String matrixName, String fillName, String propertyNameEng){
        final String DELIMITER = ";";
        final String HYPHEN = "-";
        DefaultCategoryDataset dataset = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT nlc.matrix_fraction, nlc.fill_fraction, nfa.ANSWER_TEXT, nla.ANSWER_NAME, nlc.name\n" +
                            "FROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd," +
                            "nano_l_matrix nlm, nano_l_fill nlf, nano_l_composite nlc\n" +
                            "WHERE nlq.QUESTION_ID = nla.QUESTION_ID\n" +
                            "AND nla.ANSWER_ID = nfa.ANSWER_ID\n" +
                            "AND nla.QUESTION_ID = nfa.QUESTION_ID\n" +
                            "AND nfa.DATA_ID = nfd.DATA_ID\n" +
                            "AND nfd.MATRIX_ID = nlm.MATRIX_ID\n" +
                            "AND nlf.FILL_ID = nfd.FILL_ID\n" +
                            "AND nlc.id = nfd.COMPOSITE_ID\n" +
                            "AND nfd.DATA_ID <> 367\n" +
                            "AND nlm.MATRIX_NAME = \"" + Converter.convertToDatabaseMatrixName(matrixName) + "\"\n" +
                            "AND nlf.FILL_NAME = \"" + Converter.convertToDatabaseMatrixName(fillName) + "\"\n" +
                            "AND nlq.QUESTION_NAME_ENG = \"" + propertyNameEng + "\"");
            if(resultSet.first()){
                String value, measure, fractions, compositeName;
                dataset = new DefaultCategoryDataset();
                do{
                    fractions = resultSet.getString(1) + "% - " + resultSet.getString(2) + "%";
                    value = resultSet.getString(3);
                    measure = resultSet.getString(4);
                    compositeName = resultSet.getString(5);
                    if(value.contains(DELIMITER) && measure.contains(DELIMITER)){
                        String[] values = value.split(DELIMITER);
                        String[] conditions = measure.split(DELIMITER);
                        for (int i = 0; i < values.length; i++) {
                            try{
                                dataset.addValue(Double.parseDouble(values[i]),
                                        conditions[0] + "(" + conditions[i + 1] + ")",
                                        fractions + " (" + compositeName + ")");
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[i])
                                );
                                dataset.addValue(parsableDouble,
                                        conditions[0] + "(" + conditions[i + 1] + ")",
                                        fractions + " (" + compositeName + ")");
                            }
                        }
                    }
                    else {
                        if(value.contains(HYPHEN)){
                            String[] values = value.split(HYPHEN);
                            try{
                                dataset.addValue(Double.parseDouble(values[0]),
                                        measure + "(Мин.)",
                                        fractions + " (" + compositeName + ")");
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[0])
                                );
                                dataset.addValue(parsableDouble,
                                        measure,
                                        fractions + " (" + compositeName + ")");
                            }

                            try{
                                dataset.addValue(Double.parseDouble(values[1]),
                                        measure + "(Макс.)",
                                        fractions + " (" + compositeName + ")");
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(values[1])
                                );
                                dataset.addValue(parsableDouble,
                                        measure,
                                        fractions + " (" + compositeName + ")");
                            }

                        }
                        else {
                            try{
                                dataset.addValue(Double.parseDouble(value),
                                        measure,
                                        fractions + " (" + compositeName + ")");
                            }
                            catch (NumberFormatException ex){
                                double parsableDouble = Double.parseDouble(
                                        Converter.convertToDoubleParsable(value)
                                );
                                dataset.addValue(parsableDouble,
                                        measure,
                                        fractions + " (" + compositeName + ")");
                            }
                        }
                    }
                }while (resultSet.next());
            }

            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(dataset == null){
            throw new RuntimeException();
        }
        else {
            return dataset;
        }
    }

    //метод запрашивает у сервера данные для составления списка доступных из БД матриц
    //структурирует данные, обращаясь к конвертеру в процессе для преобразования данных для сервера
    //или в презентабельный вид для приложения.
    // Возвращает вектор матриц для построения списка на его основе
    public static Vector<String> getMatrixKinds(){
        Vector<String> matrixKinds = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM matrix_kinds");
            if(resultSet.first()){
                matrixKinds = new Vector<>();
                do{
                    matrixKinds.add(Converter.convertToLocalMatrixName(resultSet.getString(1)));
                }while (resultSet.next());
            }
            else {
                matrixKinds = new Vector<>();
                matrixKinds.add("нет данных");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(matrixKinds == null){
            throw new RuntimeException();
        }
        else {
            return matrixKinds;
        }
    }

    //метод запрашивает у сервера данные для составления списка доступных из БД наполнителей
    //с учётом выбранной пользователем матрицы в фильтре
    //структурирует данные, обращаясь к конвертеру в процессе для преобразования данных для сервера
    //или в презентабельный вид для приложения.
    // Возвращает вектор наполнителей для построения списка на его основе
    public static Vector<String> getFillKinds(String matrixKind){
        Vector<String> fillKinds = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT DISTINCT nlf.FILL_NAME FROM nano_l_fill nlf, nano_l_matrix nlm," +
                            " nano_f_datum nfd, nano_l_composite nlc \n" +
                    "WHERE nlm.MATRIX_NAME = \"" + Converter.convertToDatabaseMatrixName(matrixKind) + "\"\n" +
                    "AND nlc.matrix_fraction <> \"\" \n" +
                    "AND nlc.fill_fraction <> \"\"\n" +
                    "AND nfd.FILL_ID = nlf.FILL_ID\n" +
                    "AND nfd.MATRIX_ID = nlm.MATRIX_ID\n" +
                    "AND nfd.COMPOSITE_ID = nlc.id");
            if(resultSet.first()){
                fillKinds = new Vector<>();
                do{
                    fillKinds.add(Converter.convertToLocalMatrixName(resultSet.getString(1)));
                }while (resultSet.next());
            }
            else {
                fillKinds = new Vector<>();
                fillKinds.add("нет данных");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(fillKinds == null){
            throw new RuntimeException();
        }
        else {
            return fillKinds;
        }
    }

    //метод запрашивает у сервера данные для составления списка доступных из БД свойств
    //с учётом выбранной пользователем матрицы в фильтре
    //структурирует данные, обращаясь к конвертеру в процессе для преобразования данных для сервера
    //Возвращает объект, хранящий отобранные свойства для отображения их в интерфейсе
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
            else {
                properties = new Properties();
                properties.addPair("нет данных", "no data");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(properties == null){
            throw new RuntimeException();
        }
        else {
            return properties;
        }
    }

    //метод запрашивает у сервера данные для составления списка доступных из БД свойств
    //с учётом выбранных пользователем матрицы и наполнителя в фильтре
    //структурирует данные, обращаясь к конвертеру в процессе для преобразования данных для сервера
    //Возвращает объект, хранящий отобранные свойства для отображения их в интерфейсе
    public static Properties getPropertiesList2(String matrixName, String fillName){
        Properties properties = null;
        try(Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery
                    ("SELECT DISTINCT nlq.QUESTION_NAME, nlq.QUESTION_NAME_ENG \n" +
                            "\tFROM nano_l_questions nlq, nano_l_answers nla, nano_f_answers nfa, nano_f_datum nfd," +
                            " nano_l_matrix nlm, nano_l_fill nlf, nano_l_composite nlc\n" +
                            "\tWHERE nlq.QUESTION_ID = nla.QUESTION_ID \n" +
                            "    AND nla.QUESTION_ID = nfa.QUESTION_ID\n" +
                            "\tAND nla.ANSWER_ID = nfa.ANSWER_ID\n" +
                            "\tAND nfa.DATA_ID = nfd.DATA_ID\n" +
                            "\tAND nfd.MATRIX_ID = nlm.MATRIX_ID\n" +
                            "    AND nfd.FILL_ID = nlf.FILL_ID\n" +
                            "\tAND nlm.MATRIX_NAME = \"" + Converter.convertToDatabaseMatrixName(matrixName) + "\"\n" +
                            "    AND nlf.FILL_NAME = \"" + Converter.convertToDatabaseMatrixName(fillName) + "\"\n" +
                            "    AND nlq.QUESTION_GROUP_ID <> 4\n" +
                            "    AND nlq.QUESTION_GROUP_ID <> 3\n" +
                            "    AND nlq.QUESTION_GROUP_ID <> 15\n" +
                            "    AND nlc.matrix_fraction <> \"\" \n" +
                            "    AND nlc.fill_fraction <> \"\"\n" +
                            "    AND nlc.id = nfd.COMPOSITE_ID");
            if(resultSet.first()){
                properties = new Properties();
                do{
                    properties.addPair(
                            resultSet.getString("QUESTION_NAME"),
                            resultSet.getString("QUESTION_NAME_ENG")
                    );
                }while (resultSet.next());
            }
            else {
                properties = new Properties();
                properties.addPair("нет данных", "no data");
            }
            statement.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(properties == null){
            throw new RuntimeException();
        }
        else {
            return properties;
        }
    }

    private static String getMD5Hash(String str) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest m= MessageDigest.getInstance("MD5");
        m.reset();
        m.update(str.getBytes("utf-8"));
        //получаем MD5-хеш строки без лидирующих нулей
        String s2 = new BigInteger(1,m.digest()).toString(16);
        //дополняем нулями до 32 символов, в случае необходимости
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0, count = 32 - s2.length(); i < count; i++) {
            sb.append("0");
        }
        return sb.append(s2).toString();
    }

}
