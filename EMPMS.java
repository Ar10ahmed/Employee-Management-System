import java.sql.*;
import java.util.Scanner;
import javax.swing.*;

public class EMPMS
{
    // Database credentials
    static final String URL = "jdbc:postgresql://localhost:5432/kl";
    static final String USER = "postgres";
    static final String PASSWORD = "admin";

    public static void main(String[] args)
    {

        JFrame frame = new JFrame("Employee Management System");

        // Create Buttons
        JButton addBtn = new JButton("Add Employee");
        JButton viewAllBtn = new JButton("View All Employees");
        JButton viewByIdBtn = new JButton("View by ID");
        JButton deleteBtn = new JButton("Delete Employee");
        JButton updateBtn = new JButton("Update Employee");

        // Set button positions
        addBtn.setBounds(50, 40, 200, 30);
        viewAllBtn.setBounds(50, 80, 200, 30);
        viewByIdBtn.setBounds(50, 120, 200, 30);
        deleteBtn.setBounds(50, 160, 200, 30);
        updateBtn.setBounds(50, 200, 200, 30);

        // Add to frame
        frame.add(addBtn);
        frame.add(viewAllBtn);
        frame.add(viewByIdBtn);
        frame.add(deleteBtn);
        frame.add(updateBtn);

        frame.setSize(320, 320);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //  Button Actions - Open new forms
        addBtn.addActionListener(e -> new AddEmployeeForm());
        viewAllBtn.addActionListener(e -> new ViewAllEmployeesForm());
        viewByIdBtn.addActionListener(e -> new ViewByIdForm());
        deleteBtn.addActionListener(e -> new DeleteEmployeeForm());
        updateBtn.addActionListener(e -> new UpdateEmployeeForm());

        Scanner sc = new Scanner(System.in);

        while (true)
        {
            System.out.println("\n===== Employee Management Menu =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. View Employee by ID");
            System.out.println("4. Delete Employee by ID");
            System.out.println("5. Update Employee by ID");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addEmployee(sc);
                case 2 -> viewAllEmployees();
                case 3 -> viewEmployeeById(sc);
                case 4 -> deleteEmployeeById(sc);
                case 5 -> updateEmployeeById(sc);
                case 6 -> {
                    System.out.println("Exiting. Goodbye!");
                    sc.close();
                    return;
                }
                 //
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

    }

    // Add employee
    static void addEmployee(Scanner sc)
    {
        try
        {
            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine(); // Consume newline

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Department: ");
            String dept = sc.nextLine();

            System.out.print("Enter Salary: ");
            double salary = sc.nextDouble();

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "INSERT INTO employees (id, name, department, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, dept);
            stmt.setDouble(4, salary);

            int rows = stmt.executeUpdate();
            if (rows > 0)
            {
                System.out.println("Employee added successfully!");
            }

            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    // View all employees
    static void viewAllEmployees()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM employees";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Employee List ---");
            System.out.printf("%-5s %-15s %-15s %-10s\n", "ID", "Name", "Department", "Salary");

            while (rs.next())
            {
                System.out.printf("%-5d %-15s %-15s %-10.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Error viewing employees: " + e.getMessage());
        }
    }

    // View employee by ID
    static void viewEmployeeById(Scanner sc)
    {
        try
        {
            System.out.print("Enter Employee ID to view: ");
            int id = sc.nextInt();

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "SELECT * FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                System.out.println("\n--- Employee Details ---");
                System.out.println("ID        : " + rs.getInt("id"));
                System.out.println("Name      : " + rs.getString("name"));
                System.out.println("Department: " + rs.getString("department"));
                System.out.println("Salary    : " + rs.getDouble("salary"));
            }
            else
            {
                System.out.println("Employee not found.");
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Error viewing employee: " + e.getMessage());
        }
    }
    // Delete employee by ID
    static void deleteEmployeeById(Scanner sc) {
        try {
            System.out.print("Enter Employee ID to delete: ");
            int id = sc.nextInt();

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "DELETE FROM employees WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0)
            {
                System.out.println("Employee deleted successfully.");
            }
            else
            {
                System.out.println("No employee found with that ID.");
            }

            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
    static void updateEmployeeById(Scanner sc)
    {
        try
        {
            System.out.print("Enter Employee ID to update: ");
            int id = sc.nextInt();
            sc.nextLine(); // consume newline

            // Choose what to update
            System.out.println("What do you want to update?");
            System.out.println("1. Name");
            System.out.println("2. Department");
            System.out.println("3. Salary");
            System.out.print("Enter choice: ");
            int updateChoice = sc.nextInt();
            sc.nextLine(); // consume newline

            String field = "";
            String newValue = "";
            double newSalary = 0;

            switch (updateChoice) {
                case 1 -> {
                    field = "name";
                    System.out.print("Enter new name: ");
                    newValue = sc.nextLine();
                }
                case 2 -> {
                    field = "department";
                    System.out.print("Enter new department: ");
                    newValue = sc.nextLine();
                }
                case 3 -> {
                    field = "salary";
                    System.out.print("Enter new salary: ");
                    newSalary = sc.nextDouble();
                }
                default -> {
                    System.out.println("Invalid choice.");
                    return;
                }
            }

            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql;
            PreparedStatement stmt;

            if (!field.equals("salary")) {
                sql = "UPDATE employees SET " + field + " = ? WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, newValue);
                stmt.setInt(2, id);
            } else {
                sql = "UPDATE employees SET salary = ? WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setDouble(1, newSalary);
                stmt.setInt(2, id);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No employee found with that ID.");
            }

            stmt.close();
            conn.close();
        } catch (Exception e)
        {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }
}


