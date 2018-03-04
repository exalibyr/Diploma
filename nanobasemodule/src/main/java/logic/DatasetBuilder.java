package logic;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class DatasetBuilder {

    public static CategoryDataset createCategoryDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(100, "Density", "Al2O3");
        dataset.addValue(150, "Viscosity", "Al2O3");
        dataset.addValue(120, "Density", "ZnO");
        dataset.addValue(80, "Viscosity", "ZnO");
        return dataset;
    }

}
