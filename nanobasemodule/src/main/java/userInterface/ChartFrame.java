package userInterface;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

public class ChartFrame extends JFrame{

    private ChartPanel chartPanel = null;
    private Container container = getContentPane();

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

    public void update(ChartPanel chartPanel, String title){
        container.remove(this.chartPanel);
        this.chartPanel = chartPanel;
        container.add(this.chartPanel);
        setTitle(title);
        locateWindow();
        revalidate();
    }

    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = container.getPreferredSize();
        Point point = new Point();
        point.x = -5;
        point.y = 0;
        setLocation(point);
    }

}
