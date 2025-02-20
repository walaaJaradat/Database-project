package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuppliersController {

 
    public void insertSupplier(String name, String email, long phoneNumber) throws SQLException {
        String insertSupplierSql = "INSERT INTO Suppliers (SuppliersName) VALUES (?)";
        String insertEmailSql = "INSERT INTO Supplier_Email (Suppliers_ID, SuppliersEmail) VALUES (?, ?)";
        String insertPhoneSql = "INSERT INTO Supplier_Number (Suppliers_ID, SupplierNumber) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);

             PreparedStatement insertSupplierStmt = conn.prepareStatement(insertSupplierSql, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertEmailStmt = conn.prepareStatement(insertEmailSql);
             PreparedStatement insertPhoneStmt = conn.prepareStatement(insertPhoneSql)) {

            conn.setAutoCommit(false);  

            insertSupplierStmt.setString(1, name);
            insertSupplierStmt.executeUpdate();

            ResultSet generatedKeys = insertSupplierStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int supplierId = generatedKeys.getInt(1);

                insertEmailStmt.setInt(1, supplierId);
                insertEmailStmt.setString(2, email);
                insertEmailStmt.executeUpdate();

                insertPhoneStmt.setInt(1, supplierId);
                insertPhoneStmt.setLong(2, phoneNumber);
                insertPhoneStmt.executeUpdate();

                conn.commit();  
            } else {
                conn.rollback();  
                throw new SQLException("Inserting supplier failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSupplier(int supplierId, String name, String email, long phoneNumber) throws SQLException {
        String updateSupplierSql = "UPDATE Suppliers SET SuppliersName = ? WHERE Suppliers_ID = ?";
        String updateEmailSql = "UPDATE Supplier_Email SET SuppliersEmail = ? WHERE Suppliers_ID = ?";
        String updatePhoneSql = "UPDATE Supplier_Number SET SupplierNumber = ? WHERE Suppliers_ID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement updateSupplierStmt = conn.prepareStatement(updateSupplierSql);
             PreparedStatement updateEmailStmt = conn.prepareStatement(updateEmailSql);
             PreparedStatement updatePhoneStmt = conn.prepareStatement(updatePhoneSql)) {

            conn.setAutoCommit(false); 

            updateSupplierStmt.setString(1, name);
            updateSupplierStmt.setInt(2, supplierId);
            updateSupplierStmt.executeUpdate();

            updateEmailStmt.setString(1, email);
            updateEmailStmt.setInt(2, supplierId);
            updateEmailStmt.executeUpdate();

            updatePhoneStmt.setLong(1, phoneNumber);
            updatePhoneStmt.setInt(2, supplierId);
            updatePhoneStmt.executeUpdate();

            conn.commit();  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(int supplierId) throws SQLException {
        String deleteSupplierSql = "DELETE FROM Suppliers WHERE Suppliers_ID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement deleteSupplierStmt = conn.prepareStatement(deleteSupplierSql)) {

            deleteSupplierStmt.setInt(1, supplierId);
            deleteSupplierStmt.executeUpdate();

            System.out.println("Supplier and its details deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Suppliers selectSupplier(int supplierId) throws SQLException {
        String selectSql = "SELECT s.Suppliers_ID, s.SuppliersName, e.SuppliersEmail, n.SupplierNumber " +
                           "FROM Suppliers s " +
                           "LEFT JOIN Supplier_Email e ON s.Suppliers_ID = e.Suppliers_ID " +
                           "LEFT JOIN Supplier_Number n ON s.Suppliers_ID = n.Suppliers_ID " +
                           "WHERE s.Suppliers_ID = ?";
        Suppliers Suppliers = null;

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {

            selectStmt.setInt(1, supplierId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int Supplierid= rs.getInt("Suppliers_ID");
               String SupplierName= rs.getString("SuppliersName");
              String  SupplierEmail = rs.getString("SuppliersEmail");
              String  SupplierNumber  = rs.getString("SupplierNumber");
              Suppliers = new Suppliers(Supplierid,SupplierName,SupplierNumber,SupplierEmail
                      );
            } else {
                System.out.println("Supplier with ID " + supplierId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return Suppliers;
    }
}