package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerController {

    // Method to insert a Customer
    public void insert( String name, String address, long phoneNumber, String email) throws SQLException {
        String insertCustomerSql = "INSERT INTO Customer (CustomerName, CustomerAddress) VALUES (?, ?)";
        String insertEmailSql = "INSERT INTO Customer_Email (CustomerID, CustomerEmail) VALUES (?, ?)";
        String insertPhoneSql = "INSERT INTO Customer_Number (CustomerID, CustomerNumber) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement insertCustomerStmt = conn.prepareStatement(insertCustomerSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertEmailStmt = conn.prepareStatement(insertEmailSql);
             PreparedStatement insertPhoneStmt = conn.prepareStatement(insertPhoneSql)) {

            conn.setAutoCommit(false);  // Start transaction

            insertCustomerStmt.setString(1, name);
            insertCustomerStmt.setString(2, address);
            insertCustomerStmt.executeUpdate();

            ResultSet generatedKeys = insertCustomerStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int customerId = generatedKeys.getInt(1);

                insertEmailStmt.setInt(1, customerId);
                insertEmailStmt.setString(2, email);
                insertEmailStmt.executeUpdate();

                insertPhoneStmt.setInt(1, customerId);
                insertPhoneStmt.setLong(2, phoneNumber);
                insertPhoneStmt.executeUpdate();

                conn.commit();  // Commit transaction
                System.out.println("Customer inserted successfully.");
            } else {
                conn.rollback();  // Rollback if no ID was obtained
                throw new SQLException("Inserting customer failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int customerId, String name, String address, long phoneNumber, String email) throws SQLException {
        String updateCustomerSql = "UPDATE Customer SET CustomerName = ?, CustomerAddress = ? WHERE CustomerID = ?";
        String updateEmailSql = "UPDATE Customer_Email SET CustomerEmail = ? WHERE CustomerID = ?";
        String updatePhoneSql = "UPDATE Customer_Number SET CustomerNumber = ? WHERE CustomerID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement updateCustomerStmt = conn.prepareStatement(updateCustomerSql);
             PreparedStatement updateEmailStmt = conn.prepareStatement(updateEmailSql);
             PreparedStatement updatePhoneStmt = conn.prepareStatement(updatePhoneSql)) {

            conn.setAutoCommit(false);  // Start transaction

            // Update customer information
            updateCustomerStmt.setString(1, name);
            updateCustomerStmt.setString(2, address);
            updateCustomerStmt.setInt(3, customerId);
            updateCustomerStmt.executeUpdate();

            // Update email
            updateEmailStmt.setString(1, email);
            updateEmailStmt.setInt(2, customerId);
            updateEmailStmt.executeUpdate();

            // Update phone number
            updatePhoneStmt.setLong(1, phoneNumber);
            updatePhoneStmt.setInt(2, customerId);
            updatePhoneStmt.executeUpdate();

            conn.commit();  // Commit transaction
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error occurred while updating the customer.", e);
        }
    }


    // Method to delete a Customer
    public void deleteCustomer(int customerId) throws SQLException {
        String deleteCustomerSql = "DELETE FROM Customer WHERE CustomerID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement deleteCustomerStmt = conn.prepareStatement(deleteCustomerSql)) {

            deleteCustomerStmt.setInt(1, customerId);
            deleteCustomerStmt.executeUpdate();

            System.out.println("Customer and its details deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to select a Customer
    public Customer select(int customerId) throws SQLException {
        String selectSql = "SELECT c.CustomerID, c.CustomerName, c.CustomerAddress, e.CustomerEmail, n.CustomerNumber " +
                           "FROM Customer c " +
                           "LEFT JOIN Customer_Email e ON c.CustomerID = e.CustomerID " +
                           "LEFT JOIN Customer_Number n ON c.CustomerID = n.CustomerID " +
                           "WHERE c.CustomerID = ?";
        Customer customer = null;

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, customerId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
            	int customerIdFromDb = rs.getInt("CustomerID");
            	String customerName = rs.getString("CustomerName");
            	String customerAddress = rs.getString("CustomerAddress");
            	String customerEmail = rs.getString("CustomerEmail");
            	long customerPhoneNumber = rs.getInt("CustomerNumber");

                customer = new Customer( customerIdFromDb,customerName,customerEmail,customerPhoneNumber, customerAddress);
            } else {
                System.out.println("Customer with ID " + customerId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}
