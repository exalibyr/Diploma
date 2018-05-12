package userInterface;


import logic.Converter;
import logic.DataManager;
import logic.DatasetBuilder;
import logic.Properties;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class GraphicalAnalyticalModule extends JFrame {

    private Container container;
    private JComboBox<String> matrixComboBox;
    private JComboBox<String> propertyComboBox;
    private String selectedMatrix = "";
    private JComponent resultPanel = null;
    private JTable resultTable = null;
    private RowSorter<TableModel> resultTableSorter = null;
    private JList<String> queryList;
    private ChartFrame chartFrame = null;
    private ChartPanel chartPanel = null;

    private Properties propertiesCache;

    public GraphicalAnalyticalModule(){
        setTitle("Графически-аналитический модуль");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        container = getContentPane();

        JLabel matrixLabel = new JLabel("Выбор матрицы");
        matrixComboBox = new JComboBox<>(DataManager.getMatrixKinds());
        selectedMatrix = (String) matrixComboBox.getSelectedItem();
        JLabel propertyLabel = new JLabel("Выбор свойства");
        propertiesCache = DataManager.getPropertiesList(selectedMatrix);
        propertyComboBox = new JComboBox<>(propertiesCache.getPropertiesRus());
        JButton drawBarChartButton = new JButton("Построить диаграмму");
        JButton drawExampleChartButton = new JButton("Пример диаграммы");
        JLabel dataLabel = new JLabel("Данные по нанокомпозитам");
        queryList = UIBuilder.buildQueryList();
        JButton getResultButton = new JButton("Получить данные");
        JLabel compositeShapeLabelQuestion = new JLabel("Кристаллическая структура: ");
        JLabel compositeShapeLabelAnswer = new JLabel();
        JLabel delimiter = new JLabel("__________________________");
        JLabel obtainingMethodLabelQuestion = new JLabel("Способ получения:");
        JLabel obtainingMethodLabelAnswer = new JLabel("");


        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new VerticalLayout());
        controlPanel.add(matrixLabel);
        controlPanel.add(matrixComboBox);
        controlPanel.add(propertyLabel);
        controlPanel.add(propertyComboBox);
        controlPanel.add(drawBarChartButton);
        controlPanel.add(drawExampleChartButton);
        controlPanel.add(dataLabel);
        controlPanel.add(queryList);
        controlPanel.add(getResultButton);
        controlPanel.add(compositeShapeLabelQuestion);
        controlPanel.add(compositeShapeLabelAnswer);
        controlPanel.add(delimiter);
        controlPanel.add(obtainingMethodLabelQuestion);
        controlPanel.add(obtainingMethodLabelAnswer);
        container.add(BorderLayout.WEST, controlPanel);

        matrixComboBox.addActionListener(matrixComboBoxActionListener());
        drawBarChartButton.addActionListener(chartButtonActionListener());
        getResultButton.addActionListener(getResultButtonActionListener());
        drawExampleChartButton.addActionListener(drawExampleBarChartActionListener());

        setVisible(true);
        validate();
        pack();
    }

    private ActionListener drawExampleBarChartActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chartFrame == null){
                    drawExampleBarChart();
                    chartFrame = new ChartFrame(chartPanel);
                }
                else {
                    if(chartFrame.isValid()){
                        drawExampleBarChart();
                        chartFrame.update(chartPanel);
                    }
                    else {
                        drawExampleBarChart();
                        chartFrame = new ChartFrame(chartPanel);
                    }
                }
            }
        };
        return l;
    }

    private ActionListener matrixComboBoxActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matrix = (String) matrixComboBox.getSelectedItem();
                if(!matrix.equals(selectedMatrix)){
                    propertyComboBox.removeAllItems();
                    propertiesCache = DataManager.getPropertiesList(matrix);
                    propertyComboBox = new JComboBox<>(propertiesCache.getPropertiesRus());
                }
            }
        };
        return l;
    }

    private ActionListener chartButtonActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chartFrame == null){
                    drawBarChart();
                    chartFrame = new ChartFrame(chartPanel);
                }
                else {
                    if(chartFrame.isValid()){
                        drawBarChart();
                        chartFrame.update(chartPanel);
                    }
                    else {
                        drawBarChart();
                        chartFrame = new ChartFrame(chartPanel);
                    }
                }
            }
        };
        return l;
    }

    private ActionListener getResultButtonActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultPanel != null) {
                    container.remove(resultPanel);
                }
                resultTable =  DataManager.getResultTable(Converter.convertToViewName(queryList.getSelectedValue()));
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                resultTableSorter = new TableRowSorter<>(resultTable.getModel());
                resultTable.setRowSorter(resultTableSorter);
                resultPanel = new JScrollPane(resultTable,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
            }
        };
        return l;
    }

    private void drawBarChart(){
        String selectedPropertyEng = propertiesCache.getPropertiesEng().get(propertyComboBox.getSelectedIndex());
        String selectedPropertyRus = (String) propertyComboBox.getSelectedItem();
        JFreeChart chart = UIBuilder.createBarChart(
                DataManager.getDataset(selectedMatrix, selectedPropertyEng),
                selectedPropertyRus,
                selectedMatrix
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setVerticalAxisTrace(true);
    }

    private void drawExampleBarChart(){
        JFreeChart chart = UIBuilder.createBarChartExample(DatasetBuilder.createCategoryDatasetExample());
        chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setVerticalAxisTrace(true);
    }
}
