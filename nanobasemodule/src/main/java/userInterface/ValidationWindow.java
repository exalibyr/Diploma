package userInterface;


import logic.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ValidationWindow extends JDialog {

    private DataManager dataManager = new DataManager();

    public ValidationWindow(){
        setTitle("Авторизация в базе данных");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        ValidationWindowLayout layout = new ValidationWindowLayout();
        setLayout(layout);

        dataManager.registerMySQLDriver();

        Container container = getContentPane();
        JTextField loginTextField = new JTextField(12);
        JPasswordField passwordField = new JPasswordField(12);
        JLabel loginLabel = new JLabel("Имя пользователя");
        JLabel passwordLabel = new JLabel("Пароль");
        passwordField.setEchoChar('•');
        JButton signInButton = new JButton("Войти в систему");

        signInButton.addActionListener(signInActionListener(loginTextField, passwordField));
        loginTextField.addKeyListener(keysListener(loginTextField, passwordField));
        passwordField.addKeyListener(keysListener(loginTextField, passwordField));
        container.add(loginTextField);
        container.add(passwordField);
        container.add(signInButton);
        container.add(loginLabel);
        container.add(passwordLabel);

        locate();
        pack();
        setVisible(true);
    }

    private void signIn(JTextField loginTextField, JPasswordField passwordField){
        if(dataManager.validate(loginTextField.getText(), passwordField.getPassword())){
            dispose();
            new GUI();
        }
        else {
            JOptionPane.showMessageDialog(ValidationWindow.this,
                    "Неправильный логин или пароль!",
                    "Ошибка входа!",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private ActionListener signInActionListener(JTextField loginTextField, JPasswordField passwordField){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn(loginTextField, passwordField);
            }
        };
        return l;
    }

    private KeyListener keysListener(JTextField loginTextField, JPasswordField passwordField){
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:{
                        signIn(loginTextField, passwordField);
                        break;
                    }
                    case KeyEvent.VK_UP:{
                        loginTextField.requestFocus();
                        break;
                    }
                    case KeyEvent.VK_DOWN:{
                        passwordField.requestFocus();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE:{
                        dispose();
                        break;
                    }
                    case KeyEvent.VK_DELETE:{
                        Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                        if(component.getClass() == JTextField.class){
                            loginTextField.setText("");
                            return;
                        }
                        if(component.getClass() == JPasswordField.class){
                            passwordField.setText("");
                            return;
                        }
                    }
                    default:break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        return l;
    }

    private void locate(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(new Dimension(300, 145));
        Point point = new Point();
        point.x = (screenSize.width - windowSize.width) / 2;
        point.y = (screenSize.height - windowSize.height) / 2;
        setLocation(point);
        setResizable(false);
        setPreferredSize(windowSize);
    }
}
