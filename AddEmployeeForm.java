import javax.swing.*;
import java.sql.*;

public class AddEmployeeForm extends JFrame {
    public AddEmployeeForm() {
        setTitle("Add Employee");
        setSize(350, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel deptLabel = new JLabel("Department:");
        JLabel salaryLabel = new JLabel("Salary:");

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField salaryField = new JTextField();

        JButton saveBtn = new JButton("Save");

        // Positioning
        idLabel.setBounds(30, 30, 80, 25);
        nameLabel.setBounds(30, 70, 80, 25);
        deptLabel.setBounds(30, 110, 80, 25);
        salaryLabel.setBounds(30, 150, 80, 25);

        idField.setBounds(120, 30, 180, 25);
        nameField.setBounds(120, 70, 180, 25);
        deptField.setBounds(120, 110, 180, 25);
        salaryField.setBounds(120, 150, 180, 25);
        saveBtn.setBounds(120, 200, 100, 30);

        add(idLabel); add(idField);
        add(nameLabel); add(nameField);
        add(deptLabel); add(deptField);
        add(salaryLabel); add(salaryField);
        add(saveBtn);

        setVisible(true);

        // Save button logic (connect to PostgreSQL)
        saveBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String dept = deptField.getText();
                double salary = Double.parseDouble(salaryField.getText());

                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/kl",
                        "postgres", "admin");

                String sql = "INSERT INTO employees (id, name, department, salary) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setString(3, dept);
                stmt.setDouble(4, salary);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Employee added successfully!");
                }

                stmt.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}

