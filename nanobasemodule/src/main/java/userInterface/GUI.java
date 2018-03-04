package userInterface;

import logic.DataManager;
import logic.DatasetBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;


public class GUI extends JFrame {

    private JTable resultTable = null;
    private JComponent resultPanel = null;
    private DataManager dataManager = new DataManager();

    public GUI(){
        setTitle("Модуль анализа свойств");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        final Container container = getContentPane();
//        final GroupLayout mainLayout = new GroupLayout(container);
//        container.setLayout(mainLayout);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new VerticalLayout());

        final JList<String> queryList = GUIBuilder.buildQueryList();
        JButton getResultButton = new JButton("get result");
        JButton drawChartButton = new JButton("draw bar chart");
        actionPanel.add(queryList);
        actionPanel.add(getResultButton);
        actionPanel.add(drawChartButton);
        container.add(BorderLayout.WEST, actionPanel);
//        mainLayout.setVerticalGroup(mainLayout
//                .createParallelGroup()
//                .addComponent(actionPanel)
//        );

        getResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultPanel != null) {
                    container.remove(resultPanel);
                }
                resultTable =  dataManager.getResultTable(queryList.getSelectedValue());
                resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                resultPanel = new JScrollPane(resultTable,
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//                mainLayout.setVerticalGroup(mainLayout
//                        .createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addComponent(resultPanel)
//                );
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
            }
        });

        drawChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultPanel != null){
                    container.remove(resultPanel);
                }
                JFreeChart chart = GUIBuilder.createBarChart(DatasetBuilder.createCategoryDataset());
                resultPanel = new ChartPanel(chart);
                ((ChartPanel) resultPanel).setMouseWheelEnabled(true);
                ((ChartPanel) resultPanel).setFillZoomRectangle(true);
                container.add(BorderLayout.CENTER, resultPanel);
                revalidate();
            }
        });


        setVisible(true);
        validate();
        pack();
    }


}
