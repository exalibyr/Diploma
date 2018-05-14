package userInterface;

import java.awt.*;

public class VerticalLayout implements LayoutManager {

    private Dimension dimension = new Dimension();

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Component[] components = parent.getComponents();
        int currentY = 5;
        Dimension prefSize;
        for (int i = 0; i < components.length; i++) {
            prefSize = components[i].getPreferredSize();
            components[i].setBounds(5, currentY, prefSize.width, prefSize.height);
            currentY = currentY + 5 + prefSize.height;
        }
        parent.revalidate();
    }

    private Dimension calculateSize(Container container){
        Component[] components = container.getComponents();
        int maxWidth = 0, currentWidth;
        int height = 0;
        Dimension prefSize;
        for (int i = 0; i < components.length; i++){
            prefSize = components[i].getPreferredSize();
            height = height + 5 + prefSize.height;
            if(prefSize.width > maxWidth){
                maxWidth = prefSize.width;
            }
        }
        dimension.width = maxWidth + 10;
        dimension.height = height + 10;
        return dimension;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
