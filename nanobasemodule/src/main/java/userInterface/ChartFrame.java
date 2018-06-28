package userInterface;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

//класс описывающий объект окна, содержащего гистограмму
//наследуется от класса стандартного окна
public class ChartFrame extends JFrame{

    //объект панели с гитограммой
    private ChartPanel chartPanel = null;
    //контейнер, является ссылкой на объект окна
    private Container container = getContentPane();

    //конструктор окна для гистограммы
    ChartFrame(ChartPanel chartPanel, String title){
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.chartPanel = chartPanel;
        container.add(this.chartPanel);
        setAlwaysOnTop(true);
        locateWindow();
        setVisible(true);
        pack();
    }

    //метод позволяет обновлять панель с гистограммой
    //без конструирования нового окна
    public void update(ChartPanel chartPanel, String title){
        container.remove(this.chartPanel);
        this.chartPanel = chartPanel;
        container.add(this.chartPanel);
        setTitle(title);
        locateWindow();
        revalidate();
    }

    //метод устанавливает окно с гистограммой на экране
    private void locateWindow(){
        Point point = new Point();
        point.x = -5;
        point.y = 0;
        setLocation(point);
    }

}
