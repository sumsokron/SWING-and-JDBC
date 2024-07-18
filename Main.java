import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame implements ActionListener {
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton previousButton, nextButton;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public Main() {
        // Create frame
        JFrame frame = new JFrame("Customer");

        // Create text fields
        idField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        phoneField = new JTextField();

        idField.setEditable(false);
        lastNameField.setEditable(false);
        firstNameField.setEditable(false);
        phoneField.setEditable(false);

        // Create buttons
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");

        // Set layout manager
        frame.setLayout(null);

        // Set bounds for text fields and labels
        JLabel idLabel = new JLabel("ID:");
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel phoneLabel = new JLabel("Phone:");

        idLabel.setBounds(20, 20, 100, 25);
        idField.setBounds(120, 20, 150, 25);
        lastNameLabel.setBounds(20, 60, 100, 25);
        lastNameField.setBounds(120, 60, 150, 25);
        firstNameLabel.setBounds(20, 100, 100, 25);
        firstNameField.setBounds(120, 100, 150, 25);
        phoneLabel.setBounds(20, 140, 100, 25);
        phoneField.setBounds(120, 140, 150, 25);

        previousButton.setBounds(20, 180, 100, 25);
        nextButton.setBounds(170, 180, 100, 25);

        // Add action listeners
        previousButton.addActionListener(this);
        nextButton.addActionListener(this);

        // Add components to frame
        frame.add(idLabel);
        frame.add(idField);
        frame.add(lastNameLabel);
        frame.add(lastNameField);
        frame.add(firstNameLabel);
        frame.add(firstNameField);
        frame.add(phoneLabel);
        frame.add(phoneField);
        frame.add(previousButton);
        frame.add(nextButton);

        // Set frame properties
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Database connection
        connectDatabase();
        loadFirstCustomer();
    }

    private void connectDatabase() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Replace with your database credentials
            String url = "jdbc:mysql://localhost:3306/exam_jdbc";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM customers");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFirstCustomer() {
        try {
            if (resultSet.first()) {
                showCustomer();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCustomer() {
        try {
            idField.setText(resultSet.getString("customer_id"));
            lastNameField.setText(resultSet.getString("customer_last_name"));
            firstNameField.setText(resultSet.getString("customer_first_name"));
            phoneField.setText(resultSet.getString("customer_phone"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == previousButton && !resultSet.isFirst()) {
                resultSet.previous();
                showCustomer();
            } else if (e.getSource() == nextButton && !resultSet.isLast()) {
                resultSet.next();
                showCustomer();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
