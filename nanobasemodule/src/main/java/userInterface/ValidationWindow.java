package userInterface;


import logic.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//класс объекта окна авторизации в системе
//наследуется от класса стандартного диалогового окна
public class ValidationWindow extends JDialog {

    //объект текстового поля для ввода логина
    private JTextField loginTextField = new JTextField(12);
    //объект текстового поля для ввода пароля
    private JPasswordField passwordField = new JPasswordField(12);


    public JTextField getLoginTextField() {
        return loginTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    //конструктор объекта окна авторизации
    //устанавливает элементы управления
    //и обработчики событий
    public ValidationWindow(){

        setTitle("Авторизация");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        ValidationWindowLayout layout = new ValidationWindowLayout();
        setLayout(layout);

        DataManager.registerMySQLDriver();

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
        container.setBackground(new Color(114, 209, 255, 50));

        locateWindow();
        pack();
        setVisible(true);
    }

    //метод производит валидацию
    //вызывает проверку введённых данных
    //в случае успешного входа закрывает окно авторизации
    //и запускает главное меню
    //в случае некорректных данных вызывает соответствующий обработчик
    private void signIn(){
        if(DataManager.validate(loginTextField.getText(), passwordField.getPassword(), ValidationWindow.this)){
            dispose();
            new GraphicalAnalyticalModule();
        }
    }

    //обработчик в случае некорректного логина/пароля
    private void onValidationFailure(){
        JOptionPane.showMessageDialog(ValidationWindow.this,
                "Неправильный логин или пароль!",
                "Ошибка входа!",
                JOptionPane.ERROR_MESSAGE);
        passwordField.setText("");
        loginTextField.setText("");
        loginTextField.requestFocus();
    }

    //метод производит проверяет заполнены ли поля логин и пароль
    //и при необходимости вызывает метод валидации или обработчик ошибки
    private void tryToSignIn(){
        if(loginTextField.getText().isEmpty() || (passwordField.getPassword().length == 0)){
            onValidationFailure();
        }
        else {
            signIn();
        }
    }

    //обработчик нажатия кнопки "войти в систему"
    private ActionListener signInActionListener(){
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryToSignIn();
            }
        };
        return l;
    }

    //обработчик нажатия кнопок клавиатуры для
    // текстового поля с логином
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

    //обработчик нажатия кнопок клавиатуры для
    // текстового поля с паролем
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

    //метод устанавливает окно авторизации на экране
    private void locateWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(new Dimension(300, 145));
        Point point = new Point();
        point.x = (screenSize.width - windowSize.width) / 2;
        point.y = (screenSize.height - windowSize.height) / 2 - 50;
        setLocation(point);
        setResizable(false);
        setPreferredSize(windowSize);
    }

}
