package userInterface;


import logic.DataManager;
import logic.DatasetBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicalAnalyticalModule extends JFrame {

    private Container container;
    private JComboBox<String> matrixComboBox;
    private JComboBox<String> propertyComboBox;
    private String selectedMatrix = "";
    private JComponent resultPanel = null;
    private JTable resultTable = null;
    private JList<String> queryList;

    public GraphicalAnalyticalModule(){
        setTitle("Графически-аналитический модуль");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        container = getContentPane();

        JLabel matrixLabel = new JLabel("Выбор матрицы");
        matrixComboBox = new JComboBox<>(DataManager.getMatrixKinds());
        selectedMatrix = (String) matrixComboBox.getSelectedItem();
        JLabel propertyLabel = new JLabel("Выбор свойства");
        propertyComboBox = new JComboBox<>(DataManager.getPropertiesList(selectedMatrix));
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
                if(resultPanel != null){
                    container.remove(resultPanel);
                }
                JFreeChart chart = UIBuilder.createBarChartExample(DatasetBuilder.createCategoryDatasetExample());
                resultPanel = new ChartPanel(chart);
                ((ChartPanel) resultPanel).setMouseWheelEnabled(true);
                ((ChartPanel) resultPanel).setFillZoomRectangle(true);
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
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
                    propertyComboBox = new JComboBox<>(
                            DataManager.getPropertiesList(matrix)
                    );
                }
            }
        };
        return l;
    }

    private ActionListener chartButtonActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultPanel != null){
                    container.remove(resultPanel);
                }
                String selectedProperty = (String) propertyComboBox.getSelectedItem();
                JFreeChart chart = UIBuilder.createBarChart(
                        DataManager.getDataset(selectedMatrix, selectedProperty),
                        selectedProperty,
                        selectedMatrix
                        );
                resultPanel = new ChartPanel(chart);
                ((ChartPanel) resultPanel).setMouseWheelEnabled(true);
                ((ChartPanel) resultPanel).setFillZoomRectangle(true);
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
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
                resultTable =  DataManager.getResultTable(transformToViewName(queryList.getSelectedValue()));
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                resultPanel = new JScrollPane(resultTable,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
            }
        };
        return l;
    }

    private String transformToViewName(String selectedItem){
        switch (selectedItem){
            case "Свойства керамических нанокомпозитов":{
                return "properties_view";
            }
            case "Статьи по керамическим нанокомпозитам":{
                return "articles";
            }
            case "Свойства кермических нанокомпозитов (старые)":{
                return "properties_view_old";
            }
            default: throw new RuntimeException();
        }
    }

}
