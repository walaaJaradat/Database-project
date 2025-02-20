package application;


import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.DatePicker;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class DatabaseTwista extends Application {

	 
	ObservableList<Product> ProductobservableList1 = FXCollections.observableArrayList();
	ObservableList<Suppliers> SuppliersobservableList = FXCollections.observableArrayList();
	ObservableList<Employee> EmployeeobservableList = FXCollections.observableArrayList();
	ObservableList<Orders> OrdersobservableList = FXCollections.observableArrayList();
	ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
	ObservableList<Discount> 	DiscountObservableList= FXCollections.observableArrayList();
	private VBox reviewsContainer;
	private VBox archivedReviewsContainer;
	private List<HBox> archivedReviews;
	private Map<HBox, Integer> reviewPositions;

	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/project";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "MyPassword123!";

	//method to create connection with database
	public void createConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			System.out.println("Connected to the database successfully!");

		} catch (SQLException e) {
			System.out.println("Database connection error.");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found. Please add the JDBC library to your project.");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
					System.out.println("Database connection closed.");
				} catch (SQLException e) {
					System.out.println("Error while closing the database connection.");
					e.printStackTrace();
				}
			}
		}
	}
	//*************************************************************************************************************
	public static void applyDarkMode(Scene scene, ToggleButton toggleDarkMode) {
	    if (DarkModeState.isDarkMode) {
	        scene.getStylesheets().clear();
	        scene.getStylesheets().add(DatabaseTwista.class.getResource("dark-mode.css").toExternalForm());
	        toggleDarkMode.setText("Disable Dark Mode");
	        toggleDarkMode.setSelected(true);
	    } else {
	        scene.getStylesheets().clear();
	       scene.getStylesheets().add(DatabaseTwista.class.getResource("light-mode.css").toExternalForm());

	        toggleDarkMode.setText("Enable Dark Mode");
	        toggleDarkMode.setSelected(false);
	    }
	}
	
	@Override
	public void start(Stage primaryStage) {
	    try {
	        createConnection();

	        Image image = new Image("file:/C:/Users/user/Desktop/Twista logo.PNg");
	        ImageView imageView = new ImageView(image);
	        imageView.setFitWidth(800);
	        imageView.setPreserveRatio(true);

	        Label lb = new Label("LOG IN");
	        lb.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

	        Label lb2 = new Label("Email address");
	        lb2.setStyle("-fx-font-size: 18px;");
	        Label lb3 = new Label("Password");
	        lb3.setStyle("-fx-font-size: 18px;");

	        TextField TF1 = new TextField();
	        TF1.setPrefWidth(250);
	        PasswordField TF2 = new PasswordField();
	        TF2.setPrefWidth(250);

	        HBox hbox1 = new HBox(10, lb2, TF1);
	        HBox hbox2 = new HBox(30, lb3, TF2);

	        Line line = new Line();
	        line.setStartX(0);
	        line.setEndX(400);
	        line.setStyle("-fx-stroke: gray; -fx-stroke-width: 2;");

	        Button b1 = new Button("      LOG IN       ");
	        b1.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;  ");
	        b1.setPrefWidth(200);
	        Button b2 = new Button("    SIGN UP       ");
	        b2.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;  ");
	        b2.setPrefWidth(200);
	        Button b3 = new Button("     Cancel          ");
	        b3.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; ");
	        b3.setOnAction(e -> primaryStage.close());
	        b3.setPrefWidth(200);

	        ToggleButton toggleDarkMode = new ToggleButton("Enable Dark Mode");
	        toggleDarkMode.setPrefWidth(200);

	        VBox vboxButtons = new VBox(10, b1, b2, b3, toggleDarkMode);
	        vboxButtons.setAlignment(Pos.CENTER_LEFT);
	        vboxButtons.setStyle("-fx-padding: 30 0 0 90;");

	        b1.setOnAction(e -> {
	            String email = TF1.getText();
	            String password = TF2.getText();

	            String status = validateLogin(email, password);

	            if (status != null) {
	                if (status.equalsIgnoreCase("manager")) {
	                    Dashboard(primaryStage);
	                } else if (status.equalsIgnoreCase("employee")) {
	                    Dashboard2(primaryStage);
	                }
	            } else {
	                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Credentials", "The email or password you entered is incorrect.");
	            }
	        });

	        b2.setOnAction(e -> signUp());

	        VBox vbox = new VBox(20, lb, line, hbox1, hbox2, vboxButtons);
	        HBox hboxMain = new HBox(20, imageView, vbox);

	        Scene scene = new Scene(hboxMain, 1200, 600);


	        applyDarkMode(scene, toggleDarkMode);

	        toggleDarkMode.setOnAction(e -> {
	            DarkModeState.isDarkMode = toggleDarkMode.isSelected();
	            applyDarkMode(scene, toggleDarkMode);
	        });

	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Twista Store");
	        primaryStage.show();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	//****************************************************************************************************************
	// Method to validate login credentials
	private String validateLogin(String email, String password) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String status = null;

		try {
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
			String query = "SELECT e.Status FROM Employee e " +
					"JOIN Employee_Email ee ON e.Employee_Id = ee.Employee_Id " +
					"WHERE ee.EmployeeEmail = ? AND e.Password = ?";
			ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);

			rs = ps.executeQuery();
			if (rs.next()) {
				status = rs.getString("Status"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status; 
	}
	private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	private void signUp( ) {
		Stage stage = new Stage ();
		Label managerLoginLabel = new Label("MANAGER LOGIN");
		managerLoginLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

		Label managerEmailLabel = new Label("Manager Email");
		managerEmailLabel.setStyle("-fx-font-size: 18px;");
		Label managerPasswordLabel = new Label("Manager Password");
		managerPasswordLabel.setStyle("-fx-font-size: 18px;");

		TextField managerEmailField = new TextField();
		managerEmailField.setPrefWidth(250);
		PasswordField managerPasswordField = new PasswordField();
		managerPasswordField.setPrefWidth(250);

		Button nextButton = new Button("NEXT");
		nextButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #00008B; -fx-text-fill: white;");
		nextButton.setPrefWidth(200);


		VBox managerForm = new VBox(20, managerLoginLabel, managerEmailLabel, managerEmailField,
				managerPasswordLabel, managerPasswordField, nextButton);
		managerForm.setStyle("-fx-padding: 30;");
		managerForm.setAlignment(Pos.CENTER);

		Scene managerScene = new Scene(managerForm, 500, 500);

		nextButton.setOnAction(e -> {
			String email = managerEmailField.getText();
			String password = managerPasswordField.getText();

			if (authenticateManager(email, password)) {
				showEmployeeSignUpScreen(stage);
			} else {
				showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid Manager Credentials", "Please enter valid manager credentials.");
			}
		});

		applyTheme(managerScene); 

		stage.setScene(managerScene);
		stage.setTitle("Manager Login");
		stage.show();
	}

	private void showEmployeeSignUpScreen(Stage primaryStage) {
		Label lb = new Label("EMPLOYEE SIGN UP");
		lb.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

		Label lb1 = new Label("Full Name");
		lb1.setStyle("-fx-font-size: 18px;");
		Label lb2 = new Label("Email Address");
		lb2.setStyle("-fx-font-size: 18px;");
		Label lb3 = new Label("Password");
		lb3.setStyle("-fx-font-size: 18px;");
		Label lb4 = new Label("Confirm Password");
		lb4.setStyle("-fx-font-size: 18px;");

		TextField TF1 = new TextField();
		TF1.setPrefWidth(250);
		TextField TF2 = new TextField();
		TF2.setPrefWidth(250);
		PasswordField PF1 = new PasswordField();
		PF1.setPrefWidth(250);
		PasswordField PF2 = new PasswordField();
		PF2.setPrefWidth(250);

		Button signUpButton = new Button("SIGN UP");
		signUpButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-color: #00008B; -fx-text-fill: white;");
		signUpButton.setPrefWidth(200);


		signUpButton.setOnAction(e -> {
			String fullName = TF1.getText();
			String email = TF2.getText();
			String password = PF1.getText();
			String confirmPassword = PF2.getText();

			if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
				showAlert(Alert.AlertType.ERROR, "Error", "Missing Fields", "Please fill in all fields.");
			} else if (!password.equals(confirmPassword)) {
				showAlert(Alert.AlertType.ERROR, "Error", "Password Mismatch", "The passwords do not match.");
			} else if (!isValidEmail(email)) {
				showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", "Please enter a valid email address.");
			} else {
				if (addUserToDatabase(fullName, email, password)) {
					showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created", "Employee account has been created successfully.");
					primaryStage.close();
					Dashboard2(primaryStage);
				} else {
					showAlert(Alert.AlertType.ERROR, "Error", "Account Creation Failed", "An error occurred while creating the account.");
				}
			}
		});


		VBox form = new VBox(20, lb, lb1, TF1, lb2, TF2, lb3, PF1, lb4, PF2, signUpButton);
		form.setStyle("-fx-padding: 30;");
		form.setAlignment(Pos.CENTER);

		Scene signUpScene = new Scene(form, 500, 500);
		applyTheme(signUpScene); 

		primaryStage.setScene(signUpScene);
		primaryStage.setTitle("Employee Sign Up");
		primaryStage.show();
	}

	private boolean authenticateManager(String email, String password) {
		return (email.equals("TuleenRimawi@gmail.com") && password.equals("tuleen3.3Rimawi")) ||
				(email.equals("WalaaJaradat@gmail.com") && password.equals("walaa1234>")) ||
				(email.equals("AbdallahHosheah@gmail.com") && password.equals("abdallah1234>"));
	}


	// Method to validate email format using regex
	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	private boolean addUserToDatabase(String fullName, String email, String password) {
		Connection connection = null;
		PreparedStatement ps1 = null, ps2 = null;
		ResultSet rs = null;
		boolean success = false;

		try {
			connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

			String checkQuery = "SELECT * FROM Employee_Email WHERE EmployeeEmail = ?";
			ps1 = connection.prepareStatement(checkQuery);
			ps1.setString(1, email);
			rs = ps1.executeQuery();

			if (rs.next()) {
				showAlert(Alert.AlertType.ERROR, "Error", "Email Exists", "This email is already registered.");
				return false;
			}

			String insertEmployeeQuery = "INSERT INTO Employee (EmployeeName, Password, Status) VALUES (?, ?, ?)";
			ps2 = connection.prepareStatement(insertEmployeeQuery, Statement.RETURN_GENERATED_KEYS);
			ps2.setString(1, fullName);
			ps2.setString(2, password);
			ps2.setString(3, "employee");
			ps2.executeUpdate();

			rs = ps2.getGeneratedKeys();
			if (rs.next()) {
				int employeeId = rs.getInt(1);

				String insertEmailQuery = "INSERT INTO Employee_Email (Employee_Id, EmployeeEmail) VALUES (?, ?)";
				ps2 = connection.prepareStatement(insertEmailQuery);
				ps2.setInt(1, employeeId);
				ps2.setString(2, email);
				ps2.executeUpdate();

				success = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps1 != null) ps1.close();
				if (ps2 != null) ps2.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}



	public void ProductInterface(Stage primaryStage) {

		TableView<Product> tableview = new TableView<>();

		TableColumn<Product, Integer> idColumn = new TableColumn<>("Product ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getProduct_id().asObject());

		TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

		TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPrice().asObject());

		TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
		categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategory());

		TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
		descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());

		TableColumn<Product, Integer> stockQuantityColumn = new TableColumn<>("Stock Quantity");
		stockQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().getStock_quantity().asObject());

		TableColumn<Product, String> colorColumn = new TableColumn<>("Color");
		colorColumn.setCellValueFactory(cellData -> cellData.getValue().getColor());

		TableColumn<Product, String> sizeColumn = new TableColumn<>("Size");
		sizeColumn.setCellValueFactory(cellData -> cellData.getValue().getSize());

		TableColumn<Product, String> inOutStockColumn = new TableColumn<>("In/Out Stock");
		inOutStockColumn.setCellValueFactory(cellData -> cellData.getValue().getIn_out_stock());

		tableview.getColumns().addAll(
				idColumn, nameColumn, priceColumn, categoryColumn, descriptionColumn,
				stockQuantityColumn, colorColumn, sizeColumn, inOutStockColumn
				);

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button deleteButton = createMenuButton("Delete");
			Button updateButton = createMenuButton("Update");
			Button Back = createMenuButton("Back");

			selectButton.setOnAction(e -> SelectProduct(primaryStage));
			deleteButton.setOnAction(e -> DeleteProduct(primaryStage));
			insertButton.setOnAction(e -> insertProduct(primaryStage));
			updateButton.setOnAction(e -> UpdateProduct(primaryStage));

			Back.setOnAction(e -> SecondInterface(primaryStage));

			vbox.getChildren().addAll(
					selectButton, insertButton, deleteButton,
					updateButton, Back
					);

			loadProduct(); 
			tableview.setItems(ProductobservableList1);

			BorderPane root = new BorderPane();
			root.setLeft(vbox);
			HBox hbox = new HBox(20, root, tableview);
			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene); 

			primaryStage.setScene(scene);
			primaryStage.setTitle("Product");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void loadProduct() {
		ProductobservableList1.clear();
		String query = "SELECT p.Product_ID, p.Product_Name, p.Price, p.Category, p.Description, " +
				"pd.Quantity, p.In_Out_Stock, pd.Size, pd.Color " +
				"FROM Product p " +
				"JOIN Product_Details pd ON p.Product_ID = pd.Product_ID";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				ProductobservableList1.add(new Product(
						rs.getInt("Product_ID"),
						rs.getString("Product_Name"),
						rs.getDouble("Price"),
						rs.getString("Category"),
						rs.getString("Description"),
						rs.getInt("Quantity"),
						rs.getString("In_Out_Stock"),
						rs.getString("Size"),
						rs.getString("Color")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void UpdateProduct(Stage primaryStage) {

		Label lb = new Label("Enter product ID to select");
		lb.setAlignment(Pos.CENTER);
		TextField productIdField = new TextField();
		productIdField.setPromptText("Product ID");

		Button selectButton = new Button("Select Product");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> ProductInterface(primaryStage));

		TextField productNameField = new TextField();
		productNameField.setPromptText("Name");

		TextField productPriceField = new TextField();
		productPriceField.setPromptText("Price");

		TextField productCategoryField = new TextField();
		productCategoryField.setPromptText("Category");

		TextField productDescriptionField = new TextField();
		productDescriptionField.setPromptText("Description");

		TextField productStockField = new TextField();
		productStockField.setPromptText("Quantity");

		TextField productSizeField = new TextField();
		productSizeField.setPromptText("Size");

		TextField productColorField = new TextField();
		productColorField.setPromptText("Color");

		TextField productInOutStockField = new TextField();
		productInOutStockField.setPromptText("In/Out Stock");




		Button updateButton = new Button("Update Product");
		updateButton.setDisable(true);

		HBox hbox = new HBox(5, updateButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, lb, productIdField, selectButton, productNameField, productPriceField, productCategoryField,
				productDescriptionField, productStockField, productSizeField, productColorField, productInOutStockField
				, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-border-radius: 15px;");
		vbox.setSpacing(5);

		selectButton.setOnAction(e -> {
			String productIdText = productIdField.getText();
			if (!productIdText.isEmpty()) {
				try {
					int productId = Integer.parseInt(productIdText);

					ProductController productController = new ProductController();
					Product product = productController.select(productId);

					if (product != null) {
						productNameField.setText(product.getName().get());
						productPriceField.setText(String.valueOf(product.getPrice().get()));
						productCategoryField.setText(product.getCategory().get());
						productDescriptionField.setText(product.getDescription().get());
						productStockField.setText(String.valueOf(product.getStock_quantity().get()));
						productSizeField.setText(product.getSize().get());
						productColorField.setText(product.getColor().get());
						productInOutStockField.setText(product.getIn_out_stock().get());

						updateButton.setDisable(false);
					} else {
						showAlert3("Product not found.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid Product ID.");
				}
			} else {
				showAlert1("Please enter a valid Product ID.");
			}
		});

		updateButton.setOnAction(e -> {
			try {
				int productId = Integer.parseInt(productIdField.getText());
				int quantity = Integer.parseInt(productStockField.getText());
				String inOutStock = productInOutStockField.getText();

				if (quantity == 0 && !inOutStock.equalsIgnoreCase("Out")) {
					showAlert1("Quantity is 0. Product must be 'Out of Stock'.");
					return; 
				}

				if (quantity > 0 && !inOutStock.equalsIgnoreCase("In")) {
					showAlert1("Quantity is greater than 0. Product must be 'In Stock'.");
					return; 
				}

				Product updatedProduct = new Product(
						productId, 
						productNameField.getText(),
						Double.parseDouble(productPriceField.getText()), 
						productCategoryField.getText(),
						productDescriptionField.getText(), 
						quantity,
						inOutStock, 
						productSizeField.getText(), 
						productColorField.getText()
						);

				ProductController productController = new ProductController();
				productController.update(updatedProduct);

				showAlert2("Product updated successfully.");

				productNameField.clear();
				productPriceField.clear();
				productCategoryField.clear();
				productDescriptionField.clear();
				productStockField.clear();
				productSizeField.clear();
				productColorField.clear();
				productInOutStockField.clear();

			} catch (NumberFormatException ex) {
				showAlert1("Please fill all fields correctly.");
			} catch (SQLException ex) {
				ex.printStackTrace();
				showAlert3("Error occurred while updating the product.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Update Product");

		primaryStage.show();
	}

	public void SelectProduct(Stage primaryStage) {

		Label label = new Label("Enter Product ID to View Details:");
		label.setStyle(" -fx-font-weight: bold;");

		TextField productIdField = new TextField();
		productIdField.setPromptText("Product ID");
		productIdField.setStyle(" -fx-padding: 10px;");

		Button selectButton = new Button("Select Product");
		selectButton.setStyle(" -fx-padding: 10px 20px;");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> ProductInterface(primaryStage));

		Label productDetailsLabel = new Label("Product Details:");
		productDetailsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		Label productNameLabel = new Label("Name: ");
		productNameLabel.setStyle(" -fx-font-weight: bold;");
		Label productPriceLabel = new Label("Price: ");
		productPriceLabel.setStyle(" -fx-font-weight: bold;");
		Label productCategoryLabel = new Label("Category: ");
		productCategoryLabel.setStyle(" -fx-font-weight: bold;");
		Label productDescriptionLabel = new Label("Description: ");
		productDescriptionLabel.setStyle(" -fx-font-weight: bold;");
		Label productStockLabel = new Label("Stock Quantity: ");
		productStockLabel.setStyle(" -fx-font-weight: bold;");
		Label productInOutStockLabel = new Label("In/Out Stock: ");
		productInOutStockLabel.setStyle(" -fx-font-weight: bold;");
		Label productSizeLabel = new Label("Size: ");
		productSizeLabel.setStyle(" -fx-font-weight: bold;");
		Label productColorLabel = new Label("Color: ");
		productColorLabel.setStyle(" -fx-font-weight: bold;");

		Label productNameValue = new Label();
		Label productPriceValue = new Label();
		Label productCategoryValue = new Label();
		Label productDescriptionValue = new Label();
		Label productStockValue = new Label();
		Label productInOutStockValue = new Label();
		Label productSizeValue = new Label();
		Label productColorValue = new Label();

		VBox vbox = new VBox(5, label, productIdField, selectButton, productDetailsLabel,
				productNameLabel, productNameValue, productPriceLabel, productPriceValue,
				productCategoryLabel, productCategoryValue, productDescriptionLabel, productDescriptionValue,
				productStockLabel, productStockValue, productInOutStockLabel, productInOutStockValue,
				productSizeLabel, productSizeValue, productColorLabel, productColorValue, backButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-padding: 30px; -fx-border-radius: 15px;");

		selectButton.setOnAction(e -> {
			String productIdText = productIdField.getText();
			if (!productIdText.isEmpty()) {
				try {
					int productId = Integer.parseInt(productIdText);

					ProductController productController = new ProductController();
					Product product = productController.select(productId);

					if (product != null) {
						productNameValue.setText(product.getName().get());
						productPriceValue.setText(product.getPrice().get()+"");
						productCategoryValue.setText(product.getCategory().get());
						productDescriptionValue.setText(product.getDescription().get());
						productStockValue.setText(product.getStock_quantity().get()+"");
						productInOutStockValue.setText(product.getIn_out_stock().get());
						productSizeValue.setText(product.getSize().get());
						productColorValue.setText(product.getColor().get());
					} else {
						productNameValue.setText("Product not found.");
						productPriceValue.setText("");
						productCategoryValue.setText("");
						productDescriptionValue.setText("");
						productStockValue.setText("");
						productInOutStockValue.setText("");
						productSizeValue.setText("");
						productColorValue.setText("");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid product ID.");
				}
			} else {
				showAlert1("Please enter a Product ID.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Select Product");

		primaryStage.show();
	}
	public void DeleteProduct(Stage primaryStage) {

		Label label = new Label("Enter the Product ID:");
		label.setStyle("-fx-font-weight: bold;-fx-padding: 10px;");

		TextField productIdField = new TextField();
		productIdField.setPromptText("Product ID");
		productIdField.setStyle("-fx-padding: 10px;");
		productIdField.setMaxWidth(200);  // Set a fixed width

		Button deleteButton = new Button("Delete");
		deleteButton.setStyle(
				"-fx-padding: 10px 20px;");

		Button backButton = new Button("Back");
		backButton.setStyle(
				"-fx-padding: 10px 20px;");
		backButton.setOnAction(e -> {
			ProductInterface(primaryStage);
		});

		Label feedbackLabel = new Label();
		feedbackLabel.setStyle("-fx-font-size: 12px;");

		deleteButton.setOnAction(e -> {
			String productIdText = productIdField.getText();

			if (!productIdText.isEmpty()) {
				try {
					int productId = Integer.parseInt(productIdText);
					ProductController controller = new ProductController();

					if (controller.select(productId) != null) {
						controller.delete(productId);
						showAlert2("Product with ID " + productId + " has been deleted.");
					} else {
						showAlert1("Product with ID " + productId + " does not exist.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Invalid Product ID. Please enter a valid number.");
				} catch (SQLException ex) {
					showAlert3("Error occurred while deleting the product: " + ex.getMessage());
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please enter a Product ID.");
			}
		});

		HBox buttonBox = new HBox(10, deleteButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(20, label, productIdField, buttonBox, feedbackLabel);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-padding: 30px; -fx-border-radius: 15px;");

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Delete Product");

		primaryStage.show();
	}
	public void insertProduct(Stage primaryStage) {

		Label titleLabel = new Label("Enter New Product Information");
		titleLabel.setStyle(" -fx-font-weight: bold;");

		Label nameLabel = new Label("Product Name:");
		TextField nameField = new TextField();
		nameField.setPromptText("Product Name");

		Label priceLabel = new Label("Price:");
		TextField priceField = new TextField();
		priceField.setPromptText("Price");

		Label categoryLabel = new Label("Category:");
		TextField categoryField = new TextField();
		categoryField.setPromptText("Category");

		Label descriptionLabel = new Label("Description:");
		TextField descriptionField = new TextField();
		descriptionField.setPromptText("Description");

		Label inOutStockLabel = new Label("In/Out of Stock:");
		ComboBox<String> inOutStockComboBox = new ComboBox<>();
		inOutStockComboBox.getItems().addAll("IN", "OUT");
		inOutStockComboBox.setPromptText("Select In/Out");
		inOutStockComboBox.setPrefWidth(1000);


		Label colorLabel = new Label("Color:");
		TextField colorField = new TextField();
		colorField.setPromptText("Color");

		Label sizeLabel = new Label("Size:");
		TextField sizeField = new TextField();
		sizeField.setPromptText("Size");

		Label quantityLabel = new Label("Quantity:");
		TextField quantityField = new TextField();
		quantityField.setPromptText("Quantity");

		Button insertButton = new Button("Insert Product");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			ProductInterface(primaryStage);
		});

		insertButton.setOnAction(e -> {
			String name = nameField.getText();
			String priceText = priceField.getText();
			String category = categoryField.getText();
			String description = descriptionField.getText();
			String inOutStock = inOutStockComboBox.getValue(); 
			String color = colorField.getText();
			String size = sizeField.getText();
			String quantityText = quantityField.getText();

			if (!name.isEmpty() && !priceText.isEmpty() && !category.isEmpty() && !description.isEmpty() &&
					inOutStock != null && !color.isEmpty() && !size.isEmpty() && !quantityText.isEmpty()) {
				try {
					double price = Double.parseDouble(priceText);
					int quantity = Integer.parseInt(quantityText);

					if (quantity == 0 && !inOutStock.equalsIgnoreCase("Out")) {
						showAlert1("Quantity is 0. Product must be 'Out of Stock'.");
						return; 
					}

					if (quantity > 0 && !inOutStock.equalsIgnoreCase("In")) {
						showAlert1("Quantity is greater than 0. Product must be 'In Stock'.");
						return; 
					}

					Product product = new Product(
							0, 
							name, 
							price, 
							category, 
							description, 
							quantity, 
							inOutStock, 
							size, 
							color
							);

					ProductController controller = new ProductController();
					controller.insert(product); 

					showAlert2("Product inserted successfully.");

					nameField.clear();
					priceField.clear();
					categoryField.clear();
					descriptionField.clear();
					inOutStockComboBox.getSelectionModel().clearSelection();
					colorField.clear();
					sizeField.clear();
					quantityField.clear();

				} catch (NumberFormatException ex) {
					showAlert1("Invalid number format for price or quantity.");
				} catch (SQLException ex) {
					showAlert3("Error occurred while inserting the product.");
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please fill in all fields.");
			}
		});

		HBox hbox = new HBox(5, insertButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(3, titleLabel, nameLabel, nameField, priceLabel, priceField, categoryLabel, categoryField,
				descriptionLabel, descriptionField, inOutStockLabel, inOutStockComboBox, colorLabel, colorField,
				sizeLabel, sizeField, quantityLabel, quantityField, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-padding: 30px; -fx-border-radius: 15px;");

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Insert Product");

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	public void SecondInterface(Stage primaryStage) {

		Dashboard(primaryStage);

	}

	// Method to create menu buttons
	private Button createMenuButton(String text) {
		Button button = new Button(text);
		button.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
		button.setMaxWidth(Double.MAX_VALUE);
		return button;
	}



	public void SuppliersInterface(Stage primaryStage) {

		TableView<Suppliers> tableview = new TableView<>();

		TableColumn<Suppliers, Integer> idColumn = new TableColumn<>("Supplier ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplier_id().asObject());

		TableColumn<Suppliers, String> nameColumn = new TableColumn<>("Supplier Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplier_name());

		TableColumn<Suppliers, String> numberColumn = new TableColumn<>("Number");
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplier_number());

		TableColumn<Suppliers, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getSupplier_email());

		tableview.getColumns().addAll(idColumn, nameColumn, numberColumn, emailColumn);

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button deleteButton = createMenuButton("Delete");
			Button updateButton = createMenuButton("Update");
			Button backButton = createMenuButton("Back");

			selectButton.setOnAction(e -> SelectSupplier(primaryStage));
			deleteButton.setOnAction(e -> deleteSupplier(primaryStage));
			insertButton.setOnAction(e -> insertSupplier(primaryStage));
			updateButton.setOnAction(e -> updateSupplier(primaryStage));
			backButton.setOnAction(e -> SecondInterface(primaryStage));

			vbox.getChildren().addAll(selectButton, insertButton, deleteButton, updateButton, backButton);

			loadSuppliers();
			tableview.setItems(SuppliersobservableList);

			VBox phoneNumberVBox = createAddPhoneNumberInterface$(primaryStage, tableview);
			VBox emailVBox = createAddEmailInterface$(primaryStage, tableview);

			TitledPane titledPane = new TitledPane("Add Details", new VBox(phoneNumberVBox, emailVBox));
			titledPane.setCollapsible(true);
			titledPane.setExpanded(false);
			Button showNumbersButton = new Button("Show Supplier Numbers");
			Button showEmailsButton = new Button("Show Supplier Emails");


			showNumbersButton.setOnAction(e -> {
				Suppliers selectedSupplier = tableview.getSelectionModel().getSelectedItem();
				if (selectedSupplier != null) {
					showSupplierNumbers(primaryStage, selectedSupplier);
				} else {
					showAlert1("Please select a supplier first.");
				}
			});

			showEmailsButton.setOnAction(e -> {
				Suppliers selectedSupplier = tableview.getSelectionModel().getSelectedItem();
				if (selectedSupplier != null) {
					showSupplierEmails(primaryStage, selectedSupplier);
				} else {
					showAlert1("Please select a supplier first.");
				}
			});

			VBox comboBoxVBox = new VBox(10, titledPane, showNumbersButton, showEmailsButton);
			comboBoxVBox.setAlignment(Pos.TOP_LEFT);

			BorderPane root = new BorderPane();
			root.setLeft(vbox);

			HBox hbox = new HBox(20, vbox, tableview, comboBoxVBox);
			HBox.setHgrow(tableview, Priority.ALWAYS);
			tableview.setPrefWidth(600);

			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene); 

			primaryStage.setScene(scene);
			primaryStage.setTitle("Suppliers");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private VBox createAddPhoneNumberInterface$(Stage primaryStage, TableView<Suppliers> tableview) {

		VBox vbox = new VBox(10);

		Label label = new Label("Add Phone Number");
		label.setStyle("-fx-font-weight: bold;");

		TextField phoneNumberField = new TextField();
		phoneNumberField.setPromptText("Phone Number");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String phoneNumberStr = phoneNumberField.getText();
			Suppliers selectedSupplier = tableview.getSelectionModel().getSelectedItem();
			if (selectedSupplier != null && !phoneNumberStr.isEmpty()) {
				try {
					addPhoneNumber(selectedSupplier.getSupplier_id().get(), phoneNumberStr);
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid phone number.");
				}
			} else {
				showAlert1("Please select a supplier and enter a phone number.");
			}
		});

		vbox.getChildren().addAll(label, phoneNumberField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}

	private VBox createAddEmailInterface$(Stage primaryStage, TableView<Suppliers> tableview) {
		VBox vbox = new VBox(10);

		Label label = new Label("Add Email");

		TextField emailField = new TextField();
		emailField.setPromptText("Email");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String email = emailField.getText();
			Suppliers selectedSupplier = tableview.getSelectionModel().getSelectedItem();
			if (selectedSupplier != null && !email.isEmpty()) {
				addEmail(selectedSupplier.getSupplier_id().get(), email);
			} else {
				showAlert1("Please select a supplier and enter an email.");
			}
		});

		vbox.getChildren().addAll(label, emailField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}
	private void addPhoneNumber(int supplierId, String phoneNumber) {
		String insertPhoneSql = "INSERT INTO Supplier_Number (Suppliers_ID, SupplierNumber) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertPhoneStmt = conn.prepareStatement(insertPhoneSql)) {

			insertPhoneStmt.setInt(1, supplierId);
			insertPhoneStmt.setString(2, phoneNumber);
			insertPhoneStmt.executeUpdate();

			showAlert2("Phone number added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding phone number.");
		}
	}

	private void addEmail(int supplierId, String email) {
		String insertEmailSql = "INSERT INTO Supplier_Email (Suppliers_ID, SuppliersEmail) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertEmailStmt = conn.prepareStatement(insertEmailSql)) {

			insertEmailStmt.setInt(1, supplierId);
			insertEmailStmt.setString(2, email);
			insertEmailStmt.executeUpdate();

			showAlert2("Email added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding email.");
		}
	}


	private void showSupplierNumbers(Stage primaryStage, Suppliers supplier) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Phone Numbers for Supplier " + supplier.getSupplier_name().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT SupplierNumber FROM Supplier_Number WHERE Suppliers_ID = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, supplier.getSupplier_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("SupplierNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error loading phone numbers.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Supplier Phone Numbers");

		stage.show();
	}

	private void showSupplierEmails(Stage primaryStage, Suppliers supplier) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Emails for Supplier " + supplier.getSupplier_name().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT SuppliersEmail FROM Supplier_Email WHERE Suppliers_ID = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, supplier.getSupplier_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("SuppliersEmail"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error loading emails.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Supplier Emails");

		stage.show();
	}

	public void EmployeeInterface(Stage primaryStage) {

		TableView<Employee> tableview = new TableView<>();

		TableColumn<Employee, Integer> idColumn = new TableColumn<>("Employee ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getEmployee_id().asObject());

		TableColumn<Employee, String> nameColumn = new TableColumn<>("Employee Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getEmployee_name());

		TableColumn<Employee, String> numberColumn = new TableColumn<>("Number");
		numberColumn.setCellValueFactory(cellData -> cellData.getValue().getEmployee_number());

		TableColumn<Employee, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmployee_Email());

		TableColumn<Employee, String> passwordColumn = new TableColumn<>("Password");
		passwordColumn.setCellValueFactory(cellData -> cellData.getValue().getPassword());

		TableColumn<Employee, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());

		TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Salary");
		salaryColumn.setCellValueFactory(cellData -> cellData.getValue().getSalary().asObject());

		tableview.getColumns().addAll(idColumn, nameColumn, numberColumn, emailColumn, passwordColumn, statusColumn, salaryColumn);

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button deleteButton = createMenuButton("Delete");
			Button updateButton = createMenuButton("Update");
			Button backButton = createMenuButton("Back");

			selectButton.setOnAction(e -> selectEmployee(primaryStage));
			deleteButton.setOnAction(e -> deleteEmployee(primaryStage));
			insertButton.setOnAction(e -> insertEmployee(primaryStage));
			updateButton.setOnAction(e -> updateEmployee(primaryStage));
			backButton.setOnAction(e -> SecondInterface(primaryStage));

			vbox.getChildren().addAll(selectButton, insertButton, deleteButton, updateButton, backButton);

			loadEmployees();
			tableview.setItems(EmployeeobservableList);

			VBox phoneNumberVBox = createAddPhoneNumberInterface(primaryStage, tableview);
			VBox emailVBox = createAddEmailInterface(primaryStage, tableview);

			TitledPane titledPane = new TitledPane("Add Details", new VBox(phoneNumberVBox, emailVBox));
			titledPane.setCollapsible(true);
			titledPane.setExpanded(false);

			Button showNumbersButton = new Button("Show Employee Numbers");
			showNumbersButton.setOnAction(e -> {
				Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
				if (selectedEmployee != null) {
					showEmployeeNumbers(primaryStage, selectedEmployee);
				} else {
					showAlert1("Please select an employee first.");
				}
			});

			Button showEmailsButton = new Button("Show Employee Emails");
			showEmailsButton.setOnAction(e -> {
				Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
				if (selectedEmployee != null) {
					showEmployeeEmails(primaryStage, selectedEmployee);
				} else {
					showAlert1("Please select an employee first.");
				}
			});

			VBox comboBoxVBox = new VBox(10, titledPane, showNumbersButton, showEmailsButton);
			comboBoxVBox.setAlignment(Pos.TOP_LEFT);

			BorderPane root = new BorderPane();
			root.setLeft(vbox);

			HBox hbox = new HBox(20, vbox, tableview, comboBoxVBox);
			HBox.setHgrow(tableview, Priority.ALWAYS);
			tableview.setPrefWidth(600);

			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene); 

			primaryStage.setScene(scene);
			primaryStage.setTitle("Employees");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VBox createAddPhoneNumberInterface(Stage primaryStage, TableView<Employee> tableview) {
		VBox vbox = new VBox(10);

		Label label = new Label("Add Phone Number");

		TextField phoneNumberField = new TextField();
		phoneNumberField.setPromptText("Phone Number");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String phoneNumberStr = phoneNumberField.getText();
			Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
			if (selectedEmployee != null && !phoneNumberStr.isEmpty()) {
				try {
					addPhoneNumber2(selectedEmployee.getEmployee_id().get(), phoneNumberStr);
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid phone number.");
				}
			} else {
				showAlert1("Please select an employee and enter a phone number.");
			}
		});

		vbox.getChildren().addAll(label, phoneNumberField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}

	private VBox createAddEmailInterface(Stage primaryStage, TableView<Employee> tableview) {
		VBox vbox = new VBox(10);

		Label label = new Label("Add Email");

		TextField emailField = new TextField();
		emailField.setPromptText("Email");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String email = emailField.getText();
			Employee selectedEmployee = tableview.getSelectionModel().getSelectedItem();
			if (selectedEmployee != null && !email.isEmpty()) {
				addEmail2(selectedEmployee.getEmployee_id().get(), email);
			} else {
				showAlert1("Please select an employee and enter an email.");
			}
		});

		vbox.getChildren().addAll(label, emailField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}
	private void addPhoneNumber2(int employeeId, String phoneNumber) {
		String insertPhoneSql = "INSERT INTO Employee_Number (Employee_Id, EmployeeNumber) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertPhoneStmt = conn.prepareStatement(insertPhoneSql)) {

			insertPhoneStmt.setInt(1, employeeId);
			insertPhoneStmt.setString(2, phoneNumber);
			insertPhoneStmt.executeUpdate();

			showAlert2("Phone number added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding phone number.");
		}
	}
	private void addEmail2(int employeeId, String email) {
		String insertEmailSql = "INSERT INTO Employee_Email (Employee_Id, EmployeeEmail) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertEmailStmt = conn.prepareStatement(insertEmailSql)) {

			insertEmailStmt.setInt(1, employeeId);
			insertEmailStmt.setString(2, email);
			insertEmailStmt.executeUpdate();

			showAlert2("Email added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding email.");
		}
	} 
	public void insertSupplier(Stage primaryStage) {


		Label titleLabel = new Label("Enter New Supplier Information");

		Label nameLabel = new Label("Supplier Name:");
		TextField nameField = new TextField();
		nameField.setPromptText("Supplier Name");

		Label numberLabel = new Label("Supplier Number:");
		TextField numberField = new TextField();
		numberField.setPromptText("Supplier Number");

		Label emailLabel = new Label("Supplier Email:");
		TextField emailField = new TextField();
		emailField.setPromptText("Supplier Email");

		Button insertButton = new Button("Insert Supplier");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			SuppliersInterface( primaryStage);
		});

		insertButton.setOnAction(e -> {
			String name = nameField.getText();
			String email = emailField.getText();
			String phoneNumberStr = numberField.getText();

			if (!name.isEmpty() && !email.isEmpty() && !phoneNumberStr.isEmpty()) {
				try {
					long phoneNumber = Long.parseLong(phoneNumberStr);
					SuppliersController controller = new SuppliersController();

					controller.insertSupplier(name, email, phoneNumber);
					showAlert2("Supplier inserted successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Invalid number format.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error occurred while inserting the Supplier.");
				}
			} else {
				showAlert1("Please fill in all fields.");
			}
		});

		HBox hbox = new HBox(5, insertButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10, titleLabel, nameLabel, nameField, numberLabel, numberField, emailLabel, emailField, hbox);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Insert Supplier");

		primaryStage.show();
	}
	public void deleteSupplier(Stage primaryStage) {

		Label label = new Label("Enter the Supplier ID:");

		TextField supplierIdField = new TextField();
		supplierIdField.setPromptText("Supplier ID");
		supplierIdField.setMaxWidth(200);

		Button deleteButton = new Button("Delete");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			SuppliersInterface(primaryStage);
		});

		Label feedbackLabel = new Label();

		deleteButton.setOnAction(e -> {
			String supplierIdStr = supplierIdField.getText();
			if (!supplierIdStr.isEmpty()) {
				try {
					int supplierId = Integer.parseInt(supplierIdStr);
					SuppliersController controller = new SuppliersController();

					controller.  deleteSupplier(supplierId);
					feedbackLabel.setText("Supplier and its details deleted successfully.");
				} catch (NumberFormatException ex) {
					feedbackLabel.setText("Invalid Supplier ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					feedbackLabel.setText("Error deleting supplier.");
				}
			} else {
				feedbackLabel.setText("Supplier ID cannot be empty.");
			}
		});

		HBox buttonBox = new HBox(10, deleteButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(20, label, supplierIdField, buttonBox, feedbackLabel);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Delete Supplier");

		primaryStage.show();
	}
	public void SelectSupplier(Stage primaryStage) {

		Label label = new Label("Enter Supplier ID to View Details:");

		TextField SupplierIdField = new TextField();
		SupplierIdField.setPromptText("Supplier ID");

		Button selectButton = new Button("Select Supplier");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> SuppliersInterface(primaryStage));

		Label SupplierDetailsLabel = new Label("Supplier Details:");

		Label SupplierNameLabel = new Label("Name: ");
		Label SupplierNumberLabel = new Label("Number: ");
		Label SupplierEmailLabel = new Label("Email: ");

		Label SupplierNameValue = new Label();
		Label SupplierNumberValue = new Label();
		Label SupplierEmailValue = new Label();

		VBox vbox = new VBox(5, label, SupplierIdField, selectButton, SupplierDetailsLabel,
				SupplierNameLabel, SupplierNameValue, SupplierNumberLabel, SupplierNumberValue, SupplierEmailLabel, SupplierEmailValue, backButton);
		vbox.setAlignment(Pos.CENTER);

		selectButton.setOnAction(e -> {
			String SupplierIdText = SupplierIdField.getText();
			if (!SupplierIdText.isEmpty()) {
				try {
					int SuppliertId = Integer.parseInt(SupplierIdText);

					SuppliersController controller = new SuppliersController();
					Suppliers Supplier = controller.selectSupplier(SuppliertId);

					if (Supplier != null) {
						SupplierNameValue.setText(Supplier.getSupplier_name().get());
						SupplierNumberValue.setText(Supplier.getSupplier_number().get());
						SupplierEmailValue.setText(Supplier.getSupplier_email().get());
					} else {
						SupplierNameValue.setText("Supplier not found.");
						SupplierNumberValue.setText("");
						SupplierEmailValue.setText("");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid Supplier ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please enter a Supplier ID.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Select Supplier");

		primaryStage.show();
	}
	public void updateSupplier(Stage primaryStage) {


		Label lb = new Label("Enter Supplier id to select");
		lb.setAlignment(Pos.CENTER);

		TextField SupplierIdField = new TextField();
		SupplierIdField.setPromptText("Supplier ID");

		Button selectButton = new Button("Select Supplier");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> SuppliersInterface(primaryStage));

		TextField SupplierNameField = new TextField();
		SupplierNameField.setPromptText("Name");

		TextField SupplierNumberField = new TextField();
		SupplierNumberField.setPromptText("Number");

		TextField SupplierEmailField = new TextField();
		SupplierEmailField.setPromptText("Email");

		Button updateButton = new Button("Update Supplier");
		updateButton.setDisable(true);

		HBox hbox = new HBox(5, updateButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, lb, SupplierIdField, selectButton, SupplierNameField,
				SupplierNumberField, SupplierEmailField, hbox);

		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);

		selectButton.setOnAction(e -> {
			String SupplierIdText = SupplierIdField.getText();
			if (!SupplierIdText.isEmpty()) {
				try {
					int supplierId = Integer.parseInt(SupplierIdText);

					SuppliersController controller = new SuppliersController();
					Suppliers supplier = controller.selectSupplier(supplierId);

					if (supplier != null) {
						SupplierNameField.setText(supplier.getSupplier_name().get());
						SupplierNumberField.setText(supplier.getSupplier_number().get());
						SupplierEmailField.setText(supplier.getSupplier_email().get());

						updateButton.setDisable(false);
					} else {
						showAlert3("Supplier not found.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid supplier ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please enter a valid supplier ID.");
			}
		});

		updateButton.setOnAction(e -> {
			String SupplierIdText = SupplierIdField.getText();
			String name = SupplierNameField.getText();
			String email = SupplierEmailField.getText();
			String phoneNumberStr = SupplierNumberField.getText();

			if (!SupplierIdText.isEmpty() && !name.isEmpty() && !email.isEmpty() && !phoneNumberStr.isEmpty()) {
				try {
					int supplierId = Integer.parseInt(SupplierIdText);
					long phoneNumber = Long.parseLong(phoneNumberStr);

					SuppliersController controller = new SuppliersController();
					controller.updateSupplier(supplierId, name, email, phoneNumber);

					showAlert2("Supplier updated successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid number for Supplier ID and Phone Number.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error updating supplier.");
				}
			} else {
				showAlert1("Please fill all the fields.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Update Supplier");

		primaryStage.show();
	}
	public void loadSuppliers() {
		SuppliersobservableList.clear();
		String query = "SELECT s.Suppliers_ID, s.SuppliersName, se.SuppliersEmail, sn.SupplierNumber " +
				"FROM Suppliers s " +
				"LEFT JOIN Supplier_Email se ON s.Suppliers_ID = se.Suppliers_ID " +
				"LEFT JOIN Supplier_Number sn ON s.Suppliers_ID = sn.Suppliers_ID";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				SuppliersobservableList.add(new Suppliers(
						rs.getInt("Suppliers_ID"),
						rs.getString("SuppliersName"),
						rs.getString("SupplierNumber"),
						rs.getString("SuppliersEmail")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void loadEmployees() {
		EmployeeobservableList.clear(); 

		String query = "SELECT e.Employee_Id, e.EmployeeName, e.Password, e.Salary, e.Status, e.CreatedAt, " +
				"ee.EmployeeEmail, en.EmployeeNumber " +
				"FROM Employee e " +
				"LEFT JOIN Employee_Email ee ON e.Employee_Id = ee.Employee_Id " +
				"LEFT JOIN Employee_Number en ON e.Employee_Id = en.Employee_Id";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				EmployeeobservableList.add(new Employee(
						rs.getInt("Employee_Id"),
						rs.getString("EmployeeName"),
						rs.getString("EmployeeNumber"),
						rs.getString("EmployeeEmail"),
						rs.getString("Password"),
						rs.getString("Status"),
						rs.getDouble("Salary")

						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void insertEmployee(Stage primaryStage) {

		Label titleLabel = new Label("Enter New Employee Information");

		Label nameLabel = new Label("Employee Name:");
		TextField nameField = new TextField();
		nameField.setPromptText("Employee Name");

		Label passwordLabel = new Label("Password:");
		TextField passwordField = new TextField();
		passwordField.setPromptText("Password");

		Label salaryLabel = new Label("Salary:");
		TextField salaryField = new TextField();
		salaryField.setPromptText("Salary");

		Label statusLabel = new Label("Status:");
		ComboBox<String> statusComboBox = new ComboBox<>();
		statusComboBox.getItems().addAll("manager", "employee");
		statusComboBox.setPromptText("Select Status");

		Label numberLabel = new Label("Employee Number:");
		TextField numberField = new TextField();
		numberField.setPromptText("Employee Number");

		Label emailLabel = new Label("Employee Email:");
		TextField emailField = new TextField();
		emailField.setPromptText("Employee Email");

		Button insertButton = new Button("Insert Employee");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			EmployeeInterface(primaryStage);
		});

		insertButton.setOnAction(e -> {
			String name = nameField.getText();
			String password = passwordField.getText();
			String salaryStr = salaryField.getText();
			String status = statusComboBox.getValue();
			String email = emailField.getText();
			String phoneNumberStr = numberField.getText();

			if (!name.isEmpty() && !password.isEmpty() && !salaryStr.isEmpty() && status != null && !email.isEmpty() && !phoneNumberStr.isEmpty()) {
				try {
					double salary = Double.parseDouble(salaryStr);
					long phoneNumber = Long.parseLong(phoneNumberStr);
					EmployeeController controller = new EmployeeController();

					controller.insertEmployee(name, password, salary, status, email, phoneNumber);
					showAlert2("Employee inserted successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Invalid number format.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error occurred while inserting the Employee.");
				}
			} else {
				showAlert1("Please fill in all fields.");
			}
		});

		HBox hbox = new HBox(5, insertButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10, titleLabel, nameLabel, nameField, passwordLabel, passwordField, salaryLabel, salaryField, statusLabel, statusComboBox, numberLabel, numberField, emailLabel, emailField, hbox);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Insert Employee");

		primaryStage.show();
	}
	public void deleteEmployee(Stage primaryStage) {

		Label label = new Label("Enter the Employee ID:");

		TextField employeeIdField = new TextField();
		employeeIdField.setPromptText("Employee ID");
		employeeIdField.setMaxWidth(200);

		Button deleteButton = new Button("Delete");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> EmployeeInterface(primaryStage));

		Label feedbackLabel = new Label();

		deleteButton.setOnAction(e -> {
			String employeeIdStr = employeeIdField.getText();
			if (!employeeIdStr.isEmpty()) {
				try {
					int employeeId = Integer.parseInt(employeeIdStr);
					EmployeeController controller = new EmployeeController();

					controller.deleteEmployee(employeeId);
					showAlert2("Employee and its details deleted successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Invalid Employee ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error deleting employee.");
				}
			} else {
				showAlert1("Employee ID cannot be empty.");
			}
		});

		HBox buttonBox = new HBox(10, deleteButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(20, label, employeeIdField, buttonBox, feedbackLabel);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Delete Employee");

		primaryStage.show();
	}
	public void selectEmployee(Stage primaryStage) {

		Label label = new Label("Enter Employee ID to View Details:");

		TextField employeeIdField = new TextField();
		employeeIdField.setPromptText("Employee ID");

		Button selectButton = new Button("Select Employee");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> EmployeeInterface(primaryStage));

		Label employeeDetailsLabel = new Label("Employee Details:");

		Label employeeNameLabel = new Label("Name: ");
		Label employeeSalaryLabel = new Label("Salary: ");
		Label employeeStatusLabel = new Label("Status: ");
		Label employeeEmailLabel = new Label("Email: ");
		Label employeePasswordLabel = new Label("Password: ");
		Label employeeNumberLabel = new Label("Number: ");

		Label employeeNameValue = new Label();
		Label employeeSalaryValue = new Label();
		Label employeeStatusValue = new Label();
		Label employeeEmailValue = new Label();
		Label employeePasswordValue = new Label();
		Label employeeNumberValue = new Label();

		VBox vbox = new VBox(5, label, employeeIdField, selectButton, employeeDetailsLabel,
				employeeNameLabel, employeeNameValue, employeeSalaryLabel, employeeSalaryValue, employeeStatusLabel, employeeStatusValue,
				employeeEmailLabel, employeeEmailValue, employeePasswordLabel, employeePasswordValue, employeeNumberLabel, employeeNumberValue, backButton);
		vbox.setAlignment(Pos.CENTER);

		selectButton.setOnAction(e -> {
			String employeeIdText = employeeIdField.getText();
			if (!employeeIdText.isEmpty()) {
				try {
					int employeeId = Integer.parseInt(employeeIdText);

					EmployeeController controller = new EmployeeController();
					Employee employee = controller.selectEmployee(employeeId);

					if (employee != null) {
						employeeNameValue.setText(employee.getEmployee_name().get());
						employeeSalaryValue.setText(employee.getSalary().get()+"");
						employeeStatusValue.setText(employee.getStatus().get());
						employeeEmailValue.setText(employee.getEmployee_Email().get());
						employeePasswordValue.setText(employee.getPassword().get());
						employeeNumberValue.setText(employee.getEmployee_number().get());
					} else {
						employeeNameValue.setText("Employee not found.");
						employeeSalaryValue.setText("");
						employeeStatusValue.setText("");
						employeeEmailValue.setText("");
						employeePasswordValue.setText("");
						employeeNumberValue.setText("");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid Employee ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please enter an Employee ID.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Select Employee");

		primaryStage.show();
	}
	public void updateEmployee(Stage primaryStage) {

		Label lb = new Label("Enter Employee ID to select");
		lb.setAlignment(Pos.CENTER);

		TextField employeeIdField = new TextField();
		employeeIdField.setPromptText("Employee ID");

		Button selectButton = new Button("Select Employee");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> EmployeeInterface(primaryStage));

		TextField employeeNameField = new TextField();
		employeeNameField.setPromptText("Name");

		TextField passwordField = new TextField();
		passwordField.setPromptText("Password");

		TextField salaryField = new TextField();
		salaryField.setPromptText("Salary");

		ComboBox<String> statusComboBox = new ComboBox<>();
		statusComboBox.getItems().addAll("manager", "employee");
		statusComboBox.setPromptText("Select Status");

		TextField employeeNumberField = new TextField();
		employeeNumberField.setPromptText("Number");

		TextField employeeEmailField = new TextField();
		employeeEmailField.setPromptText("Email");

		Button updateButton = new Button("Update Employee");
		updateButton.setDisable(true);

		HBox hbox = new HBox(5, updateButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, lb, employeeIdField, selectButton, employeeNameField,
				passwordField, salaryField, statusComboBox, employeeNumberField, employeeEmailField, hbox);

		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);

		selectButton.setOnAction(e -> {
			String employeeIdText = employeeIdField.getText();
			if (!employeeIdText.isEmpty()) {
				try {
					int employeeId = Integer.parseInt(employeeIdText);

					EmployeeController controller = new EmployeeController();
					Employee employee = controller.selectEmployee(employeeId);

					if (employee != null) {
						employeeNameField.setText(employee.getEmployee_name().get());
						passwordField.setText(employee.getPassword().get());
						salaryField.setText(Double.toString(employee.getSalary().get()));
						statusComboBox.setValue(employee.getStatus().get());
						employeeNumberField.setText(employee.getEmployee_number().get());
						employeeEmailField.setText(employee.getEmployee_Email().get());

						updateButton.setDisable(false);
					} else {
						showAlert3("Employee not found.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid employee ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error fetching employee data.");
				}
			} else {
				showAlert1("Please enter a valid employee ID.");
			}
		});

		updateButton.setOnAction(e -> {
			String employeeIdText = employeeIdField.getText();
			String name = employeeNameField.getText();
			String password = passwordField.getText();
			String salaryStr = salaryField.getText();
			String status = statusComboBox.getValue();
			String email = employeeEmailField.getText();
			String phoneNumberStr = employeeNumberField.getText();

			if (!employeeIdText.isEmpty() && !name.isEmpty() && !password.isEmpty() && !salaryStr.isEmpty() && status != null && !email.isEmpty() && !phoneNumberStr.isEmpty()) {
				try {
					int employeeId = Integer.parseInt(employeeIdText);
					double salary = Double.parseDouble(salaryStr);
					long phoneNumber = Long.parseLong(phoneNumberStr);

					EmployeeController controller = new EmployeeController();
					controller.updateEmployee(employeeId, name, password, salary, status, email, phoneNumber);

					showAlert2("Employee updated successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Please enter valid numbers for Salary and Phone Number.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert3("Error updating employee.");
				}
			} else {
				showAlert1("Please fill all the fields.");
			}
		});

		Scene scene = new Scene(vbox, 1200, 600);
		applyTheme(scene);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Update Employee");
		primaryStage.show();
	}

	private void showEmployeeNumbers(Stage primaryStage, Employee employee) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Phone Numbers for Employee " + employee.getEmployee_name().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT EmployeeNumber FROM Employee_Number WHERE Employee_Id = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, employee.getEmployee_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("EmployeeNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error loading phone numbers.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Employee Phone Numbers");

		stage.show();
	}
	private void showEmployeeEmails(Stage primaryStage, Employee employee) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Emails for Employee " + employee.getEmployee_name().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT EmployeeEmail FROM Employee_Email WHERE Employee_Id = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, employee.getEmployee_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("EmployeeEmail"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error loading emails.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Employee Emails");

		stage.show();
	}

	public void OrderInterface(Stage primaryStage) {

		TableView<Orders> tableview = new TableView<>();

		TableColumn<Orders, Integer> orderIdColumn = new TableColumn<>("Order ID");
		orderIdColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getOrderId()));

		TableColumn<Orders, Integer> customerIdColumn = new TableColumn<>("Customer ID");
		customerIdColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getCustomerId()));

		TableColumn<Orders, LocalDate> orderDateColumn = new TableColumn<>("Order Date");
		orderDateColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getOrderDate()));

		TableColumn<Orders, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getPrice()));

		TableColumn<Orders, Integer> discountIdColumn = new TableColumn<>("Discount ID");
		discountIdColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getDiscountId()));

		TableColumn<Orders, Double> originalPriceColumn = new TableColumn<>("Original Price");
		originalPriceColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getOriginalPrice()));

		TableColumn<Orders, Double> discountRateColumn = new TableColumn<>("Discount Rate");
		discountRateColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getDiscountRate()));

		TableColumn<Orders, Double> costPriceColumn = new TableColumn<>("Cost Price");
		costPriceColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getCostPrice()));

		tableview.getColumns().addAll(orderIdColumn, customerIdColumn, orderDateColumn, 
				priceColumn, discountIdColumn, originalPriceColumn, 
				discountRateColumn, costPriceColumn);

		ComboBox<String> filterComboBox = new ComboBox<>();
		filterComboBox.getItems().addAll("With Discounts", "Without Discounts");
		filterComboBox.setValue("With / without  Discounts");

		filterComboBox.setOnAction(e -> {
			String selectedOption = filterComboBox.getValue();
			if (selectedOption.equals("With Discounts")) {
				loadOrdersWithDiscounts(tableview);
			} else {
				loadOrdersWithoutDiscounts(tableview);
			}
		});

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button deleteButton = createMenuButton("Delete");
			Button updateButton = createMenuButton("Update");
			Button backButton = createMenuButton("Back");

			selectButton.setOnAction(e -> SelectOrder(primaryStage));
			updateButton.setOnAction(e -> UpdateOrder(primaryStage));
			insertButton.setOnAction(e -> InsertOrder(primaryStage));
			deleteButton.setOnAction(e -> DeleteOrder(primaryStage));

			backButton.setOnAction(e -> {
				SecondInterface(primaryStage);
			});

			vbox.getChildren().addAll(filterComboBox, selectButton, updateButton, insertButton, deleteButton, backButton);

			loadOrdersWithDiscounts(tableview); 

			BorderPane root = new BorderPane();
			root.setLeft(vbox);
			HBox hbox = new HBox(20, root, tableview);
			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene); 

			primaryStage.setScene(scene);
			primaryStage.setTitle("Orders");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void loadOrdersWithDiscounts(TableView<Orders> table) {
		ObservableList<Orders> OrdersobservableList = FXCollections.observableArrayList();
		String query = "SELECT o.Order_ID, o.Customer_ID, o.Price, o.Order_Date, o.DiscountID, o.OriginalPrice, o.DiscountRate, o.CostPrice " +
				"FROM Orders o WHERE o.DiscountID IS NOT NULL";

		try (Connection connection = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				OrdersobservableList.add(new Orders(
						rs.getInt("Order_ID"),
						rs.getInt("Customer_ID"),
						rs.getDouble("Price"),
						rs.getDate("Order_Date").toLocalDate(),
						rs.getInt("DiscountID"),
						rs.getDouble("OriginalPrice"),
						rs.getDouble("DiscountRate"),
						rs.getDouble("CostPrice")
						));
			}
			table.setItems(OrdersobservableList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void loadOrdersWithoutDiscounts(TableView<Orders> table) {
		ObservableList<Orders> OrdersobservableList = FXCollections.observableArrayList();
		String query = "SELECT o.Order_ID, o.Customer_ID, o.Price, o.Order_Date, o.DiscountID, o.OriginalPrice, o.DiscountRate, o.CostPrice " +
				"FROM Orders o WHERE o.DiscountID IS NULL";

		try (Connection connection = DriverManager.getConnection(DatabaseTwista.JDBC_URL, DatabaseTwista.USERNAME, DatabaseTwista.PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				OrdersobservableList.add(new Orders(
						rs.getInt("Order_ID"),
						rs.getInt("Customer_ID"),
						rs.getDouble("Price"),
						rs.getDate("Order_Date").toLocalDate(),
						null,
						rs.getDouble("OriginalPrice"),
						rs.getDouble("DiscountRate"),
						rs.getDouble("CostPrice")
						));
			}
			table.setItems(OrdersobservableList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}




	public void SelectOrder(Stage primaryStage) {

		Label label = new Label("Enter Order ID to View Details:");

		TextField orderIdField = new TextField();
		orderIdField.setPromptText("Order ID");

		Button selectButton = new Button("Select Order");
		selectButton.setStyle(
				" -fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 10px 20px;");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			OrderInterface(primaryStage); 
		});

		Label orderDetailsLabel = new Label("Order Details:");

		Label orderDateLabel = new Label("Order Date: ");
		Label orderPriceLabel = new Label("Price: ");
		Label orderIdLabel = new Label("Order ID: ");
		Label orderDiscountLabel = new Label("Discount ID: ");
		Label originalPriceLabel = new Label("Original Price: ");  
		Label discountRateLabel = new Label("Discount Rate: ");   
		Label costPriceLabel = new Label("Cost Price: ");        

		VBox vbox = new VBox(5, label, orderIdField, selectButton, orderDetailsLabel,
				orderIdLabel, orderDateLabel, orderPriceLabel, orderDiscountLabel, 
				originalPriceLabel, discountRateLabel, costPriceLabel, backButton);  
		vbox.setAlignment(Pos.CENTER);

		selectButton.setOnAction(e -> {
			String orderIdText = orderIdField.getText();
			if (!orderIdText.isEmpty()) {
				try {
					int orderId = Integer.parseInt(orderIdText);

					OrdersController orderController = new OrdersController();
					Orders order = orderController.select(orderId);

					if (order != null) {
						orderIdLabel.setText("Order ID: " + order.getOrderId());
						orderDateLabel.setText("Order Date: " + order.getOrderDate());
						orderPriceLabel.setText("Price: " + order.getPrice());
						orderDiscountLabel.setText("Discount ID: " + (order.getDiscountId() != null ? order.getDiscountId() : "N/A"));
						originalPriceLabel.setText("Original Price: " + order.getOriginalPrice());
						discountRateLabel.setText("Discount Rate: " + order.getDiscountRate());
						costPriceLabel.setText("Cost Price: " + order.getCostPrice());
					} else {
						orderIdLabel.setText("Order not found.");
						orderDateLabel.setText("Order Date: ");
						orderPriceLabel.setText("Price: ");
						orderDiscountLabel.setText("Discount ID: ");
						originalPriceLabel.setText("Original Price: ");
						discountRateLabel.setText("Discount Rate: ");
						costPriceLabel.setText("Cost Price: ");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid order ID.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				showAlert1("Please enter an Order ID.");
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Select Order");

		primaryStage.show();
	}



	public void UpdateOrder(Stage primaryStage) {

		Label lb = new Label("Enter Order ID to select");
		lb.setAlignment(Pos.CENTER);

		TextField orderIdField = new TextField();
		orderIdField.setPromptText("Order ID");

		Button selectButton = new Button("Select Order");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			OrderInterface(primaryStage); 
		});

		TextField orderDateField = new TextField();
		orderDateField.setPromptText("Order Date (yyyy-mm-dd)");

		TextField orderPriceField = new TextField();
		orderPriceField.setPromptText("Price");

		TextField originalPriceField = new TextField();
		originalPriceField.setPromptText("Original Price");

		TextField discountRateField = new TextField();
		discountRateField.setPromptText("Discount Rate");

		TextField costPriceField = new TextField();
		costPriceField.setPromptText("Cost Price");

		Button updateButton = new Button("Update Order");
		updateButton.setDisable(true);

		HBox hbox = new HBox(5, updateButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, lb, orderIdField, selectButton, orderDateField, orderPriceField,
				originalPriceField, discountRateField, costPriceField, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(5);

		selectButton.setOnAction(e -> {
			String orderIdText = orderIdField.getText();
			if (!orderIdText.isEmpty()) {
				try {
					int orderId = Integer.parseInt(orderIdText);

					OrdersController ordersController = new OrdersController();
					Orders order = ordersController.select(orderId);

					if (order != null) {
						orderDateField.setText(order.getOrderDate().toString());
						orderPriceField.setText(String.valueOf(order.getPrice()));
						originalPriceField.setText(String.valueOf(order.getOriginalPrice()));
						discountRateField.setText(String.valueOf(order.getDiscountRate()));
						costPriceField.setText(String.valueOf(order.getCostPrice()));

						updateButton.setDisable(false); 
					} else {
						showAlert3("Order not found.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid Order ID.");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				showAlert1("Please enter a valid Order ID.");
			}
		});

		updateButton.setOnAction(e -> {
			try {
				int orderId = Integer.parseInt(orderIdField.getText());
				LocalDate orderDate = LocalDate.parse(orderDateField.getText());
				double price = Double.parseDouble(orderPriceField.getText());
				double originalPrice = Double.parseDouble(originalPriceField.getText());
				double discountRate = Double.parseDouble(discountRateField.getText());
				double costPrice = Double.parseDouble(costPriceField.getText());

				Orders updatedOrder = new Orders(orderId, price, orderDate, originalPrice, discountRate, costPrice);

				OrdersController ordersController = new OrdersController();
				ordersController.update(updatedOrder);

				showAlert2("Order updated successfully.");

				orderDateField.clear();
				orderPriceField.clear();
				originalPriceField.clear();
				discountRateField.clear();
				costPriceField.clear();

			} catch (NumberFormatException ex) {
				showAlert1("Please fill all fields correctly.");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Update Order");

		primaryStage.show();
	}




	public void DeleteOrder(Stage primaryStage) {

		Label label = new Label("Enter the Order ID:");

		TextField orderIdField = new TextField();
		orderIdField.setPromptText("Order ID");
		orderIdField.setMaxWidth(200);

		Button deleteButton = new Button("Delete");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			OrderInterface(primaryStage); 
		});

		Label feedbackLabel = new Label();

		deleteButton.setOnAction(e -> {
			String orderIdText = orderIdField.getText();

			if (!orderIdText.isEmpty()) {
				try {
					int orderId = Integer.parseInt(orderIdText);
					OrdersController ordersController = new OrdersController();

					if (ordersController.select(orderId) != null) {
						ordersController.delete(orderId);
						showAlert2("Order with ID " + orderId + " has been deleted.");
						orderIdField.clear();  
					} else {
						showAlert1("Order with ID " + orderId + " does not exist.");
					}
				} catch (NumberFormatException ex) {
					showAlert1("Invalid Order ID. Please enter a valid number.");
				} catch (SQLException ex) {
					showAlert3("Error occurred while deleting the order: " + ex.getMessage());
					ex.printStackTrace();
				}
			} else {
				showAlert1("Please enter an Order ID.");
			}
		});

		HBox buttonBox = new HBox(10, deleteButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(20, label, orderIdField, buttonBox, feedbackLabel);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Delete Order");

		primaryStage.show();
	}




	public void InsertOrder(Stage primaryStage) {

		Locale.setDefault(Locale.ENGLISH);

		Label titleLabel = new Label("Enter New Order Information");

		Label customerIdLabel = new Label("Customer ID:");
		TextField customerIdField = new TextField();
		customerIdField.setPromptText("Customer ID");

		Label orderDateLabel = new Label("Order Date:");
		DatePicker orderDatePicker = new DatePicker();

		Label priceLabel = new Label("Price:");
		TextField priceField = new TextField();
		priceField.setPromptText("Price");

		Label discountIdLabel = new Label("Discount ID (Optional):");
		TextField discountIdField = new TextField();
		discountIdField.setPromptText("Discount ID (Optional)");

		Label originalPriceLabel = new Label("Original Price:");
		TextField originalPriceField = new TextField();
		originalPriceField.setPromptText("Original Price");

		Label discountRateLabel = new Label("Discount Rate:");
		TextField discountRateField = new TextField();
		discountRateField.setPromptText("Discount Rate");

		Label costPriceLabel = new Label("Cost Price:");
		TextField costPriceField = new TextField();
		costPriceField.setPromptText("Cost Price");

		Button insertButton = new Button("Insert Order");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			OrderInterface(primaryStage);
		});

		insertButton.setOnAction(e -> {
			String customerIdText = customerIdField.getText();
			LocalDate orderDate = orderDatePicker.getValue();
			String priceText = priceField.getText();
			String discountIdText = discountIdField.getText();
			String originalPriceText = originalPriceField.getText();
			String discountRateText = discountRateField.getText();
			String costPriceText = costPriceField.getText();

			if (!customerIdText.isEmpty() && orderDate != null && !priceText.isEmpty() && !originalPriceText.isEmpty() &&
					!discountRateText.isEmpty() && !costPriceText.isEmpty()) {
				try {
					int customerId = Integer.parseInt(customerIdText);
					double price = Double.parseDouble(priceText);
					double originalPrice = Double.parseDouble(originalPriceText);
					double discountRate = Double.parseDouble(discountRateText);
					double costPrice = Double.parseDouble(costPriceText);

					Integer discountId = null;
					if (!discountIdText.isEmpty()) {
						discountId = Integer.parseInt(discountIdText);
					}

					Orders order = new Orders(0, customerId, price, orderDate, discountId, originalPrice, discountRate, costPrice);

					OrdersController controller = new OrdersController();
					controller.insertOrder(order);

					showAlert2("Order inserted successfully.");
				} catch (NumberFormatException ex) {
					showAlert1("Invalid format for Customer ID, Price, Original Price, Discount Rate, or Cost Price.");
				}
			} else {
				showAlert1("Please fill in all fields.");
			}
		});

		HBox hbox = new HBox(5, insertButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(5, titleLabel, customerIdLabel, customerIdField, orderDateLabel, orderDatePicker, priceLabel, priceField,
				discountIdLabel, discountIdField, originalPriceLabel, originalPriceField, discountRateLabel, discountRateField,
				costPriceLabel, costPriceField, hbox);
		vbox.setAlignment(Pos.CENTER);

		primaryStage.setY(0);
		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Insert Order");


		primaryStage.show();
	}
	public void CustomersInterface(Stage P) {

		TableView<Customer> tableView = new TableView<>();
		tableView.setItems(customerObservableList);
		loadCustomers();
		P.setTitle("Customer Management");

		TableColumn<Customer, Number> idColumn = new TableColumn<>("Customer ID");
		idColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomer_id());

		TableColumn<Customer, String> nameColumn = new TableColumn<>("Customer Name");
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

		TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress());

		TableColumn<Customer, Number> phoneColumn = new TableColumn<>("Phone Number");
		phoneColumn.setCellValueFactory(cellData -> cellData.getValue().getPhone_number());

		TableColumn<Customer, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(cellData -> cellData.getValue().getEmail());

		tableView.getColumns().addAll(idColumn, nameColumn, addressColumn, phoneColumn, emailColumn);

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button updateButton = createMenuButton("Update");
			Button backButton = createMenuButton("Back");

			selectButton.setOnAction(e -> selectCustomer(P));
			insertButton.setOnAction(e -> insertCustomer(P));
			updateButton.setOnAction(e -> updateCustomer(P, tableView));
			backButton.setOnAction(e -> SecondInterface(P));

			vbox.getChildren().addAll(selectButton, insertButton, updateButton, backButton);

			VBox phoneNumberVBox = createAddPhoneNumberInterface_(P, tableView);
			VBox emailVBox = createAddEmailInterface_(P, tableView);

			TitledPane titledPane = new TitledPane("Add Details", new VBox(phoneNumberVBox, emailVBox));
			titledPane.setCollapsible(true);
			titledPane.setExpanded(false);

			Button showNumbersButton = new Button("Show Customer Numbers");
			showNumbersButton.setOnAction(e -> {
				Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
				if (selectedCustomer != null) {
					showCustomerNumbers(P, selectedCustomer);
				} else {
					showAlert1("Please select a customer first.");
				}
			});

			Button showEmailsButton = new Button("Show Customer Emails");
			showEmailsButton.setOnAction(e -> {
				Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
				if (selectedCustomer != null) {
					showCustomerEmails(P, selectedCustomer);
				} else {
					showAlert1("Please select a customer first.");
				}
			});

			VBox comboBoxVBox = new VBox(10, titledPane, showNumbersButton, showEmailsButton);
			comboBoxVBox.setAlignment(Pos.TOP_LEFT);

			BorderPane root = new BorderPane();
			root.setLeft(vbox);

			HBox hbox = new HBox(20, vbox, tableView, comboBoxVBox);
			HBox.setHgrow(tableView, Priority.ALWAYS);
			tableView.setPrefWidth(600);


			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene);

			P.setScene(scene);
			P.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private VBox createAddPhoneNumberInterface_(Stage primaryStage, TableView<Customer> tableView) {
		VBox vbox = new VBox(10);

		Label label = new Label("Add Phone Number");

		TextField phoneNumberField = new TextField();
		phoneNumberField.setPromptText("Phone Number");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String phoneNumberStr = phoneNumberField.getText();
			Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
			if (selectedCustomer != null && !phoneNumberStr.isEmpty()) {
				try {
					addPhoneNumber3(selectedCustomer.getCustomer_id().get(), phoneNumberStr);
				} catch (NumberFormatException ex) {
					showAlert1("Please enter a valid phone number.");
				}
			} else {
				showAlert1("Please select a customer and enter a phone number.");
			}
		});

		vbox.getChildren().addAll(label, phoneNumberField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}

	private VBox createAddEmailInterface_(Stage primaryStage, TableView<Customer> tableView) {
		VBox vbox = new VBox(10);

		Label label = new Label("Add Email");

		TextField emailField = new TextField();
		emailField.setPromptText("Email");

		Button addButton = new Button("Add");
		addButton.setOnAction(e -> {
			String email = emailField.getText();
			Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
			if (selectedCustomer != null && !email.isEmpty()) {
				addEmail3(selectedCustomer.getCustomer_id().get(), email);
			} else {
				showAlert1("Please select a customer and enter an email.");
			}
		});

		vbox.getChildren().addAll(label, emailField, addButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return vbox;
	}

	public void loadCustomers() {
		customerObservableList.clear();

		String query = "SELECT c.CustomerID, c.CustomerName, c.CustomerAddress, " +
				"ce.CustomerEmail, cn.CustomerNumber " +
				"FROM Customer c " +
				"LEFT JOIN Customer_Email ce ON c.CustomerID = ce.CustomerID " +
				"LEFT JOIN Customer_Number cn ON c.CustomerID = cn.CustomerID";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				String customerEmail = rs.getString("CustomerEmail");
				String customerAddress = rs.getString("CustomerAddress");
				Long customerNumber = (Long) rs.getObject("CustomerNumber");

				customerObservableList.add(new Customer(
						rs.getInt("CustomerID"),
						rs.getString("CustomerName"),
						customerAddress != null ? customerAddress : "No Address",  
								customerNumber != null ? customerNumber : 0L,  
										customerEmail != null ? customerEmail : "No Email"  
						));
			}


			System.out.println("Loaded customers: " + customerObservableList.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}





	private void insertCustomer(Stage primaryStage) {
		Label titleLabel = new Label("Enter New Customer Information");

		Label nameLabel = new Label("Customer Name:");
		TextField nameField = new TextField();
		nameField.setPromptText("Customer Name");

		Label phoneNumberLabel = new Label("Phone Number:");
		TextField phoneNumberField = new TextField();
		phoneNumberField.setPromptText("Phone Number");

		Label emailLabel = new Label("Email:");
		TextField emailField = new TextField();
		emailField.setPromptText("Email");

		Label addressLabel = new Label("Address:");
		TextField addressField = new TextField();
		addressField.setPromptText("Address");

		Button insertButton = new Button("Insert Customer");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			CustomersInterface(primaryStage);
		});

		insertButton.setOnAction(e -> {

			CustomerController customerController=new CustomerController();
			String name = nameField.getText();
			String email = emailField.getText();
			String phoneNumberStr = phoneNumberField.getText();
			String address = addressField.getText();

			if (!name.isEmpty() && !email.isEmpty() && !phoneNumberStr.isEmpty() && !address.isEmpty()) {
				try {
					long phoneNumber = Long.parseLong(phoneNumberStr);


					customerController.insert(name,email,phoneNumber,address);


					showAlert(Alert.AlertType.INFORMATION, "Success", "Customer inserted successfully.");

					nameField.clear();
					phoneNumberField.clear();
					emailField.clear();
					addressField.clear();
				} catch (NumberFormatException ex) {
					showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number format.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Error", "Error occurred while inserting the customer.");
				}
			} else {
				showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
			}
		});

		HBox hbox = new HBox(5, insertButton, backButton);
		hbox.setAlignment(Pos.CENTER);

		VBox vbox = new VBox(10, titleLabel, nameLabel, nameField, phoneNumberLabel, phoneNumberField, emailLabel, emailField, addressLabel, addressField, hbox);
		vbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Insert Customer");

		primaryStage.show();
	}



	private void updateCustomer(Stage primaryStage, TableView<Customer> tableView) {

		Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
		if (selectedCustomer == null) {
			showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to update.");
			return;
		}
		CustomerController customerController = new CustomerController();

		TextField nameField = new TextField(selectedCustomer.getName().get());
		TextField phoneNumberField = new TextField(String.valueOf(selectedCustomer.getPhone_number().get()));
		TextField emailField = new TextField(selectedCustomer.getEmail().get());
		TextField addressField = new TextField(selectedCustomer.getAddress().get());

		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> {
			try {
				String name = nameField.getText();
				long phoneNumber = Long.parseLong(phoneNumberField.getText());
				String email = emailField.getText();
				String address = addressField.getText();

				selectedCustomer.setName(name);
				selectedCustomer.setPhone_number(phoneNumber);
				selectedCustomer.setEmail(email);
				selectedCustomer.setAddress(address);

				// Pass updated customer details to the controller for database update
				customerController.update(selectedCustomer.getCustomer_id().get(), name, address, phoneNumber, email); // Ensure selectedCustomer has getId method
				tableView.refresh(); // Refresh the table view

				showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully.");

				// Do not close the window, but clear fields or reset as needed
				nameField.clear();
				phoneNumberField.clear();
				emailField.clear();
				addressField.clear();
			} catch (Exception ex) {
				showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer.");
			}
		});

		VBox layout = new VBox(10, nameField, phoneNumberField, emailField, addressField, saveButton);
		layout.setPadding(new Insets(10));

		Stage dialog = new Stage();
		Scene scene=new Scene(layout, 400, 300);
		applyTheme(scene);

		dialog.setScene(scene);
		dialog.show();
	}


	private void selectCustomer(Stage primaryStage) {

		Label promptLabel = new Label("Enter Customer ID:");

		TextField customerIdField = new TextField();
		customerIdField.setPromptText("Customer ID");

		Button selectButton = new Button("Select Customer");

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> CustomersInterface(primaryStage));

		Label customerDetailsLabel = new Label("Customer Details:");

		Label idLabel = new Label("Customer ID: ");
		Label nameLabel = new Label("Name: ");
		Label phoneLabel = new Label("Phone: ");
		Label emailLabel = new Label("Email: ");
		Label addressLabel = new Label("Address: ");

		Label idValue = new Label();
		Label nameValue = new Label();
		Label phoneValue = new Label();
		Label emailValue = new Label();
		Label addressValue = new Label();

		VBox vbox = new VBox(5, promptLabel, customerIdField, selectButton, customerDetailsLabel,
				idLabel, idValue, nameLabel, nameValue, phoneLabel, phoneValue, emailLabel, emailValue, addressLabel, addressValue, backButton);
		vbox.setAlignment(Pos.CENTER);

		selectButton.setOnAction(e -> {
			String customerIdText = customerIdField.getText();

			if (customerIdText.isEmpty()) {
				showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a Customer ID.");
				return;
			}

			int customerId;
			try {
				customerId = Integer.parseInt(customerIdText);
			} catch (NumberFormatException ex) {
				showAlert(Alert.AlertType.WARNING, "Input Error", "Invalid Customer ID format.");
				return;
			}

			Customer selectedCustomer = null;
			for (Customer customer : customerObservableList) {
				if (customer.getCustomer_id().get() == customerId) {
					selectedCustomer = customer;
					break;
				}
			}

			if (selectedCustomer == null) {
				showAlert(Alert.AlertType.WARNING, "Not Found", "Customer with ID " + customerId + " not found.");
				return;
			}

			idValue.setText(selectedCustomer.getCustomer_id().get() + "");
			nameValue.setText(selectedCustomer.getName().get());
			phoneValue.setText(selectedCustomer.getPhone_number().get()+"");
			emailValue.setText(selectedCustomer.getEmail().get());
			addressValue.setText(selectedCustomer.getAddress().get());
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.setTitle("Customer Details");

		primaryStage.show();
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void addPhoneNumber3(int customerId, String phoneNumber) {
		String insertPhoneSql = "INSERT INTO Customer_Number (CustomerID, CustomerNumber) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertPhoneStmt = conn.prepareStatement(insertPhoneSql)) {

			insertPhoneStmt.setInt(1, customerId); 
			insertPhoneStmt.setLong(2, Long.parseLong(phoneNumber));
			insertPhoneStmt.executeUpdate();

			showAlert2("Phone number added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding phone number.");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			showAlert1("Invalid phone number format.");
		}
	}




	private void addEmail3(int customerId, String email) {
		String insertEmailSql = "INSERT INTO Customer_Email (CustomerID, CustomerEmail) VALUES (?, ?)";

		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement insertEmailStmt = conn.prepareStatement(insertEmailSql)) {

			insertEmailStmt.setInt(1, customerId);
			insertEmailStmt.setString(2, email);
			insertEmailStmt.executeUpdate();

			showAlert2("Email added successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert3("Error adding email.");
		}
	}





	private void showCustomerNumbers(Stage primaryStage, Customer customer) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Phone Numbers for Customer " + customer.getName().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT CustomerNumber FROM Customer_Number WHERE CustomerID = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, customer.getCustomer_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("CustomerNumber"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert1("Error loading phone numbers.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Customer Phone Numbers");

		stage.show();
	}



	private void showCustomerEmails(Stage primaryStage, Customer customer) {
		Stage stage = new Stage();
		VBox vbox = new VBox(10);

		Label label = new Label("Emails for Customer " + customer.getName().get());
		ListView<String> listView = new ListView<>();

		String query = "SELECT CustomerEmail FROM Customer_Email WHERE CustomerID = ?";
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, customer.getCustomer_id().get());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				listView.getItems().add(rs.getString("CustomerEmail"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert1("Error loading emails.");
		}

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> stage.close());

		vbox.getChildren().addAll(label, listView, closeButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		Scene scene = new Scene(vbox, 300, 400);
		applyTheme(scene); 

		stage.setScene(scene);
		stage.setTitle("Customer Emails");

		stage.show();
	}
	public void DiscountInterface(Stage primaryStage) {

		TableView<Discount> tableView = new TableView<>();

		TableColumn<Discount, Integer> discountIdColumn = new TableColumn<>("Discount ID");
		discountIdColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getDiscountID()));

		TableColumn<Discount, Float> discountPercentageColumn = new TableColumn<>("Discount Percentage");
		discountPercentageColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getDiscountPercentage()));

		TableColumn<Discount, String> startDateColumn = new TableColumn<>("Start Date");
		startDateColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getStartDate().toString()));

		TableColumn<Discount, String> endDateColumn = new TableColumn<>("End Date");
		endDateColumn.setCellValueFactory(cellData -> 
		new SimpleObjectProperty<>(cellData.getValue().getEndDate().toString()));

		tableView.getColumns().addAll(discountIdColumn, discountPercentageColumn, startDateColumn, endDateColumn);

		VBox vbox = new VBox();
		vbox.setSpacing(15);
		vbox.setPadding(new Insets(20));

		try {
			Button selectButton = createMenuButton("Select");
			Button insertButton = createMenuButton("Insert");
			Button deleteButton = createMenuButton("Delete");
			Button updateButton = createMenuButton("Update");
			Button backButton = createMenuButton("Back");

			selectButton.setOnAction(e -> SelectDiscount(primaryStage));
			updateButton.setOnAction(e -> UpdateDiscount(primaryStage, tableView));
			insertButton.setOnAction(e -> InsertDiscount(primaryStage));
			deleteButton.setOnAction(e -> DeleteDiscount(primaryStage));

			backButton.setOnAction(e -> SecondInterface(primaryStage));

			vbox.getChildren().addAll(selectButton, updateButton, insertButton, deleteButton, backButton);

			loadDiscounts();
			tableView.setItems(DiscountObservableList);

			BorderPane root = new BorderPane();
			root.setLeft(vbox);

			HBox hbox = new HBox(20, root, tableView);
			HBox.setHgrow(tableView, Priority.ALWAYS);
			tableView.setPrefWidth(800);

			Scene scene = new Scene(hbox,1200,600);
			applyTheme(scene); 

			primaryStage.setScene(scene);
			primaryStage.setTitle("Discounts");

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadDiscounts() {
		DiscountObservableList.clear();
		String query = "SELECT * FROM Discount";

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int discountID = rs.getInt("DiscountID");
				float discountPercentage = rs.getFloat("DiscountPercentage");
				LocalDateTime startDate = rs.getTimestamp("StartDate").toLocalDateTime();
				LocalDateTime endDate = rs.getTimestamp("EndDate").toLocalDateTime();

				DiscountObservableList.add(new Discount(discountID, discountPercentage, startDate, endDate));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void InsertDiscount(Stage primaryStage) {

		Label percentageLabel = new Label("Enter Discount Percentage:");
		TextField discountField = new TextField();
		discountField.setPromptText("Discount Percentage");

		Label startDateLabel = new Label("Enter Start Date:");
		DatePicker startDatePicker = new DatePicker();
		startDatePicker.setPromptText("Start Date");

		Label endDateLabel = new Label("Enter End Date:");
		DatePicker endDatePicker = new DatePicker();
		endDatePicker.setPromptText("End Date");

		Button insertButton = new Button("Insert");
		Button backButton = new Button("Back");

		backButton.setOnAction(e -> DiscountInterface(primaryStage));

		HBox discountBox = new HBox(10, percentageLabel, discountField);
		discountBox.setAlignment(Pos.CENTER);
		HBox hbox = new HBox (10,backButton,insertButton);
		hbox.setAlignment(Pos.CENTER);
		VBox vbox = new VBox(20, discountBox, startDateLabel, startDatePicker, endDateLabel, endDatePicker, hbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		percentageLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");
		startDateLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");
		endDateLabel.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold;");
		discountField.setStyle("-fx-font-size: 10pt; -fx-padding: 5; -fx-pref-width: 150;");
		startDatePicker.setStyle("-fx-font-size: 10pt; -fx-padding: 5; -fx-pref-width: 200;");
		endDatePicker.setStyle("-fx-font-size: 10pt; -fx-padding: 5; -fx-pref-width: 200;");
		insertButton.setOnAction(e -> {
			String discountText = discountField.getText();
			LocalDate startDate = startDatePicker.getValue();
			LocalDate endDate = endDatePicker.getValue();

			if (!discountText.isEmpty() && startDate != null && endDate != null) {
				try {
					float discountPercentage = Float.parseFloat(discountText);

					if (discountPercentage < 0 || discountPercentage > 100) {
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						errorAlert.setTitle("Invalid Discount Percentage");
						errorAlert.setHeaderText(null);
						errorAlert.setContentText("Discount percentage must be between 0 and 100.");
						errorAlert.showAndWait();
						return;
					}

					LocalDateTime startDateTime = startDate.atStartOfDay();
					LocalDateTime endDateTime = endDate.atStartOfDay();

					DiscountController controller = new DiscountController();
					if (controller.insertDiscount(new Discount(0, discountPercentage, startDateTime, endDateTime))) {
						loadDiscounts();
						Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
						successAlert.setTitle("Insertion Successful");
						successAlert.setHeaderText(null);
						successAlert.setContentText("Discount inserted successfully!");
						successAlert.showAndWait();
					} else {
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						errorAlert.setTitle("Insertion Failed");
						errorAlert.setHeaderText(null);
						errorAlert.setContentText("Failed to insert discount. Please try again.");
						errorAlert.showAndWait();
					}
				} catch (NumberFormatException ex) {
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					errorAlert.setTitle("Invalid Input");
					errorAlert.setHeaderText(null);
					errorAlert.setContentText("Please enter valid numbers for Discount Percentage.");
					errorAlert.showAndWait();
				}
			} else {
				Alert warningAlert = new Alert(Alert.AlertType.WARNING);
				warningAlert.setTitle("Missing Information");
				warningAlert.setHeaderText(null);
				warningAlert.setContentText("Please enter all fields.");
				warningAlert.showAndWait();
			}
		});

		Scene scene = new Scene(vbox,1200,600);
		applyTheme(scene);

		primaryStage.setScene(scene);
	}
	private void DeleteDiscount(Stage primaryStage) {

		Label label = new Label("Enter Discount ID to Delete:");
		TextField discountIdField = new TextField();
		discountIdField.setPromptText("Discount ID");
		Button deleteButton = new Button("Delete");
		Button backButton = new Button("Back");


		HBox inputBox = new HBox(10, label, discountIdField);
		inputBox.setAlignment(Pos.CENTER);

		deleteButton.setOnAction(e -> {
			String discountIdText = discountIdField.getText();
			if (!discountIdText.isEmpty()) {
				try {
					int discountId = Integer.parseInt(discountIdText);
					DiscountController controller = new DiscountController();
					boolean isDeleted = controller.delete(discountId);

					if (isDeleted) {
						loadDiscounts();
						showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", "Discount deleted successfully!");
					} else {
						showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Discount ID not found. Deletion failed.");
					}
				} catch (NumberFormatException ex) {
					showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid Discount ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the discount. Please try again.");
				}
			} else {
				showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter a Discount ID to delete.");
			}
		});

		backButton.setOnAction(e -> DiscountInterface(primaryStage));

		VBox vbox = new VBox(15, inputBox, deleteButton, backButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		Scene scene=new Scene(vbox,1200,600);
		applyTheme(scene);

		primaryStage.setScene(scene);
	}

	private void SelectDiscount(Stage primaryStage) {
		Label label = new Label("Enter Discount ID to View Details:");
		TextField discountIdField = new TextField();
		discountIdField.setPromptText("Discount ID");
		Button selectButton = new Button("Select Discount");
		Button backButton = new Button("Back");


		Label detailsLabel = new Label("Discount Details:");
		Label discountDetailsLabel = new Label();
		discountDetailsLabel.setStyle("-fx-font-weight: bold;");

		selectButton.setOnAction(e -> {
			String discountIdText = discountIdField.getText();
			if (!discountIdText.isEmpty()) {
				try {
					int discountId = Integer.parseInt(discountIdText);
					DiscountController controller = new DiscountController();
					Discount discount = controller.select(discountId);

					if (discount != null) {
						discountDetailsLabel.setText(
								"Discount ID: " + discount.getDiscountID() +
								"\nPercentage: " + discount.getDiscountPercentage() +
								"\nStart Date: " + discount.getStartDate() +
								"\nEnd Date: " + discount.getEndDate()
								);
					} else {
						discountDetailsLabel.setText("Discount not found.");
					}
				} catch (NumberFormatException ex) {
					showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid Discount ID.");
				} catch (SQLException ex) {
					ex.printStackTrace();
					showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while retrieving the discount. Please try again.");
				}
			}
		});

		backButton.setOnAction(e -> DiscountInterface(primaryStage));

		VBox vbox = new VBox(15, label, discountIdField, selectButton, detailsLabel, discountDetailsLabel, backButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		Scene scene=new Scene(vbox,1200,600);
		applyTheme(scene);

		primaryStage.setScene(scene);
	}

	private void UpdateDiscount(Stage primaryStage, TableView<Discount> tableView) {

		Stage dialog = new Stage();

		Discount selectedDiscount = tableView.getSelectionModel().getSelectedItem();
		if (selectedDiscount == null) {
			showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a discount to update.");
			return;
		}

		Label percentageLabel = new Label("Discount Percentage:");
		TextField percentageField = new TextField(String.valueOf(selectedDiscount.getDiscountPercentage()));
		Label startDateLabel = new Label("Start Date:");
		DatePicker startDatePicker = new DatePicker(selectedDiscount.getStartDate().toLocalDate());
		Label endDateLabel = new Label("End Date:");
		DatePicker endDatePicker = new DatePicker(selectedDiscount.getEndDate().toLocalDate());
		Button saveButton = new Button("Save");


		saveButton.setOnAction(e -> {
			try {
				float discountPercentage = Float.parseFloat(percentageField.getText());
				if (discountPercentage < 0 || discountPercentage > 100) {
					showAlert(Alert.AlertType.ERROR, "Invalid Input", "Discount percentage must be between 0 and 100.");
					return;
				}

				LocalDate startDate = startDatePicker.getValue();
				LocalDate endDate = endDatePicker.getValue();

				if (startDate.isAfter(endDate)) {
					showAlert(Alert.AlertType.ERROR, "Invalid Dates", "Start date must be before end date.");
					return;
				}

				LocalDateTime startDateTime = startDate.atStartOfDay();
				LocalDateTime endDateTime = endDate.atStartOfDay();

				selectedDiscount.setDiscountPercentage(discountPercentage);
				selectedDiscount.setStartDate(startDateTime);
				selectedDiscount.setEndDate(endDateTime);

				DiscountController discountController = new DiscountController();
				boolean updated = discountController.update(selectedDiscount);

				if (updated) {
					tableView.refresh();
					showAlert(Alert.AlertType.INFORMATION, "Success", "Discount updated successfully.");
					dialog.close();
				} else {
					showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update discount. Please try again.");
				}
			} catch (NumberFormatException ex) {
				showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for Discount Percentage.");
			} catch (SQLException ex) {
				ex.printStackTrace();
				showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the discount. Please try again.");
			}
		});

		VBox layout = new VBox(10, percentageLabel, percentageField, startDateLabel, startDatePicker, endDateLabel, endDatePicker, saveButton);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);

		Scene scene=new Scene(layout, 400, 400);
		applyTheme(scene); 

		dialog.setScene(scene);

		dialog.show();
	}
	//*********************************************************************************************
	public void reviewsContainer(Stage primaryStage) {
		reviewPositions = new HashMap<>();

		primaryStage.setTitle("Customer reviews");

		reviewsContainer = new VBox(10);
		reviewsContainer.setAlignment(Pos.TOP_CENTER);

		archivedReviewsContainer = new VBox(10);
		archivedReviewsContainer.setAlignment(Pos.TOP_CENTER);
		archivedReviewsContainer.setPadding(new Insets(10));
		archivedReviewsContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
		archivedReviews = new ArrayList<>();

		reviewsContainer.getChildren().addAll(
				createReview("خدمة ممتازة ومنتجات ذات جودة عالية! - محمد أحمد"),
				createReview("المنتج وصل بسرعة وكان مطابق للوصف. شكرًا! - ليلى خليل"),
				createReview("تجربة شراء رائعة وسأكررها بالتأكيد. - خالد سعيد"),
				createReview("المنتجات رائعة، لكن الشحن كان بطيئًا. - سمية ناصر"),
				createReview("المنتج جاء في حالة جيدة وكان مطابقًا للوصف. - جمال عبد الله"),
				createReview("الخدمة كانت جيدة، لكن التوصيل تأخر. - فاطمة مصطفى"),
				createReview("تعامل راقي واحترافي. - أحمد علي"),
				createReview("المنتج لم يكن كما توقعت. - نادية حسن"),
				createReview("خدمة العملاء كانت ممتازة. - يوسف محمود")
				);

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> SecondInterface(primaryStage));

		Button archiveButton = new Button("سلة الأرشفة");
		archiveButton.setOnAction(e -> showArchivedReviews(primaryStage));

		HBox buttonsBox = new HBox(10, backButton, archiveButton);
		buttonsBox.setAlignment(Pos.CENTER);

		reviewsContainer.getChildren().add(buttonsBox);

		ImageView imageView = new ImageView(new Image("file:///C:/Users/user/Desktop/reviews.png"));
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);

		HBox imageContainer = new HBox(imageView);
		imageContainer.setAlignment(Pos.CENTER);

		HBox layout = new HBox(20, reviewsContainer, imageContainer);
		layout.setAlignment(Pos.TOP_CENTER);
		layout.setPrefWidth(1200);
		layout.setPadding(new Insets(20));

		Scene scene = new Scene(layout, 1200, 600, Color.LIGHTGRAY);
		applyTheme(scene); 

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private HBox createReview(String text) {
		Text reviewText = new Text(text);
		reviewText.getStyleClass().add("review-text");
		reviewText.getStyleClass().add("text-arabic"); 

		Button likeButton = new Button("❤️ أعجبني");
		likeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");

		Button dislikeButton = new Button("👎 لم يعجبني");
		dislikeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");

		likeButton.setOnAction(e -> {
			if (likeButton.getStyle().contains("#4CAF50")) {
				likeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");
			} else {
				likeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
				dislikeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");
			}
		});

		dislikeButton.setOnAction(e -> {
			if (dislikeButton.getStyle().contains("#dc143c")) {
				dislikeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");
			} else {
				dislikeButton.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
				likeButton.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");
			}
		});

		Label moreOptions = new Label("⋮");
		moreOptions.setStyle("-fx-font-size: 20px; -fx-cursor: hand;");

		ContextMenu contextMenu = new ContextMenu();
		MenuItem archiveItem = new MenuItem("📥 أرشفة");
		MenuItem deleteItem = new MenuItem("🗑️ حذف");

		archiveItem.setStyle("-fx-font-size: 12px;");
		deleteItem.setStyle("-fx-font-size: 12px;");

		archiveItem.setOnAction(e -> {
			HBox reviewBox = (HBox) moreOptions.getParent().getParent();
			int index = reviewsContainer.getChildren().indexOf(reviewBox);
			reviewPositions.put(reviewBox, index);
			reviewsContainer.getChildren().remove(reviewBox);
			archivedReviews.add(reviewBox);
		});

		deleteItem.setOnAction(e -> {
			HBox reviewBox = (HBox) moreOptions.getParent().getParent();
			reviewsContainer.getChildren().remove(reviewBox);
		});

		contextMenu.getItems().addAll(archiveItem, deleteItem);

		moreOptions.setOnMouseClicked(e -> contextMenu.show(moreOptions, e.getScreenX(), e.getScreenY()));

		HBox buttonsBox = new HBox(5, likeButton, dislikeButton, moreOptions);
		buttonsBox.setAlignment(Pos.CENTER_LEFT);

		HBox reviewBox = new HBox(10, reviewText, buttonsBox);
		reviewBox.setAlignment(Pos.CENTER_RIGHT);
		reviewBox.setPadding(new Insets(10));

		return reviewBox;
	}

	private void showArchivedReviews(Stage primaryStage) {
		Stage archiveStage = new Stage();
		archiveStage.setTitle("سلة الأرشفة");

		VBox archiveBox = new VBox(10);
		archiveBox.setAlignment(Pos.TOP_CENTER);
		archiveBox.setPadding(new Insets(20));

		for (HBox reviewBox : archivedReviews) {
			Label moreOptions = (Label) ((HBox) reviewBox.getChildren().get(1)).getChildren().get(2);
			ContextMenu contextMenu = new ContextMenu();
			MenuItem unarchiveItem = new MenuItem("📤 إلغاء الأرشفة");
			unarchiveItem.setStyle("-fx-font-size: 12px;");

			unarchiveItem.setOnAction(e -> {
				archivedReviews.remove(reviewBox);
				archiveBox.getChildren().remove(reviewBox);
				int originalIndex = reviewPositions.get(reviewBox);
				reviewsContainer.getChildren().add(originalIndex, reviewBox);
			});

			contextMenu.getItems().add(unarchiveItem);
			moreOptions.setOnMouseClicked(event -> contextMenu.show(moreOptions, event.getScreenX(), event.getScreenY()));

			archiveBox.getChildren().add(reviewBox);
		}

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> archiveStage.close());

		archiveBox.getChildren().add(backButton);

		Scene archiveScene = new Scene(archiveBox, 800, 600);
		applyTheme(archiveScene); 

		archiveStage.setScene(archiveScene);
		archiveStage.show();
	}

	private void applyTheme(Scene scene) {
		scene.getStylesheets().clear();
		if (DarkModeState.isDarkMode) {
			scene.getStylesheets().add(getClass().getResource("dark-mode.css").toExternalForm());
		} else {
			scene.getStylesheets().add(getClass().getResource("light-mode.css").toExternalForm());
		}
	}
	//************************************************************************************************
	public void chart(Stage primaryStage) {
		primaryStage.setTitle("Dashboard");

		// Create ComboBox for profit type selection
		ComboBox<String> profitTypeComboBox = new ComboBox<>();
		profitTypeComboBox.getItems().addAll("Profits with Discounts", "Profits without Discounts");
		profitTypeComboBox.setValue("Profits with Discounts"); // Default selection

		// Create LineChart
		CategoryAxis xAxis = new CategoryAxis(); // X-axis for dates (String)
		NumberAxis yAxis = new NumberAxis();     // Y-axis for profits (Number)
		xAxis.setLabel("Date");
		yAxis.setLabel("Profit");

		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis); // Explicit type arguments
		lineChart.setTitle("Profits by Date");

		// Customize xAxis to show all dates
		xAxis.setTickLabelRotation(90);
		xAxis.setTickLabelGap(10);

		// Handle ComboBox selection change
		profitTypeComboBox.setOnAction(e -> updateChart(profitTypeComboBox.getValue(), lineChart, xAxis));
		Button back =new Button  ("Back");
		HBox hbox= new HBox (back);
		hbox.setAlignment(Pos.CENTER);
		back.setOnAction(e -> SecondInterface(primaryStage));
		// Layout
		VBox vbox = new VBox(10, profitTypeComboBox, lineChart ,hbox);
		Scene scene = new Scene(vbox, 1200, 600);
		primaryStage.setScene(scene);
		applyTheme(scene);

		primaryStage.show();

		updateChart(profitTypeComboBox.getValue(), lineChart, xAxis);
	}

	private void updateChart(String selectedOption, LineChart<String, Number> lineChart, CategoryAxis xAxis) {
		String query;

		if (selectedOption.equals("Profits with Discounts")) {
			query = """
					    SELECT 
					        DATE_FORMAT(Order_Date, '%Y-%m') AS Month, 
					        SUM(Price - CostPrice) AS TotalProfit 
					    FROM Orders 
					    WHERE DiscountID IS NOT NULL 
					    GROUP BY DATE_FORMAT(Order_Date, '%Y-%m')
					    ORDER BY Month
					""";
		} else {
			query = """
					    SELECT 
					        DATE_FORMAT(Order_Date, '%Y-%m') AS Month, 
					        SUM(OriginalPrice - CostPrice) AS TotalProfit 
					    FROM Orders 
					    WHERE DiscountID IS NULL 
					    GROUP BY DATE_FORMAT(Order_Date, '%Y-%m')
					    ORDER BY Month
					""";
		}

		Map<String, Number> data = fetchDataFromDatabase(query);

		lineChart.getData().clear();

		// Add new data to chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(selectedOption);

		// Use TreeMap to sort data by month
		Map<String, Number> sortedData = new TreeMap<>(data);

		for (Map.Entry<String, Number> entry : sortedData.entrySet()) {
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
		}

		lineChart.getData().add(series);

		// Update xAxis to show all dates
		xAxis.setAutoRanging(false);
		xAxis.setCategories(FXCollections.observableArrayList(sortedData.keySet()));
	}

	private Map<String, Number> fetchDataFromDatabase(String query) {
		Map<String, Number> data = new HashMap<>();

		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				String month = resultSet.getString("Month");
				Number totalProfit = resultSet.getDouble("TotalProfit");
				data.put(month, totalProfit);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}
	public void Dashboard(Stage primaryStage) {
	    ToggleButton toggleDarkMode = new ToggleButton("Enable Dark Mode");
	    toggleDarkMode.setPrefWidth(150);

	    Label timeLabel = new Label();
	    timeLabel.setStyle("-fx-font-size: 14px;");

	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
	        timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	    }));
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();

	    Button calendarButton = new Button();
	    ImageView calendarIcon = new ImageView(new Image("file:///C:/Users/user/Desktop/cale.png"));
	    calendarIcon.setFitWidth(24);
	    calendarIcon.setFitHeight(24);
	    calendarButton.setGraphic(calendarIcon);
	    calendarButton.setOnAction(e -> showCalendar());

	    Label dashboardLabel = new Label("Dashboard");
	    dashboardLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

	    HBox topRightBox = new HBox(10, dashboardLabel, timeLabel, calendarButton, toggleDarkMode);
	    topRightBox.setAlignment(Pos.CENTER_LEFT);
	    topRightBox.setPadding(new Insets(10));

	    VBox sideMenu = new VBox();
	    sideMenu.setSpacing(15);
	    sideMenu.setPadding(new Insets(20));

	    Button dashboard = createMenuButton("Dashboard");
	    dashboard.setOnAction(e -> Dashboard(primaryStage));
	    Button chart = createMenuButton("Charts");
	    chart.setOnAction(e -> chart(primaryStage));
	    Button products = createMenuButton("Products");
	    products.setOnAction(e -> ProductInterface(primaryStage));
	    Button customers = createMenuButton("Customers");
	    customers.setOnAction(e -> CustomersInterface(primaryStage));
	    Button employees = createMenuButton("Employees");
	    employees.setOnAction(e -> EmployeeInterface(primaryStage));
	    Button suppliers = createMenuButton("Suppliers");
	    suppliers.setOnAction(e -> SuppliersInterface(primaryStage));
	    Button discounts = createMenuButton("Discounts");
	    discounts.setOnAction(e -> DiscountInterface(primaryStage));
	    Button orders = createMenuButton("Orders");
	    orders.setOnAction(e -> OrderInterface(primaryStage));
	    Button reviews = createMenuButton("Reviews");
	    reviews.setOnAction(e -> reviewsContainer(primaryStage));
	    Button logOut = createMenuButton("Log Out");
	    logOut.setOnAction(e -> start(primaryStage));

	    sideMenu.getChildren().addAll(
	            dashboard, chart, products, customers, employees, suppliers, orders, discounts, reviews, logOut
	    );

	    TableView<TableData2> productTableView = new TableView<>();
	    productTableView.setPrefWidth(500);
	    TableView<TableData> customerTableView = new TableView<>();
	    customerTableView.setPrefWidth(500);
	    TableView<TableData> categoryTableView = new TableView<>();
	    categoryTableView.setPrefWidth(500);

	    TableColumn<TableData2, String> productNameColumn = new TableColumn<>("Product Name");
	    productNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	    TableColumn<TableData2, String> categoryColumn = new TableColumn<>("Category");
	    categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
	    TableColumn<TableData2, Integer> minQuantityColumn = new TableColumn<>("Min Quantity");
	    minQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
	    productTableView.getColumns().addAll(productNameColumn, categoryColumn, minQuantityColumn);

	    TableColumn<TableData, String> nameColumn2 = new TableColumn<>("Customer");
	    nameColumn2.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
	    TableColumn<TableData, Double> spendingColumn2 = new TableColumn<>("Total Spending");
	    spendingColumn2.setCellValueFactory(cellData -> cellData.getValue().totalSpendingProperty().asObject());
	    TableColumn<TableData, String> lastPurchaseColumn2 = new TableColumn<>("Last Purchase");
	    lastPurchaseColumn2.setCellValueFactory(cellData -> cellData.getValue().lastPurchaseProperty());
	    customerTableView.getColumns().addAll(nameColumn2, spendingColumn2, lastPurchaseColumn2);

	    TableColumn<TableData, String> categoryColumn3 = new TableColumn<>("Category");
	    categoryColumn3.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
	    TableColumn<TableData, Double> inventoryValueColumn = new TableColumn<>("Total Inventory Value");
	    inventoryValueColumn.setCellValueFactory(cellData -> cellData.getValue().totalInventoryValueProperty().asObject());
	    categoryTableView.getColumns().addAll(categoryColumn3, inventoryValueColumn);

	    loadDataFromDatabase(productTableView, customerTableView, categoryTableView);

	    Label productTableLabel = new Label("Products with Low Stock (Quantity < 10)");
	    productTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Label customerTableLabel = new Label("Customer Spending Summary");
	    customerTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Label categoryTableLabel = new Label("Total Inventory Value by Category");
	    categoryTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    VBox productTableBox = new VBox(5, productTableLabel, productTableView);
	    VBox customerTableBox = new VBox(5, customerTableLabel, customerTableView);
	    VBox categoryTableBox = new VBox(5, categoryTableLabel, categoryTableView);

	    HBox tableContainer = new HBox(20, productTableBox, customerTableBox, categoryTableBox);
	    tableContainer.setPadding(new Insets(20));

	    BorderPane root = new BorderPane();
	    root.setLeft(sideMenu);
	    root.setCenter(tableContainer);
	    root.setTop(topRightBox);

	    Scene scene = new Scene(root, 1200, 600);

	    applyDarkMode(scene, toggleDarkMode);

	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Store Management");
	    primaryStage.show();

	    toggleDarkMode.setOnAction(e -> {
	        DarkModeState.isDarkMode = toggleDarkMode.isSelected();
	        applyDarkMode(scene, toggleDarkMode);
	    });
	}

	private void showCalendar() {
	    Stage calendarStage = new Stage();
	    calendarStage.setTitle("Calendar");

	    DatePicker datePicker = new DatePicker();
	    datePicker.setValue(LocalDate.now());

	    VBox layout = new VBox(10, datePicker);
	    layout.setPadding(new Insets(10));

	    Scene scene = new Scene(layout);
	    calendarStage.setScene(scene);
	    calendarStage.show();
	}
	private void loadDataFromDatabase(TableView<TableData2> productTableView,
			TableView<TableData> customerTableView,
			TableView<TableData> categoryTableView) {
		try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

			// Query for products with low stock
			String productQuery = "SELECT Product.Product_Name, Product.Category, MIN(Product_Details.Quantity) AS Min_Quantity " +
					"FROM Product " +
					"JOIN Product_Details ON Product.Product_ID = Product_Details.Product_ID " +
					"WHERE In_Out_Stock = 'IN' " +
					"GROUP BY Product.Product_Name, Product.Category " +
					"HAVING Min_Quantity < 10 " +
					"ORDER BY Min_Quantity ASC";

			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(productQuery)) {
				while (rs.next()) {
					String productName = rs.getString("Product_Name");
					String category = rs.getString("Category");
					int minQuantity = rs.getInt("Min_Quantity");
					productTableView.getItems().add(new TableData2(productName, category, minQuantity));
				}
			}

			// Query for customer spending
			String customerQuery = "SELECT c.CustomerName, SUM(o.Price) AS TotalSpending, MAX(o.Order_Date) AS LastPurchase " +
					"FROM Customer c JOIN Orders o ON c.CustomerID = o.Customer_ID " +
					"GROUP BY c.CustomerName";

			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(customerQuery)) {
				while (rs.next()) {
					String customerName = rs.getString("CustomerName");
					double totalSpending = rs.getDouble("TotalSpending");
					String lastPurchase = rs.getString("LastPurchase");
					customerTableView.getItems().add(new TableData(customerName, totalSpending, lastPurchase));
				}
			}

			// Query for total inventory value by category
			String categoryQuery = "SELECT Category, SUM(Price * Quantity) AS Total_Inventory_Value " +
					"FROM Product " +
					"JOIN Product_Details ON Product.Product_ID = Product_Details.Product_ID " +
					"WHERE In_Out_Stock = 'IN' " +
					"GROUP BY Category";

			try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(categoryQuery)) {
				while (rs.next()) {
					String category = rs.getString("Category");
					double totalInventoryValue = rs.getDouble("Total_Inventory_Value");
					categoryTableView.getItems().add(new TableData(category, totalInventoryValue));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void Dashboard2(Stage primaryStage) {
	    ToggleButton toggleDarkMode = new ToggleButton("Enable Dark Mode");
	    toggleDarkMode.setPrefWidth(150);

	    Label timeLabel = new Label();
	    timeLabel.setStyle("-fx-font-size: 14px;");

	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
	        timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
	    }));
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();

	    Button calendarButton = new Button();
	    ImageView calendarIcon = new ImageView(new Image("file:///C:/Users/user/Desktop/cale.png"));
	    calendarIcon.setFitWidth(24);
	    calendarIcon.setFitHeight(24);
	    calendarButton.setGraphic(calendarIcon);
	    calendarButton.setOnAction(e -> showCalendar());

	    Label dashboardLabel = new Label("Dashboard");
	    dashboardLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

	    HBox topRightBox = new HBox(10, dashboardLabel, timeLabel, calendarButton, toggleDarkMode);
	    topRightBox.setAlignment(Pos.CENTER_LEFT);
	    topRightBox.setPadding(new Insets(10));

	    VBox sideMenu = new VBox();
	    sideMenu.setSpacing(15);
	    sideMenu.setPadding(new Insets(20));

	    Button dashboard = createMenuButton("Dashboard");
	    dashboard.setOnAction(e -> Dashboard(primaryStage));
	    Button chart = createMenuButton("Charts");
	    chart.setOnAction(e -> chart(primaryStage));
	    Button products = createMenuButton("Products");
	    products.setOnAction(e -> ProductInterface(primaryStage));
	    Button customers = createMenuButton("Customers");
	    customers.setOnAction(e -> CustomersInterface(primaryStage));
	    Button employees = createMenuButton("Employees");
	    employees.setOnAction(e -> EmployeeInterface$(primaryStage));
	    Button suppliers = createMenuButton("Suppliers");
	    suppliers.setOnAction(e -> SuppliersInterface$(primaryStage));
	    Button discounts = createMenuButton("Discounts");
	    discounts.setOnAction(e -> DiscountInterface(primaryStage));
	    Button orders = createMenuButton("Orders");
	    orders.setOnAction(e -> OrderInterface(primaryStage));
	    Button reviews = createMenuButton("Reviews");
	    reviews.setOnAction(e -> reviewsContainer(primaryStage));
	    Button logOut = createMenuButton("Log Out");
	    logOut.setOnAction(e -> start(primaryStage));

	    sideMenu.getChildren().addAll(
	            dashboard, chart, products, customers, employees, suppliers, orders, discounts, reviews, logOut
	    );

	    TableView<TableData2> productTableView = new TableView<>();
	    productTableView.setPrefWidth(500);
	    TableView<TableData> customerTableView = new TableView<>();
	    customerTableView.setPrefWidth(500);
	    TableView<TableData> categoryTableView = new TableView<>();
	    categoryTableView.setPrefWidth(500);

	    TableColumn<TableData2, String> productNameColumn = new TableColumn<>("Product Name");
	    productNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	    TableColumn<TableData2, String> categoryColumn = new TableColumn<>("Category");
	    categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
	    TableColumn<TableData2, Integer> minQuantityColumn = new TableColumn<>("Min Quantity");
	    minQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
	    productTableView.getColumns().addAll(productNameColumn, categoryColumn, minQuantityColumn);

	    TableColumn<TableData, String> nameColumn2 = new TableColumn<>("Customer");
	    nameColumn2.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
	    TableColumn<TableData, Double> spendingColumn2 = new TableColumn<>("Total Spending");
	    spendingColumn2.setCellValueFactory(cellData -> cellData.getValue().totalSpendingProperty().asObject());
	    TableColumn<TableData, String> lastPurchaseColumn2 = new TableColumn<>("Last Purchase");
	    lastPurchaseColumn2.setCellValueFactory(cellData -> cellData.getValue().lastPurchaseProperty());
	    customerTableView.getColumns().addAll(nameColumn2, spendingColumn2, lastPurchaseColumn2);

	    TableColumn<TableData, String> categoryColumn3 = new TableColumn<>("Category");
	    categoryColumn3.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
	    TableColumn<TableData, Double> inventoryValueColumn = new TableColumn<>("Total Inventory Value");
	    inventoryValueColumn.setCellValueFactory(cellData -> cellData.getValue().totalInventoryValueProperty().asObject());
	    categoryTableView.getColumns().addAll(categoryColumn3, inventoryValueColumn);

	    loadDataFromDatabase(productTableView, customerTableView, categoryTableView);

	    Label productTableLabel = new Label("Products with Low Stock (Quantity < 10)");
	    productTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Label customerTableLabel = new Label("Customer Spending Summary");
	    customerTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Label categoryTableLabel = new Label("Total Inventory Value by Category");
	    categoryTableLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    VBox productTableBox = new VBox(5, productTableLabel, productTableView);
	    VBox customerTableBox = new VBox(5, customerTableLabel, customerTableView);
	    VBox categoryTableBox = new VBox(5, categoryTableLabel, categoryTableView);

	    HBox tableContainer = new HBox(20, productTableBox, customerTableBox, categoryTableBox);
	    tableContainer.setPadding(new Insets(20));

	    BorderPane root = new BorderPane();
	    root.setLeft(sideMenu);
	    root.setCenter(tableContainer);
	    root.setTop(topRightBox);

	    Scene scene = new Scene(root, 1200, 600);

	    applyDarkMode(scene, toggleDarkMode);

	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Store Management");
	    primaryStage.show();

	    toggleDarkMode.setOnAction(e -> {
	        DarkModeState.isDarkMode = toggleDarkMode.isSelected();
	        applyDarkMode(scene, toggleDarkMode);
	    });
	}
	
	
	private void SuppliersInterface$(Stage primaryStage) {
		Label messageLabel = new Label("Page Not Available");
		messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
		messageLabel.setTextFill(Color.WHITE);

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLACK);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(3);
		dropShadow.setOffsetY(3);
		messageLabel.setEffect(dropShadow);

		Button exitButton = new Button();
		ImageView exitIcon = new ImageView(new Image("file:///C:/Users/user/Desktop/log out.png")); 

		exitIcon.setFitWidth(30);
		exitIcon.setFitHeight(30);
		exitButton.setGraphic(exitIcon);
		exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		exitButton.setOnAction(e -> {
			Dashboard2(primaryStage); 
		});

		StackPane root = new StackPane(messageLabel);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(20));

		StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
		StackPane.setMargin(exitButton, new Insets(10));
		root.getChildren().add(exitButton);

		Scene scene = new Scene(root, 1200, 600);
		applyTheme(scene); 
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void EmployeeInterface$(Stage primaryStage) {
		Label messageLabel = new Label("Page Not Available");
		messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
		messageLabel.setTextFill(Color.WHITE);

		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.BLACK);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(3);
		dropShadow.setOffsetY(3);
		messageLabel.setEffect(dropShadow);

		Button exitButton = new Button();
		ImageView exitIcon = new ImageView(new Image("file:///C:/Users/user/Desktop/log out.png")); 
		exitIcon.setFitWidth(30);
		exitIcon.setFitHeight(30);
		exitButton.setGraphic(exitIcon);
		exitButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
		exitButton.setOnAction(e -> {
			Dashboard2(primaryStage);
		});

		StackPane root = new StackPane(messageLabel);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(20));

		StackPane.setAlignment(exitButton, Pos.TOP_LEFT);
		StackPane.setMargin(exitButton, new Insets(10));
		root.getChildren().add(exitButton);

		Scene scene = new Scene(root, 1200, 600);
		applyTheme(scene); 
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void showAlert1(String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setContentText(message);
		alert.showAndWait();
	}



	private void showAlert2(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert3(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(message);
		alert.showAndWait();
	}


}
