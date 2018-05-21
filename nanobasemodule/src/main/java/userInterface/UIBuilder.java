package userInterface;

import logic.DataManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.util.Vector;

public class UIBuilder {

    static JPanel createPropertiesPanel(JComboBox<String> matrixComboBox,
                                        JComboBox<String> propertiesComboBox,
                                        JButton drawBarChartButton){
        JLabel matrixLabel = new JLabel("Выбор матрицы");
        JLabel propertyLabel = new JLabel("Выбор свойства");
        JPanel panel = new JPanel();
        panel.setBackground(new Color(114, 209, 255, 50));
        panel.setBorder(BorderFactory.createTitledBorder("Графический анализ свойств"));
        panel.add(matrixLabel);
        panel.add(matrixComboBox);
        panel.add(propertyLabel);
        panel.add(propertiesComboBox);
        panel.add(drawBarChartButton);
        return panel;
    }

    static JList<String> buildQueryList(){
        Vector<String> queryListContent = new Vector<>();
        queryListContent.add("Статьи по керамическим нанокомпозитам");
        queryListContent.add("Свойства керамических нанокомпозитов");
//        queryListContent.add("Свойства кермических нанокомпозитов (старые)");
        queryListContent.add("Область применения керамических нанокомпозитов");
        queryListContent.add("Способы получения керамических нанокомпозитов");
        JList<String> queryList = new JList<>(queryListContent);
        queryList.setAutoscrolls(true);
        return queryList;
    }

    static JFreeChart createBarChart(CategoryDataset dataset, String propertyName, String matrixName){
        JFreeChart chart = ChartFactory.createBarChart("Гистограмма значений свойства - " + propertyName,
                "Название нанокомпозита",
                propertyName,
                dataset);
        chart.addSubtitle(new TextTitle("Матрица - " + matrixName));
        chart.setBackgroundPaint(Color.GRAY);
        chart.setPadding(new RectangleInsets(2, 2 , 4, 4));
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        //barRenderer.setDrawBarOutline(true);
        //chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }

//    static JFreeChart createBarChartExample(CategoryDataset dataset){
//        JFreeChart chart = ChartFactory.createBarChart("График значений свойства",
//                "Название нанокомпозита",
//                "Значение свойства, Ед. изм.",
//                dataset);
//        chart.addSubtitle(new TextTitle("Название матрицы"));
//        chart.setBackgroundPaint(Color.GRAY);
//        chart.setPadding(new RectangleInsets(2, 2 , 4, 4));
//        CategoryPlot plot = chart.getCategoryPlot();
//        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
////        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
//        //barRenderer.setDrawBarOutline(true);
//        //chart.getLegend().setFrame(BlockBorder.NONE);
//        return chart;
//    }

    public static JTable createResultTable(String[][] resultData, String[] headers){
        JTable resultTable = new JTable(resultData, headers);
        resultTable.setGridColor(Color.BLACK);
        return resultTable;
    }

}
