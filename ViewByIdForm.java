import javax.swing.*;
import java.sql.*;

public class ViewByIdForm extends JFrame {
    public ViewByIdForm() {
        setTitle("View Employee by ID");
        setSize(350, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Enter Employee ID:");
        JTextField idField = new JTextField();
        JButton viewBtn = new JButton("View");

        idLabel.setBounds(30, 30, 150, 25);
        idField.setBounds(160, 30, 130, 25);
        viewBtn.setBounds(110, 80, 100, 30);

        add(idLabel);
        add(idField);
        add(viewBtn);

        setVisible(true);

        viewBtn.addActionListener(e -> {
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
                    String name = rs.getString("name");
                    String dept = rs.getString("department");
                    double salary = rs.getDouble("salary");

                    String message = "ID: " + id +
                            "\nName: " + name +
                            "\nDepartment: " + dept +
                            "\nSalary: " + salary;
                    JOptionPane.showMessageDialog(this, message);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.");
                }

                rs.close();
                stmt.close();
                conn.close();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
