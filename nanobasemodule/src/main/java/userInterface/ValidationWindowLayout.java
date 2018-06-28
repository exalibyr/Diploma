package userInterface;

import javax.swing.*;
import java.awt.*;

//класс объекта макета окна авторизации приложения
//содержит методы, которые располагают элементы управления
//реализует абстрактные методы интерфейса менеджера макетов
public class ValidationWindowLayout implements LayoutManager{

    //константа содержащая величину промежутка
    //между элементами
    private static final int GAP = 10;

    //объект, содержащий размеры элементов управления
    private Dimension dimension = new Dimension();
    //объекты элементов управления
    private Component loginLabel = null;
    private Component passwordLabel = null;
    private Component loginTextField = null;
    private Component passwordField = null;
    private Component validateButton = null;
    private Component URLLabel = null;
    private Component URLTextField = null;

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
    // элементов управления в окне авторизации приложения
    @Override
    public void layoutContainer(Container parent) {
        detectComponents(parent);
        int currentY = GAP, currentX = GAP;
        Dimension componentPreferredSize = loginLabel.getWidth() >= passwordLabel.getWidth() ?
                loginLabel.getPreferredSize() : passwordLabel.getPreferredSize();

        loginLabel.setBounds(currentX, currentY, componentPreferredSize.width, componentPreferredSize.height);
        currentY += GAP + componentPreferredSize.height;

        passwordLabel.setBounds(currentX, currentY, componentPreferredSize.width, componentPreferredSize.height);
        currentX += GAP + componentPreferredSize.width;
        currentY = GAP;

        componentPreferredSize = loginTextField.getPreferredSize();
        loginTextField.setBounds(currentX, currentY, componentPreferredSize.width, componentPreferredSize.height);
        currentY += GAP + componentPreferredSize.height;

        componentPreferredSize = passwordField.getPreferredSize();
        passwordField.setBounds(currentX, currentY, componentPreferredSize.width, componentPreferredSize.height);
        currentX = GAP;
        currentY += GAP + componentPreferredSize.height;

        componentPreferredSize = validateButton.getPreferredSize();
        validateButton.setBounds(currentX, currentY, componentPreferredSize.width, componentPreferredSize.height);

        parent.revalidate();
    }

    //метод определяет необходимый минимальный и предпочитаемый размер
    //окна авторизации приложения в зависимости от элементов
    //управления, содержащихся в нём
    private Dimension calculateSize(Container parent){
        detectComponents(parent);
        int loginWidth = loginLabel.getWidth() + loginTextField.getWidth();
        int passwordWidth = passwordLabel.getWidth() + passwordField.getWidth();
        dimension.width = 3 * GAP + loginWidth >= passwordWidth ? loginWidth : passwordWidth;
        dimension.height = 4 * GAP + loginLabel.getHeight() + passwordLabel.getHeight() + validateButton.getHeight();
        return dimension;
    }


    public Dimension getDimension() {
        return dimension;
    }


    //метод идентифицирует элементы управления
    //в окне авторизации
    private void detectComponents(Container parent){
        Component[] components = parent.getComponents();
        String labelName;
        for (Component component: components) {
            if(component.getClass() == JLabel.class){
                labelName = ((JLabel) component).getText();
                if(labelName.equals("Имя пользователя")){
                    loginLabel = component;
                }
                if(labelName.equals("Пароль")){
                    passwordLabel = component;
                }
            }
            if(component.getClass() == JTextField.class){
                loginTextField = component;
            }
            if(component.getClass() == JPasswordField.class){
                passwordField = component;
            }
            if(component.getClass() == JButton.class){
                validateButton = component;
            }
        }
    }
}
