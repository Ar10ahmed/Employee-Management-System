import javax.swing.*;
import java.sql.*;

public class UpdateEmployeeForm extends JFrame {
    public UpdateEmployeeForm() {
        setTitle("Update Employee");
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Enter Employee ID:");
        JTextField idField = new JTextField();
        JButton fetchBtn = new JButton("Fetch");

        idLabel.setBounds(30, 30, 150, 25);
        idField.setBounds(180, 30, 150, 25);
        fetchBtn.setBounds(140, 70, 100, 30);

        add(idLabel); add(idField); add(fetchBtn);

        // Fields for update (initially hidden)
        JLabel nameLabel = new JLabel("Name:");
        JLabel deptLabel = new JLabel("Department:");
        JLabel salaryLabel = new JLabel("Salary:");

        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField salaryField = new JTextField();

        JButton updateBtn = new JButton("Update");

        nameLabel.setBounds(30, 120, 100, 25);
        nameField.setBounds(140, 120, 180, 25);

        deptLabel.setBounds(30, 160, 100, 25);
        deptField.setBounds(140, 160, 180, 25);

        salaryLabel.setBounds(30, 200, 100, 25);
        salaryField.setBounds(140, 200, 180, 25);

        updateBtn.setBounds(140, 250, 100, 30);

        add(nameLabel); add(nameField);
        add(deptLabel); add(deptField);
        add(salaryLabel); add(salaryField);
        add(updateBtn);

        // Hide update fields initially
        nameLabel.setVisible(false);
        nameField.setVisible(false);
        deptLabel.setVisible(false);
        deptField.setVisible(false);
        salaryLabel.setVisible(false);
        salaryField.setVisible(false);
        updateBtn.setVisible(false);

        setVisible(true);

        fetchBtn.addActionListener(e -> {
            int id;
            try {
                id = Integer.parseInt(idField.getText());

                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/kl",
                        "postgres", "admin"
                );

                String sql = "SELECT * FROM employees WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    deptField.setText(rs.getString("department"));
                    salaryField.setText(String.valueOf(rs.getDouble("salary")));

                    nameLabel.setVisible(true);
                    nameField.setVisible(true);
                    deptLabel.setVisible(true);
                    deptField.setVisible(true);
                    salaryLabel.setVisible(true);
                    salaryField.setVisible(true);
                    updateBtn.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No employee found with that ID.");
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String dept = deptField.getText();
                double salary = Double.parseDouble(salaryField.getText());

                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/kl",
                        "postgres", "admin"
                );

                String updateSql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, name);
                updateStmt.setString(2, dept);
                updateStmt.setDouble(3, salary);
                updateStmt.setInt(4, id);

                int rows = updateStmt.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed.");
                }

                updateStmt.close();
                conn.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
