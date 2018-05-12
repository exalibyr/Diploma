package userInterface;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

public class ChartFrame extends JFrame{

    private ChartPanel chartPanel = null;
    private Container container = getContentPane();

    ChartFrame(ChartPanel chartPanel){
        setTitle("Диаграмма");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.chartPanel = chartPanel;
        container.add(this.chartPanel);
        setAlwaysOnTop(true);
        setVisible(true);
        validate();
        pack();
    }

    public void update(ChartPanel chartPanel){
        container.remove(this.chartPanel);
        this.chartPanel = chartPanel;
        container.add(this.chartPanel);
        revalidate();
    }


}
