package logic;

import org.jfree.data.category.CategoryDataset;

public class MyDataset {

    private CategoryDataset categoryDataset;
    private String measure;

    public CategoryDataset getCategoryDataset() {
        return categoryDataset;
    }

    public void setCategoryDataset(CategoryDataset categoryDataset) {
        this.categoryDataset = categoryDataset;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
