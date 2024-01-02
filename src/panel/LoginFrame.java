package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entities.User;

/**
 * Provides a login interface for users to enter their username and password.
 * This class handles user authentication and can redirect to either the main application window
 * upon successful login or a registration window.
 */
public class LoginFrame extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;

    /**
     * Constructs a new LoginFrame.
     * Sets up the user interface for login including text fields for username and password,
     * and buttons for login and registration.
     */
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

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegistrationFrame(); // Open the registration window
        });
        add(registerButton);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Invoked when an action occurs, such as clicking the login button.
     * This method handles the login process using entered credentials.
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Use the User class's login method to check credentials
        if (User.login(username, password)) {
            new MainFrame(username); // Open the main application window
            this.dispose(); // Close the login window
        } else {
            // Show error message on failed login
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
