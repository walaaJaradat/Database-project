package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductController {

	// Method to select a product from the database by ID
	public Product select(int productId) {
	    Product product = null; 
	    String query = "SELECT p.Product_ID, p.Product_Name, p.Price, p.Category, p.Description, "
	                 + "p.In_Out_Stock, pd.Size, pd.Color, pd.Quantity "
	                 + "FROM Product p "
	                 + "LEFT JOIN Product_Details pd ON p.Product_ID = pd.Product_ID "
	                 + "WHERE p.Product_ID = ?"; 

	    try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	       
	        stmt.setInt(1, productId);  
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            product = new Product(
	                rs.getInt("Product_ID"),
	                rs.getString("Product_Name"),
	                rs.getDouble("Price"),
	                rs.getString("Category"),
	                rs.getString("Description"),
	                rs.getInt("Quantity"),
	                rs.getString("In_Out_Stock"),
	                rs.getString("Size"),  
	                rs.getString("Color")   
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return product; 
	}

		// Method to update an existing product in the database
		  public void update(Product product) throws SQLException {
		      String updateProductSql = "UPDATE Product SET Product_Name = ?, Description = ?, Category = ?, Price = ?, In_Out_Stock = ? WHERE Product_ID = ?";
		      String updateProductDetailsSql = "UPDATE Product_Details SET Color = ?, Size = ?, Quantity = ? WHERE Product_ID = ?";

		      try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {
		          conn.setAutoCommit(false); // Start transaction

		          try {
		              // Update Product table
		              try (PreparedStatement pstmt = conn.prepareStatement(updateProductSql)) {
		                  pstmt.setString(1, product.getName().get());
		                  pstmt.setString(2, product.getDescription().get());
		                  pstmt.setString(3, product.getCategory().get());
		                  pstmt.setDouble(4, product.getPrice().get());
		                  pstmt.setString(5, product.getIn_out_stock().get());
		                  pstmt.setInt(6, product.getProduct_id().get());

		                  pstmt.executeUpdate();
		              }

		              // Update Product_Details table
		              try (PreparedStatement pstmt = conn.prepareStatement(updateProductDetailsSql)) {
		                  pstmt.setString(1, product.getColor().get());
		                  pstmt.setString(2, product.getSize().get());
		                  pstmt.setInt(3, product.getStock_quantity().get());
		                  pstmt.setInt(4, product.getProduct_id().get());

		                  pstmt.executeUpdate();
		              }

		              conn.commit(); 
		              System.out.println("Product updated successfully.");
		          } catch (SQLException e) {
		              conn.rollback(); 
		              e.printStackTrace();
		              throw e;
		          }
		      }
		  }

		// Method to delete a product in the database
		  public void delete(int id) throws SQLException {
		      String deleteProductSql = "DELETE FROM Product WHERE Product_ID = ?";

		      try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
		           PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductSql)) {

		          conn.setAutoCommit(false);  

		          deleteProductStmt.setInt(1, id);
		          deleteProductStmt.executeUpdate();

		          conn.commit();  

		          System.out.println("Product and its details deleted successfully.");
		      } catch (SQLException e) {
		          e.printStackTrace();
		      }
		  }

 // Method to insert a new product in the database
    public void insert(Product product) throws SQLException {
        String insertProductSql = "INSERT INTO Product (Product_Name, Description, Category, Price, In_Out_Stock) VALUES (?, ?, ?, ?, ?)";
        String insertProductDetailsSql = "INSERT INTO Product_Details (Product_ID, Color, Size, Quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD)) {
            conn.setAutoCommit(false); 

            try {
                // Insert into Product table
                int productId;
                try (PreparedStatement pstmt = conn.prepareStatement(insertProductSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, product.getName().get());
                    pstmt.setString(2, product.getDescription().get());
                    pstmt.setString(3, product.getCategory().get());
                    pstmt.setDouble(4, product.getPrice().get());
                    pstmt.setString(5, product.getIn_out_stock().get());

                    pstmt.executeUpdate();

                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            productId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve Product_ID.");
                        }
                    }
                }

                // Insert into Product_Details table
                try (PreparedStatement pstmt = conn.prepareStatement(insertProductDetailsSql)) {
                    pstmt.setInt(1, productId);
                    pstmt.setString(2, product.getColor().get());
                    pstmt.setString(3, product.getSize().get());
                    pstmt.setInt(4, product.getStock_quantity().get());

                    pstmt.executeUpdate();
                }
                conn.commit();
                System.out.println("Product inserted successfully.");
            } catch (SQLException e) {
                conn.rollback(); 
                e.printStackTrace();
                throw e;
            }
        }
    }
}
