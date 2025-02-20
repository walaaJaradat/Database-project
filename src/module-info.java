module Database {
	requires javafx.controls;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;
	requires java.scripting;
	requires mysql.connector.j;
	
	opens application to javafx.graphics, javafx.fxml;
}
