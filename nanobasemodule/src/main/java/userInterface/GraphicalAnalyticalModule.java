package userInterface;


import logic.Converter;
import logic.DataManager;
import logic.DatasetBuilder;
import logic.Properties;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicalAnalyticalModule extends JFrame {

    private static final Color BACKGROUND = new Color(114, 209, 255, 50);

    private Container container;
    private JComboBox<String> matrixComboBox;
    private JComboBox<String> propertyComboBox;
    private JPanel propertyComboBoxPanel;
    private String selectedMatrix = "";
    private JList<String> queryList;
    private ChartFrame chartFrame = null;
    private ChartPanel chartPanel = null;
    private ResultTableFrame resultTableFrame = null;
    private JScrollPane resultTablePanel = null;
    private Properties propertiesCache;

    public GraphicalAnalyticalModule(){
        setTitle("Аналитический модуль");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container = getContentPane();
        JLabel matrixLabel = new JLabel("Выбор матрицы");
        JLabel propertyLabel = new JLabel("Выбор свойства");
        matrixComboBox = new JComboBox<>(DataManager.getMatrixKinds());
        selectedMatrix = (String) matrixComboBox.getSelectedItem();
        propertiesCache = DataManager.getPropertiesList(selectedMatrix);
        propertyComboBoxPanel = new JPanel();
        propertyComboBoxPanel.setBackground(BACKGROUND);
        propertyComboBox = new JComboBox<>(propertiesCache.getPropertiesRus());
        propertyComboBoxPanel.add(propertyComboBox);
        JButton drawBarChartButton = new JButton("Построить гистограмму");
//        JButton drawExampleChartButton = new JButton("Пример диаграммы");
        JLabel dataLabel = new JLabel("Данные по нанокомпозитам");
        queryList = UIBuilder.buildQueryList();
        JButton getResultButton = new JButton("Выполнить запрос");

        container.setLayout(new VerticalLayout());
        container.setBackground(BACKGROUND);
//        container.add(drawExampleChartButton);
        container.add(matrixLabel);
        container.add(matrixComboBox);
        container.add(propertyLabel);
        container.add(propertyComboBoxPanel);
        container.add(drawBarChartButton);
        container.add(dataLabel);
        container.add(queryList);
        container.add(getResultButton);
//        container.add(graphicalAnalisysPanel);

        matrixComboBox.addActionListener(matrixComboBoxActionListener());
        drawBarChartButton.addActionListener(chartButtonActionListener());
        getResultButton.addActionListener(getResultButtonActionListener());
//        drawExampleChartButton.addActionListener(drawExampleBarChartActionListener());

        locateWindow();
        setVisible(true);
        pack();
    }

//    private ActionListener drawExampleBarChartActionListener(){
//        ActionListener l = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(chartFrame == null){
//                    drawExampleBarChart();
//                    chartFrame = new ChartFrame(chartPanel, "Название свойства");
//                }
//                else {
//                    if(chartFrame.isValid()){
//                        drawExampleBarChart();
//                        chartFrame.update(chartPanel, "Название свойства");
//                    }
//                    else {
//                        drawExampleBarChart();
//                        chartFrame = new ChartFrame(chartPanel, "Название свойства");
//                    }
//                }
//            }
//        };
//        return l;
//    }

    private ActionListener matrixComboBoxActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matrix = (String) matrixComboBox.getSelectedItem();
                if(!matrix.equals(selectedMatrix)){
                    propertyComboBox.removeAllItems();
                    propertyComboBoxPanel.remove(propertyComboBox);
                    propertiesCache = DataManager.getPropertiesList(matrix);
                    propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                    propertyComboBoxPanel.add(propertyComboBox);
//                    propertyComboBox = new JComboBox<>(propertiesCache.getPropertiesRus());
                    selectedMatrix = matrix;

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
                    chartFrame = new ChartFrame(chartPanel, (String) propertyComboBox.getSelectedItem());
                }
                else {
                    if(chartFrame.isValid()){
                        drawBarChart();
                        chartFrame.update(chartPanel, (String) propertyComboBox.getSelectedItem());
                    }
                    else {
                        drawBarChart();
                        chartFrame = new ChartFrame(chartPanel, (String) propertyComboBox.getSelectedItem());
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
                if(queryList.getSelectedIndex() < 0){
                    return;
                }
                else {
                    if(resultTablePanel == null) {
                        drawResultTable();
                        resultTableFrame = new ResultTableFrame(resultTablePanel, queryList.getSelectedValue());
                    }
                    else {
                        if(resultTablePanel.isValid()){
                            drawResultTable();
                            resultTableFrame.update(resultTablePanel, queryList.getSelectedValue());
                        }
                        else {
                            drawResultTable();
                            resultTableFrame = new ResultTableFrame(resultTablePanel, queryList.getSelectedValue());
                        }
                    }
                }
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
    }

//    private void drawExampleBarChart(){
//        JFreeChart chart = UIBuilder.createBarChartExample(DatasetBuilder.createCategoryDatasetExample());
//        chartPanel = new ChartPanel(chart);
//        chartPanel.setFillZoomRectangle(true);
//        chartPanel.setVerticalAxisTrace(true);
//    }

    private void drawResultTable(){
        JTable resultTable =  DataManager.getResultTable(Converter.convertToViewName(queryList.getSelectedValue()));
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        RowSorter<TableModel> resultTableSorter = new TableRowSorter<>(resultTable.getModel());
        resultTable.setRowSorter(resultTableSorter);
        resultTablePanel = new JScrollPane(resultTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getPreferredSize();
        Point point = new Point();
        point.x = (screenSize.width - windowSize.width) / 2;
        point.y = (screenSize.height - windowSize.height) / 2 - 50;
        setLocation(point);
        setResizable(false);
    }
}
