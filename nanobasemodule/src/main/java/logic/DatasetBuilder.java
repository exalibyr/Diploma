package logic;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.sql.ResultSet;

public class DatasetBuilder {



    public static CategoryDataset createCategoryDatasetExample(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(100, "ЕдИзм/Условие 1", "Нанокомпозит 1(Наполнитель 1)");
        dataset.addValue(150, "ЕдИзм/Условие 2", "Нанокомпозит 1(Наполнитель 1)");
        dataset.addValue(210, "ЕдИзм/Условие 3", "Нанокомпозит 1(Наполнитель 1)");
        dataset.addValue(30, "ЕдИзм/нету условия", "Нанокомпозит 2(Наполнитель 1)");
        dataset.addValue(120, "ЕдИзм/Условие 4", "Нанокомпозит 3(Наполнитель 2)");
        dataset.addValue(80, "ЕдИзм/Условие 5", "Нанокомпозит 3(Наполнитель 2)");

        return dataset;
    }



}
