import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewAllEmployeesForm extends JFrame {

    public ViewAllEmployeesForm() {
        setTitle("All Employees");
        setSize(500, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Table columns
        String[] columnNames = {"ID", "Name", "Department", "Salary"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 450, 300);
        add(scrollPane);

        // Load data from PostgreSQL
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/kl",
                    "postgres", "admin"
            );

            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String dept = rs.getString("department");
                double salary = rs.getDouble("salary");

                // Add row to table
                tableModel.addRow(new Object[]{id, name, dept, salary});
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }

        setVisible(true);
    }
}
