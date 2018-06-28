package userInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;

//класс-билдер, содержащий статические методы для отрисовки интерфейса
public class UIBuilder {

    //статический методы
//    static JPanel createPropertiesPanel(JComboBox<String> matrixComboBox,
//                                        JComboBox<String> propertiesComboBox,
//                                        JButton drawBarChartButton){
//        JLabel matrixLabel = new JLabel("Выбор матрицы");
//        JLabel propertyLabel = new JLabel("Выбор свойства");
//        JPanel panel = new JPanel();
//        panel.setBackground(new Color(114, 209, 255, 50));
//        panel.setBorder(BorderFactory.createTitledBorder("Графический анализ свойств"));
//        panel.add(matrixLabel);
//        panel.add(matrixComboBox);
//        panel.add(propertyLabel);
//        panel.add(propertiesComboBox);
//        panel.add(drawBarChartButton);
//        return panel;
//    }

    //статический метод, конструирующий объект списка запросов
    //для построения таблиц с данными
    static JList<String> buildQueryList(){
        Vector<String> queryListContent = new Vector<>();
        queryListContent.add("Статьи по керамическим нанокомпозитам");
        queryListContent.add("Свойства керамических нанокомпозитов");
        queryListContent.add("Область применения керамических нанокомпозитов");
        queryListContent.add("Способы получения керамических нанокомпозитов");
        JList<String> queryList = new JList<>(queryListContent);
        queryList.setAutoscrolls(true);
        return queryList;
    }

    //статический метод, конструирующий объект гистограммы без анализа по составу композитов
    static JFreeChart createBarChart(CategoryDataset dataset, String propertyName, String matrixName){
        JFreeChart chart = ChartFactory.createBarChart("Гистограмма значений свойства - " + propertyName,
                "Название нанокомпозита",
                propertyName,
                dataset);
        chart.addSubtitle(new TextTitle("Матрица: " + matrixName));
        chart.setBackgroundPaint(Color.GRAY);
        chart.setPadding(new RectangleInsets(2, 2, 4, 4));
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    //статический метод, конструирующий объект гистограммы с анализом по составу композитов
    static JFreeChart createBarChart2(CategoryDataset dataset, String propertyName, String matrixName, String fillName){
        JFreeChart chart = ChartFactory.createBarChart("Гистограмма значений свойства - " + propertyName,
                "% матрицы - % наполнителя (название композита)",
                propertyName,
                dataset);
        chart.addSubtitle(new TextTitle("Матрица: " + matrixName + " | Наполнитель: " + fillName));
        chart.setBackgroundPaint(Color.GRAY);
        chart.setPadding(new RectangleInsets(2, 2 , 4, 4));
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    //статический метод, конструирующий объект таблицы с данными
    public static JTable createResultTable(String[][] resultData, String[] headers){
        JTable resultTable = new JTable(resultData, headers);
        resultTable.setGridColor(Color.BLACK);
        return resultTable;
    }

    public static void onMD5ConvertingException(ValidationWindow validationWindow){
        JOptionPane.showMessageDialog(validationWindow,
                "Ошибка шифрования!",
                "Ошибка шифрования!",
                JOptionPane.ERROR_MESSAGE);
        validationWindow.getPasswordField().setText("");
        validationWindow.getLoginTextField().setText("");
        validationWindow.getLoginTextField().requestFocus();
    }

    //обработчик в случае некорректного логина/пароля
    public static void onValidationFailure(ValidationWindow validationWindow){
        JOptionPane.showMessageDialog(validationWindow,
                "Неправильный логин или пароль!",
                "Ошибка входа!",
                JOptionPane.ERROR_MESSAGE);
        validationWindow.getPasswordField().setText("");
        validationWindow.getLoginTextField().setText("");
        validationWindow.getLoginTextField().requestFocus();
    }

    public static void onConnectionFailure(ValidationWindow validationWindow){
        JOptionPane.showMessageDialog(validationWindow,
                "Ошибка соединения!",
                "Ошибка входа!",
                JOptionPane.ERROR_MESSAGE);
        validationWindow.getPasswordField().setText("");
        validationWindow.getLoginTextField().setText("");
        validationWindow.getLoginTextField().requestFocus();
    }

}
