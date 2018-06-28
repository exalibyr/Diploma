package userInterface;

import javax.swing.*;
import java.awt.*;

//класс объекта макета главного окна приложения
//содержит методы, которые располагают элементы управления
//реализует абстрактные методы интерфейса менеджера макетов
public class MainModuleLayout implements LayoutManager {

    //объект, содержащий размерность элементов управления
    private Dimension dimension = new Dimension();

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    //метод возвращает предпочитаемый размер окна
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateSize(parent);
    }

    //метод возвращает минимальный размер окна
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateSize(parent);
    }

    //метод устанавливает размеры и положение
    // элементов управления в главном меню приложения
    @Override
    public void layoutContainer(Container parent) {
        Component[] components = parent.getComponents();
        int currentY = 5;
        Dimension prefSize;
        for (int i = 0; i < components.length; i++) {
            if(components[i].getClass() == JPanel.class){
                prefSize = components[i].getPreferredSize();
                components[i].setBounds(0, currentY, prefSize.width, prefSize.height);
                currentY = currentY + 5 + prefSize.height;
            }
            else {
                prefSize = components[i].getPreferredSize();
                components[i].setBounds(5, currentY, prefSize.width, prefSize.height);
                currentY = currentY + 5 + prefSize.height;
            }
        }
        parent.revalidate();
    }

    //метод определяет необходимый минимальный и предпочитаемый размер
    //окна главного меню приложения в зависимости от элементов
    //управления, содержащихся в нём
    private Dimension calculateSize(Container container){
        Component[] components = container.getComponents();
        int maxWidth = 0;
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
