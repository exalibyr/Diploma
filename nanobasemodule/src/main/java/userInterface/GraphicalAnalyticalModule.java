package userInterface;


import logic.Converter;
import logic.DataManager;
import logic.Properties;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//класс, описывающий объект главного меню приложения
//наследуется от стандартного класса окна
public class GraphicalAnalyticalModule extends JFrame {

    //константа фонового цвета меню
    private static final Color BACKGROUND = new Color(114, 209, 255, 50);

    //контейнер, ссылка на объект окна приложения
    private Container container;
    //объекты выпадающих списков-меню для фильтрации по названию
    //матрицы, наполнителя и свойства
    private JComboBox<String> matrixComboBox;
    private JComboBox<String> fillComboBox;
    private JComboBox<String> propertyComboBox;
    //объект переключателя режима построения гистограмм
    private JCheckBox modeSwitch;
    //объекты панелей, которые содержат выпадающие списки-меню
    private JPanel fillComboBoxPanel;
    private JPanel propertyComboBoxPanel;
    //объекты, кеширующие выбранные матрицы и наполнители
    private String selectedMatrix = "";
    private String selectedFill = "";
    //объект списка, в котором содержатся возможные запросы
    //для построения таблиц с данными
    private JList<String> queryList;
    //объект окна гистограммы
    private ChartFrame chartFrame = null;
    //объект панели с гистограммой
    private ChartPanel chartPanel = null;
    //объект окна с таблицой
    private ResultTableFrame resultTableFrame = null;
    //объект панели таблицы с ползунками для прокрутки
    private JScrollPane resultTablePanel = null;
    //объект, хранящий доступные свойства
    private Properties propertiesCache;

    //конструктор объекта окна главного меню приложения
    //создаёт все элементы управления и обработчики событий
    //тут происходит обращения к объектам или методам других классов
    //при запуске главного меню сразу же происходит синхронизация с БД
    //и в меню отображаются все доступные матрицы, наполнители и свойства
    public GraphicalAnalyticalModule(){
        setTitle("Аналитический модуль");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container = getContentPane();
        JLabel matrixLabel = new JLabel("Выбор матрицы");
        JLabel propertyLabel = new JLabel("Выбор свойства");
        JLabel fillLabel = new JLabel("Выбор наполнителя");
        matrixComboBox = new JComboBox<>(DataManager.getMatrixKinds());
        selectedMatrix = (String) matrixComboBox.getSelectedItem();
        fillComboBox = new JComboBox<>(DataManager.getFillKinds(selectedMatrix));
        selectedFill = (String) fillComboBox.getSelectedItem();
        fillComboBoxPanel = new JPanel();
        fillComboBoxPanel.setBackground(BACKGROUND);
        fillComboBoxPanel.add(fillComboBox);
        propertiesCache = DataManager.getPropertiesList(selectedMatrix);
        propertyComboBoxPanel = new JPanel();
        propertyComboBoxPanel.setBackground(BACKGROUND);
        propertyComboBox = new JComboBox<>(propertiesCache.getPropertiesRus());
        propertyComboBoxPanel.add(propertyComboBox);
        modeSwitch = new JCheckBox("Анализ по составу нанокомпозита");
        modeSwitch.setSelected(false);
        fillComboBox.setEnabled(false);
        JButton drawBarChartButton = new JButton("Построить гистограмму");
        JLabel dataLabel = new JLabel("Данные по нанокомпозитам");
        queryList = UIBuilder.buildQueryList();
        JButton getResultButton = new JButton("Выполнить запрос");

        container.setLayout(new MainModuleLayout());
        container.setBackground(BACKGROUND);
        container.add(matrixLabel);
        container.add(matrixComboBox);
        container.add(modeSwitch);
        container.add(fillLabel);
        container.add(fillComboBoxPanel);
        container.add(propertyLabel);
        container.add(propertyComboBoxPanel);
        container.add(drawBarChartButton);
        container.add(dataLabel);
        container.add(queryList);
        container.add(getResultButton);

        matrixComboBox.addActionListener(matrixComboBoxActionListener());
        fillComboBox.addActionListener(fillComboBoxActionListener());
        drawBarChartButton.addActionListener(chartButtonActionListener());
        getResultButton.addActionListener(getResultButtonActionListener());
        modeSwitch.addActionListener(modeSwitchActionListener());

        locateWindow();
        setVisible(true);
        pack();
    }


