package application;

import java.sql.*;
import java.time.LocalDateTime;

public class DiscountController {


	    // Method to insert a new discount into the database
	    public boolean insertDiscount(Discount discount) {
	        String insertQuery = "INSERT INTO Discount (DiscountPercentage, StartDate, EndDate) VALUES (?, ?, ?)";

	        try (Connection connection = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
	             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

	            // Set the parameters in the prepared statement
	            preparedStatement.setFloat(1, discount.getDiscountPercentage());
	            preparedStatement.setTimestamp(2, Timestamp.valueOf(discount.getStartDate()));
	            preparedStatement.setTimestamp(3, Timestamp.valueOf(discount.getEndDate()));

	            int rowsAffected = preparedStatement.executeUpdate();
	            return rowsAffected > 0; // Return true if insertion was successful

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; // Return false if insertion failed
	        }
	    }

    // Method to select a discount from the database by ID
    public Discount select(int discountId) throws SQLException {
        String sql = "SELECT * FROM Discount WHERE DiscountID = ?";
        Discount discount = null;

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, discountId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int discountIdFromDB = rs.getInt("DiscountID");
                        float discountPercentage = rs.getFloat("DiscountPercentage");
                        LocalDateTime startDate = rs.getTimestamp("StartDate").toLocalDateTime();
                        LocalDateTime endDate = rs.getTimestamp("EndDate").toLocalDateTime();

                        discount = new Discount(discountIdFromDB, discountPercentage, startDate, endDate);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discount;
    }


    public boolean update(Discount discount) throws SQLException {
        if (discount.getDiscountPercentage() < 0 || discount.getDiscountPercentage() > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }

        String sql = "UPDATE Discount SET DiscountPercentage = ?, StartDate = ?, EndDate = ? WHERE DiscountID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setFloat(1, discount.getDiscountPercentage());
                pstmt.setTimestamp(2, Timestamp.valueOf(discount.getStartDate()));
                pstmt.setTimestamp(3, Timestamp.valueOf(discount.getEndDate()));
                pstmt.setInt(4, discount.getDiscountID());

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    
      // Method to delete a discount in the database
        public boolean delete(int discountId) throws SQLException {
            String sql = "DELETE FROM Discount WHERE DiscountID = ?";

            try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, discountId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Discount deleted successfully.");
                        return true; // Return true if delete was successful
                    } else {
                        System.out.println("No discount found with the given ID.");
                        return false; // Return false if no rows were affected
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error occurred while deleting the discount: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }