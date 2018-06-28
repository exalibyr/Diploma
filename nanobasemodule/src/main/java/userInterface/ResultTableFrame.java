package userInterface;

import javax.swing.*;
import java.awt.*;

//класс описывающий объект окна, содержащего таблицу
//наследуется от класса стандартного окна
public class ResultTableFrame extends JFrame{

    //объект панели таблицы с ползунками для прокрутки
    private JScrollPane resultPanel = null;
    //контейнер, является ссылкой на объект окна
    private Container container = getContentPane();

    //конструктор окна для таблицы
    public ResultTableFrame(JScrollPane panel, String title){
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.resultPanel = panel;
        container.add(this.resultPanel);
        setAlwaysOnTop(true);
        locateWindow();
        setVisible(true);
        pack();
    }

    //метод позволяет обновлять панель с таблицей
    //без конструирования нового окна
    public void update(JScrollPane panel, String title){
        container.remove(this.resultPanel);
        this.resultPanel = panel;
        container.add(this.resultPanel);
        setTitle(title);
        locateWindow();
        revalidate();
    }

    //метод устанавливает окно с таблицей на экране
    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getPreferredSize();
        Point point = new Point();
        point.x = screenSize.width - windowSize.width - 10;
        point.y = 0;
        setLocation(point);
    }
}