    //обработчик события переключения режимов построения гистограмм
    //при переключении режима активируется/деактивируется меню с наполнителями
    //обновляются свойства, т.к. меняется способ построения гистограмм
    private ActionListener modeSwitchActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (modeSwitch.isSelected()) {
                    DefaultComboBoxModel<String> defaultFillComboBoxModel = new DefaultComboBoxModel<>(
                            DataManager.getFillKinds(selectedMatrix)
                    );
                    fillComboBox.removeAllItems();
                    fillComboBoxPanel.remove(fillComboBox);
                    fillComboBox.setModel(defaultFillComboBoxModel);
                    fillComboBoxPanel.add(fillComboBox);
                    String fill = (String) fillComboBox.getSelectedItem();
                    propertiesCache = DataManager.getPropertiesList2(selectedMatrix, fill);
                    propertyComboBox.removeAllItems();
                    propertyComboBoxPanel.remove(propertyComboBox);
                    propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                    propertyComboBoxPanel.add(propertyComboBox);
                    fillComboBox.setEnabled(true);
                    selectedFill = fill;
                } else {
                    propertiesCache = DataManager.getPropertiesList(selectedMatrix);
                    propertyComboBox.removeAllItems();
                    propertyComboBoxPanel.remove(propertyComboBox);
                    propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                    propertyComboBoxPanel.add(propertyComboBox);
                    fillComboBox.setEnabled(false);
                }
                fixBadPicture();
                pack();
            }
        };
        return l;
    }

    //обработчик события выбора матрицы в выпадающем списке-меню
    //при выборе матрицы при необходимости происходит обновление
    // списка наполнителей и списка свойств
    private ActionListener matrixComboBoxActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matrix = (String) matrixComboBox.getSelectedItem();
                if(!matrix.equals(selectedMatrix)){
                    if(modeSwitch.isSelected()){
                        fillComboBox.removeAllItems();
                        fillComboBoxPanel.remove(fillComboBox);
                        fillComboBox.setModel(new DefaultComboBoxModel<>(DataManager.getFillKinds(matrix)));
                        fillComboBoxPanel.add(fillComboBox);
                        propertyComboBox.removeAllItems();
                        propertyComboBoxPanel.remove(propertyComboBox);
                        String fill = (String) fillComboBox.getSelectedItem();
                        propertiesCache = DataManager.getPropertiesList2(matrix, fill);
                        propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                        propertyComboBoxPanel.add(propertyComboBox);
                        selectedMatrix = matrix;
                        selectedFill = fill;
                    }
                    else {
                        fillComboBox.removeAllItems();
                        fillComboBoxPanel.remove(fillComboBox);
                        fillComboBox.setModel(new DefaultComboBoxModel<>(DataManager.getFillKinds(matrix)));
                        fillComboBoxPanel.add(fillComboBox);
                        propertyComboBox.removeAllItems();
                        propertyComboBoxPanel.remove(propertyComboBox);
                        propertiesCache = DataManager.getPropertiesList(matrix);
                        propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                        propertyComboBoxPanel.add(propertyComboBox);
                        selectedMatrix = matrix;
                    }
                    pack();
                }
            }
        };
        return l;
    }

    //обработчик события выбора наполнителя в выпадающем списке-меню
    //при выборе наполнителя при необходимости происходит обновление
    // списка доступных свойств для выбранного наполнителя и матрицы
    private ActionListener fillComboBoxActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fill = (String) fillComboBox.getSelectedItem();
                if(fill == null){
                    return;
                }
                if(!fill.equals(selectedFill)){
                    propertyComboBox.removeAllItems();
                    propertyComboBoxPanel.remove(propertyComboBox);
                    propertiesCache = DataManager.getPropertiesList2(selectedMatrix, fill);
                    propertyComboBox.setModel(new DefaultComboBoxModel<>(propertiesCache.getPropertiesRus()));
                    propertyComboBoxPanel.add(propertyComboBox);
                    selectedFill = fill;
                    pack();
                }
            }
        };
        return l;
    }

    //обработчик нажатия кнопки "построить гистограмму"
    //обращается к соответствующим методам для получения, обработки данных
    //для построения гистограмм. Если окно гистограммы уже существует и активно
    //то гистограмма обновляется. Иначе создается новое окно
    private ActionListener chartButtonActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    drawBarChart();
                    if(chartFrame == null){
                        chartFrame = new ChartFrame(chartPanel, (String) propertyComboBox.getSelectedItem());
                    }
                    else {
                        if(chartFrame.isValid()){
                            chartFrame.update(chartPanel, (String) propertyComboBox.getSelectedItem());
                        }
                        else {
                            chartFrame = new ChartFrame(chartPanel, (String) propertyComboBox.getSelectedItem());
                        }
                    }
                }
                catch (RuntimeException ex){
                    JOptionPane.showMessageDialog(GraphicalAnalyticalModule.this,
                            "Отсутствуют данные для отображения гистограммы\n" +
                                    "с текущими параметрами! Выберите иные параметры!",
                            "Ошибка данных!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        return l;
    }

    //обработчик нажатия кнопки "выполнить запрос"
    //обращается к соответствующим методам для получения, обработки данных
    //для построения таблиц. Если окно с таблицей уже существует и активно
    //то таблица обновляется. Иначе создаётся новое окно
    private ActionListener getResultButtonActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(queryList.getSelectedIndex() < 0){
                    return;
                }
                else {
                    try{
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
                    catch (RuntimeException ex){
                        JOptionPane.showMessageDialog(GraphicalAnalyticalModule.this,
                                "Отсутствуют данные для отображения гистограммы\n" +
                                        "с текущими параметрами! Выберите иные параметры!",
                                "Ошибка данных!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        return l;
    }

    //метод определяет способ построения гистограмм
    //в зависимости от установленного пользователем режима
    private void drawBarChart(){
        if(modeSwitch.isSelected()){
            drawBarChart2();
        }
        else {
            drawBarChart1();
        }
    }

    //метод собирает и передаёт данные в билдер интерфейса для построения гистограмм
    //в режиме построения без анализа по составу композитов
    //тут вызывается метод статического класса-менеджера данных для запроса данных с сервера
    private void drawBarChart1(){
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

    //метод собирает и передаёт данные в билдер интерфейса для построения гистограмм
    //в режиме построения с анализом по составу композитов
    //тут вызывается метод статического класса-менеджера данных для запроса данных с сервера
    private void drawBarChart2(){
        String selectedPropertyEng = propertiesCache.getPropertiesEng().get(propertyComboBox.getSelectedIndex());
        String selectedPropertyRus = (String) propertyComboBox.getSelectedItem();
        String selectedFill = (String) fillComboBox.getSelectedItem();
        JFreeChart chart = UIBuilder.createBarChart2(
                DataManager.getDataset2(selectedMatrix, selectedFill, selectedPropertyEng),
                selectedPropertyRus,
                selectedMatrix,
                selectedFill
        );
        chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
    }

    //метод собирает и передаёт данные в билдер интерфейса для построения таблиц
    //тут вызывается метод статического класса-менеджера данных для запроса данных с сервера
    private void drawResultTable(){
        JTable resultTable =  DataManager.getResultTable(Converter.convertToViewName(queryList.getSelectedValue()));
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        RowSorter<TableModel> resultTableSorter = new TableRowSorter<>(resultTable.getModel());
        resultTable.setRowSorter(resultTableSorter);
        resultTablePanel = new JScrollPane(resultTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    //метод определяет положение окна главного меню на экране
    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getPreferredSize();
        Point point = new Point();
        point.x = (screenSize.width - windowSize.width) / 2;
        point.y = (screenSize.height - windowSize.height) / 2 - 50;
        setLocation(point);
        setResizable(false);
    }

    private void fixBadPicture(){
        container.setVisible(false);
        container.setVisible(true);
    }
}
