package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import entities.User;

/**
 * Provides a registration interface for new users.
 * This class handles the creation of new user accounts, including input validation.
 */
public class RegistrationFrame extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JPasswordField confirmPasswordField;
    JButton registerButton;

    /**
     * Constructs a new RegistrationFrame.
     * Sets up the user interface for registration including text fields for username and password,
     * a field for confirming the password, and a button for registration.
     */
    public RegistrationFrame() {
        setTitle("Register");
        setLayout(new GridLayout(4, 2));
        setSize(300, 200);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        add(registerButton);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Invoked when an action occurs, such as clicking the register button.
     * This method handles the registration process, including validation of input fields.
     *
     * @param e The event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Check if the password and confirm password fields match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check for disallowed characters in username and password
        if (containsDisallowedCharacters(username) || containsDisallowedCharacters(password)) {
            JOptionPane.showMessageDialog(this, "Username and password cannot contain : \" '", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // Use the User class's method to register new users
        if (User.register(username, password)) {
            // On successful registration:
            JOptionPane.showMessageDialog(this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            // Show error message on failed registration
            JOptionPane.showMessageDialog(this, "Registration failed", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks if a string contains disallowed characters.
     * Disallowed characters are defined in a pattern.
     *
     * @param str The string to be checked.
     * @return true if the string contains disallowed characters, false otherwise.
     */
    private boolean containsDisallowedCharacters(String str) {
        String disallowedCharsPattern = "[:\"']";
        return str.matches(".*" + disallowedCharsPattern + ".*");
    }
}
