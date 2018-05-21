package logic;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.ResultSet;

public class DatasetBuilder {



    public static CategoryDataset createCategoryDatasetExample(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(100, "Условие 1", "Нанокомпозит 1");
        dataset.addValue(150, "Условие 2", "Нанокомпозит 1");
        dataset.addValue(210, "Условие 3", "Нанокомпозит 1");
        dataset.addValue(30, "нету условия", "Нанокомпозит 2");
        dataset.addValue(120, "Условие 4", "Нанокомпозит 3");
        dataset.addValue(80, "Условие 5", "Нанокомпозит 3");

        return dataset;
    }



}
