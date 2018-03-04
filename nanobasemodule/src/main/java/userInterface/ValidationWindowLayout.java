package userInterface;

import javax.swing.*;
import java.awt.*;

public class ValidationWindowLayout implements LayoutManager{

    private static final int GAP = 10;

    private Dimension dimension = new Dimension();

    private Component loginLabel = null;
    private Component passwordLabel = null;
    private Component loginTextField = null;
    private Component passwordField = null;
    private Component validateButton = null;

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

    public Dimension getDimension() {
        return dimension;
    }

    private Dimension calculateSize(Container parent){
        detectComponents(parent);
        dimension.width = 3 * GAP + loginTextField.getWidth();
        int loginLabelWidth = loginLabel.getWidth(), passwordLabelWidth = passwordLabel.getWidth();
        dimension.width += loginLabelWidth >= passwordLabelWidth ? loginLabelWidth : passwordLabelWidth;
        dimension.height = 4 * GAP + loginLabel.getHeight() + passwordLabel.getHeight() + validateButton.getHeight();
        return dimension;
    }

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
