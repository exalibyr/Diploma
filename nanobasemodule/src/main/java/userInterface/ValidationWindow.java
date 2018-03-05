package userInterface;


import logic.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ValidationWindow extends JDialog {

    private static final int PADDING = 10;

    private DataManager dataManager = new DataManager();
    private JTextField loginTextField = new JTextField(12);
    private JPasswordField passwordField = new JPasswordField(12);

    public ValidationWindow(){

        setTitle("Авторизация в базе данных");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        ValidationWindowLayout layout = new ValidationWindowLayout();
        setLayout(layout);

        dataManager.registerMySQLDriver();

        Container container = getContentPane();
        JLabel loginLabel = new JLabel("Имя пользователя");
        JLabel passwordLabel = new JLabel("Пароль");
        passwordField.setEchoChar('•');
        JButton signInButton = new JButton("Войти в систему");

        signInButton.addActionListener(signInActionListener());
        loginTextField.addKeyListener(loginTextFieldKeyListener());
        passwordField.addKeyListener(passwordFieldKeyListener());
        container.add(loginTextField);
        container.add(passwordField);
        container.add(signInButton);
        container.add(loginLabel);
        container.add(passwordLabel);

//        SpringLayout layout = new SpringLayout();
//        container.setLayout(layout);
//        layout.putConstraint(SpringLayout.WEST, container, PADDING, SpringLayout.WEST, loginLabel);
//        layout.putConstraint(SpringLayout.NORTH, container, PADDING, SpringLayout.NORTH, loginLabel);
//        layout.putConstraint(SpringLayout.WEST, loginLabel, PADDING, SpringLayout.WEST, loginTextField);
//        layout.putConstraint(SpringLayout.NORTH, container, PADDING, SpringLayout.NORTH, loginTextField);
//        layout.putConstraint(SpringLayout.);

        locateWindow();
        pack();
        setVisible(true);
    }

    private void signIn(){
        if(dataManager.validate(loginTextField.getText(), passwordField.getPassword())){
            dispose();
            new GUI();
        }
        else {
            onValidationFailure();
        }
    }

    private void onValidationFailure(){
        JOptionPane.showMessageDialog(ValidationWindow.this,
                "Неправильный логин или пароль!",
                "Ошибка входа!",
                JOptionPane.ERROR_MESSAGE);
        passwordField.setText("");
        loginTextField.setText("");
        loginTextField.requestFocus();
    }

    private void tryToSignIn(){
        if(loginTextField.getText().isEmpty() || (passwordField.getPassword().length == 0)){
            onValidationFailure();
        }
        else {
            signIn();
        }
    }

    private ActionListener signInActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToSignIn();
            }
        };
        return l;
    }

    private KeyListener loginTextFieldKeyListener(){
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:{
                        if(loginTextField.getText().isEmpty()) {
                            onValidationFailure();
                        }
                        else {
                            if(passwordField.getPassword().length == 0){
                                passwordField.requestFocus();
                            }
                            else {
                                signIn();
                            }
                        }
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
                        loginTextField.setText("");
                        break;
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

    private KeyListener passwordFieldKeyListener(){
        KeyListener l = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_ENTER:{
                        tryToSignIn();
                        break;
                    }
                    case KeyEvent.VK_UP:{
                        loginTextField.requestFocus();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE:{
                        dispose();
                        break;
                    }
                    case KeyEvent.VK_DELETE:{
                        passwordField.setText("");
                        break;
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

//    private KeyListener keysListener(){
//        KeyListener l = new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                switch (e.getKeyCode()){
//                    case KeyEvent.VK_ENTER:{
//                        if(loginTextField.getText().isEmpty()) {
//                            onValidationFailure();
//                        }
//                        else {
//                            if(passwordField.getPassword().length == 0){
//                                if(KeyboardFocusManager.getCurrentKeyboardFocusManager()
//                                        .getFocusOwner().getClass() == JTextField.class)
//                                {
//                                    passwordField.requestFocus();
//                                }
//                                else {
//                                    onValidationFailure();
//                                }
//                            }
//                            else {
//                                signIn();
//                            }
//                        }
//                        break;
//                    }
//                    case KeyEvent.VK_UP:{
//                        loginTextField.requestFocus();
//                        break;
//                    }
//                    case KeyEvent.VK_DOWN:{
//                        passwordField.requestFocus();
//                        break;
//                    }
//                    case KeyEvent.VK_ESCAPE:{
//                        dispose();
//                        break;
//                    }
//                    case KeyEvent.VK_DELETE:{
//                        Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//                        if(component.getClass() == JTextField.class){
//                            loginTextField.setText("");
//                            break;
//                        }
//                        if(component.getClass() == JPasswordField.class){
//                            passwordField.setText("");
//                            break;
//                        }
//                    }
//                    default:break;
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//
//            }
//        };
//        return l;
//    }

    private void locateWindow(){
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
