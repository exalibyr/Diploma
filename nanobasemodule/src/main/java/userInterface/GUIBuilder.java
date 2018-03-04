package userInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class GUIBuilder {

    public static JList<String> buildQueryList(){
        Vector<String> queryListContent = new Vector<>();
        queryListContent.add("info_about_ceramic_composites");
        queryListContent.add("questions_and_answers_for_ceramic_composites");
        JList<String> queryList = new JList<>(queryListContent);
        queryList.setAutoscrolls(true);
        return queryList;
    }

    static JFreeChart createBarChart(CategoryDataset dataset){
        JFreeChart chart = ChartFactory.createBarChart("Something about nano-composites",
                "Matrix",
                "Value",
                dataset);
        chart.addSubtitle(new TextTitle("Subtitle?"));
        chart.setBackgroundPaint(Color.GRAY);
        chart.setPadding(new RectangleInsets(2, 2 , 4, 4));
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        //barRenderer.setDrawBarOutline(true);
        //chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }
}
