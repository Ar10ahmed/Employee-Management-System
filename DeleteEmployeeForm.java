import javax.swing.*;
import java.sql.*;

public class DeleteEmployeeForm extends JFrame {
    public DeleteEmployeeForm() {
        setTitle("Delete Employee");
        setSize(350, 180);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Enter Employee ID:");
        JTextField idField = new JTextField();
        JButton deleteBtn = new JButton("Delete");

        idLabel.setBounds(30, 30, 150, 25);
        idField.setBounds(160, 30, 130, 25);
        deleteBtn.setBounds(110, 80, 100, 30);

        add(idLabel);
        add(idField);
        add(deleteBtn);

        setVisible(true);

        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete employee ID " + id + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Connection conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/kl",
                            "postgres", "admin"
                    );

                    String sql = "DELETE FROM employees WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, id);

                    int rows = stmt.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No employee found with that ID.");
                    }

                    stmt.close();
                    conn.close();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
