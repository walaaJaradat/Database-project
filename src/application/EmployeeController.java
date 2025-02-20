package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeController {

    // Method to insert a new employee
    public void insertEmployee(String name, String password, double salary, String status, String email, long phoneNumber) throws SQLException {
        String insertEmployeeSql = "INSERT INTO Employee (EmployeeName, Password, Salary, Status) VALUES (?, ?, ?, ?)";
        String insertEmailSql = "INSERT INTO Employee_Email (Employee_Id, EmployeeEmail) VALUES (?, ?)";
        String insertPhoneSql = "INSERT INTO Employee_Number (Employee_Id, EmployeeNumber) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement insertEmployeeStmt = null;
        PreparedStatement insertEmailStmt = null;
        PreparedStatement insertPhoneStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
            conn.setAutoCommit(false);  

            // Insert into Employee table
            insertEmployeeStmt = conn.prepareStatement(insertEmployeeSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertEmployeeStmt.setString(1, name);
            insertEmployeeStmt.setString(2, password);
            insertEmployeeStmt.setDouble(3, salary);
            insertEmployeeStmt.setString(4, status);
            insertEmployeeStmt.executeUpdate();

            generatedKeys = insertEmployeeStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int employeeId = generatedKeys.getInt(1);

                // Insert into Employee_Email table
                insertEmailStmt = conn.prepareStatement(insertEmailSql);
                insertEmailStmt.setInt(1, employeeId);
                insertEmailStmt.setString(2, email);
                insertEmailStmt.executeUpdate();

                // Insert into Employee_Number table
                insertPhoneStmt = conn.prepareStatement(insertPhoneSql);
                insertPhoneStmt.setInt(1, employeeId);
                insertPhoneStmt.setLong(2, phoneNumber);
                insertPhoneStmt.executeUpdate();

                conn.commit();  
            } else {
                conn.rollback(); 
                throw new SQLException("Inserting employee failed, no ID obtained.");
            }
        } catch (SQLException e) {
            if (conn != null) conn.rollback();  
            e.printStackTrace();
            throw e;  // Re-throw the exception to handle it in the calling method
        } finally {
            // Close resources
            if (generatedKeys != null) generatedKeys.close();
            if (insertPhoneStmt != null) insertPhoneStmt.close();
            if (insertEmailStmt != null) insertEmailStmt.close();
            if (insertEmployeeStmt != null) insertEmployeeStmt.close();
            if (conn != null) conn.close();
        }
    }

    public void updateEmployee(int employeeId, String name, String password, double salary, String status, String email, long phoneNumber) throws SQLException {
        String updateEmployeeSql = "UPDATE Employee SET EmployeeName = ?, Password = ?, Salary = ?, Status = ? WHERE Employee_Id = ?";
        String updateEmailSql = "UPDATE Employee_Email SET EmployeeEmail = ? WHERE Employee_Id = ?";
        String updatePhoneSql = "UPDATE Employee_Number SET EmployeeNumber = ? WHERE Employee_Id = ?";
        String insertPhoneSql = "INSERT INTO Employee_Number (Employee_Id, EmployeeNumber) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement updateEmployeeStmt = null;
        PreparedStatement updateEmailStmt = null;
        PreparedStatement updatePhoneStmt = null;
        PreparedStatement insertPhoneStmt = null;

        try {
            conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
            conn.setAutoCommit(false);

            updateEmployeeStmt = conn.prepareStatement(updateEmployeeSql);
            updateEmployeeStmt.setString(1, name);
            updateEmployeeStmt.setString(2, password);
            updateEmployeeStmt.setDouble(3, salary);
            updateEmployeeStmt.setString(4, status);
            updateEmployeeStmt.setInt(5, employeeId);
            updateEmployeeStmt.executeUpdate();

            updateEmailStmt = conn.prepareStatement(updateEmailSql);
            updateEmailStmt.setString(1, email);
            updateEmailStmt.setInt(2, employeeId);
            updateEmailStmt.executeUpdate();

            if (phoneNumber != 0) {
                updatePhoneStmt = conn.prepareStatement(updatePhoneSql);
                updatePhoneStmt.setLong(1, phoneNumber);
                updatePhoneStmt.setInt(2, employeeId);
                int rowsUpdated = updatePhoneStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    insertPhoneStmt = conn.prepareStatement(insertPhoneSql);
                    insertPhoneStmt.setInt(1, employeeId);
                    insertPhoneStmt.setLong(2, phoneNumber);
                    insertPhoneStmt.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (updatePhoneStmt != null) updatePhoneStmt.close();
            if (insertPhoneStmt != null) insertPhoneStmt.close();
            if (updateEmailStmt != null) updateEmailStmt.close();
            if (updateEmployeeStmt != null) updateEmployeeStmt.close();
            if (conn != null) conn.close();
        }
    }


    // Method to delete an employee
    public void deleteEmployee(int employeeId) throws SQLException {
        String deleteEmployeeSql = "DELETE FROM Employee WHERE Employee_Id = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement deleteEmployeeStmt = conn.prepareStatement(deleteEmployeeSql)) {

            deleteEmployeeStmt.setInt(1, employeeId);
            deleteEmployeeStmt.executeUpdate();

            System.out.println("Employee and its details deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  
        }
    }

    // Method to select an employee
    public Employee selectEmployee(int employeeId) throws SQLException {
        String selectSql = "SELECT e.Employee_Id, e.EmployeeName, e.Salary, e.Status, em.EmployeeEmail, n.EmployeeNumber, e.Password " +
                           "FROM Employee e " +
                           "LEFT JOIN Employee_Email em ON e.Employee_Id = em.Employee_Id " +
                           "LEFT JOIN Employee_Number n ON e.Employee_Id = n.Employee_Id " +
                           "WHERE e.Employee_Id = ?";
        Employee employee = null;

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, employeeId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("Employee_Id");
                String name = rs.getString("EmployeeName");
                Double salary = rs.getDouble("Salary");
                String status = rs.getString("Status");
                String email = rs.getString("EmployeeEmail");
                String number = rs.getString("EmployeeNumber");
                String password = rs.getString("Password");

                employee = new Employee(id, name, number, email, password, status, salary);
            } else {
                System.out.println("Employee with ID " + employeeId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  
        }
        return employee;
    }
}