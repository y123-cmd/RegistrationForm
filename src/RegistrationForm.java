import preparedStatement.preparedStatement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RegistrationForm extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registerPanel;


    public RegistrationForm(JFrame parent) {
   super(parent);
   setTitle("Create a new account");
   setContentPane(registerPanel);
   setMinimumSize(new Dimension(450,474));
   setModal(true);
   setLocationRelativeTo(parent);
   setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            registerUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            dispose();
            }
        });

        setVisible(true);
    }

    private void registerUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String phone = tfPhone.getText();
        String address = tfAddress.getText();
        String password = String.valueOf(pfPassword.getPassword());
        String confirmPassword = String.valueOf(pfConfirmPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "please enter all fields",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "confirm password does not match",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
       user= addUserToDatabase(name, email, phone, address, password);
        if (user!= null) {
            dispose();
        }
        }
       public User user;
       private User addUserToDatabase(String name, String email, String phone, String address, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)){
            //connection created successfully....
            String sql = "INSERT INTO users(name, email, phone, address, password) VALUES=(?,?,?,?,?)";
            try(PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phone);
                preparedStatement.setString(4, address);
                preparedStatement.setString(5, password);

                int addedRows = preparedStatement.executeUpdate();
                if (addedRows > 0) {
                    user = new User();
                    user.name = name;
                    user.phone = phone;
                    user.address = address;
                    user.password = password;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
            //insert row into the table

        return user;
    }
    public static void main(String[] args) {
           RegistrationForm myForm = new RegistrationForm(null);
           User user = myForm.user;
           if (user!= null) {
               System.out.println("Successful registration of:" + user.name);
           }
           else
               System.out.println("Registration canceled");

    }
}
