package application;

import java.sql.*;

public class OrdersController {


	// Method to insert a new order into the database
	public void insertOrder(Orders order) {
	    String insertQuery = "INSERT INTO Orders (Customer_ID, Price, Order_Date, DiscountID, OriginalPrice, DiscountRate, CostPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection connection = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

	        // Set the parameters in the prepared statement
	        preparedStatement.setInt(1, order.getCustomerId());
	        preparedStatement.setDouble(2, order.getPrice());
	        preparedStatement.setDate(3, Date.valueOf(order.getOrderDate()));  
	        
	        if (order.getDiscountId() != null) {
	            preparedStatement.setInt(4, order.getDiscountId());
	        } else {
	            preparedStatement.setNull(4, Types.INTEGER);
	        }

	        preparedStatement.setDouble(5, order.getOriginalPrice());
	        preparedStatement.setDouble(6, order.getDiscountRate());
	        preparedStatement.setDouble(7, order.getCostPrice());

	        preparedStatement.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	// Method to select an order from the database by ID
	public Orders select(int orderId) throws SQLException {
	    String sql = "SELECT * FROM Orders WHERE Order_ID = ?";
	    Orders order = null;

	    try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, orderId);

	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    int orderIdFromDB = rs.getInt("Order_ID");
	                    Date orderDate = rs.getDate("Order_Date");
	                    double price = rs.getDouble("Price");
	                    Integer discountId = rs.getObject("DiscountID") != null ? rs.getInt("DiscountID") : null;
	                    double originalPrice = rs.getDouble("OriginalPrice");
	                    double discountRate = rs.getDouble("DiscountRate");
	                    double costPrice = rs.getDouble("CostPrice");

	                    order = new Orders(orderIdFromDB, rs.getInt("Customer_ID"), price, orderDate.toLocalDate(), discountId,
	                            originalPrice, discountRate, costPrice);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return order;
	}

	// Method to update an order in the database
	public void update(Orders order) throws SQLException {
	    String sql = "UPDATE Orders SET Order_Date = ?, Price = ?, DiscountID = ?, OriginalPrice = ?, DiscountRate = ?, CostPrice = ? WHERE Order_ID = ?";

	    try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setDate(1, Date.valueOf(order.getOrderDate()));  
	            pstmt.setDouble(2, order.getPrice());  
	            if (order.getDiscountId() != null) {
	                pstmt.setInt(3, order.getDiscountId()); 
	            } else {
	                pstmt.setNull(3, java.sql.Types.INTEGER);  
	            }
	            pstmt.setDouble(4, order.getOriginalPrice());
	            pstmt.setDouble(5, order.getDiscountRate());
	            pstmt.setDouble(6, order.getCostPrice());
	            pstmt.setInt(7, order.getOrderId());  

	            pstmt.executeUpdate();
	            System.out.println("Order updated successfully.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

    
    
    // Method to delete an order in the database
    public void delete(int orderId) throws SQLException {
        String sql = "DELETE FROM Orders WHERE Order_ID = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, orderId);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Order deleted successfully.");
                } else {
                    System.out.println("No order found with the given ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error occurred while deleting the order: " + e.getMessage());
            e.printStackTrace();
            throw e; 
        }
    }

}
