package userInterface;

import javax.swing.*;
import java.awt.*;

public class ResultTableFrame extends JFrame{

    private JScrollPane resultPanel = null;
    private Container container = getContentPane();

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

    public void update(JScrollPane panel, String title){
        container.remove(this.resultPanel);
        this.resultPanel = panel;
        container.add(this.resultPanel);
        setTitle(title);
        locateWindow();
        revalidate();
    }

    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getPreferredSize();
        Point point = new Point();
        point.x = screenSize.width - windowSize.width - 10;
        point.y = 0;
        setLocation(point);
    }
}
