package panel;

import entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;
    private String loggedInUsername;

    public LoginFrame() {
        setTitle("Login");
        setLayout(new GridLayout(3, 2));
        setSize(300, 150);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        add(loginButton);

//        registerButton = new JButton("Register");
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new RegistrationFrame(); // Open the registration window
//            }
//        });
//        add(registerButton);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Use the User class's login method to check credentials
        if (User.login(username, password)) {
            loggedInUsername = username;
//            new MainFrame(loggedInUsername); // Open the main application window
            this.dispose(); // Close the login window
        } else {
            // Show error message on failed login
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
