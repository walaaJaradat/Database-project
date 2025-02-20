package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty product_id;
    private final StringProperty name;
    private final DoubleProperty price;
    private final StringProperty category;
    private final StringProperty description;
    private final IntegerProperty stock_quantity;
    private final StringProperty in_out_stock;
    private final StringProperty size;        
    private final StringProperty color;      

    public Product(int product_id, String name, double price, String category, 
                   String description, int stock_quantity, String in_out_stock, 
                   String size, String color) {
        this.product_id = new SimpleIntegerProperty(product_id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.stock_quantity = new SimpleIntegerProperty(stock_quantity);
        this.in_out_stock = new SimpleStringProperty(in_out_stock);
        this.size = new SimpleStringProperty(size);  
        this.color = new SimpleStringProperty(color); 
    }

    // Getters
    public IntegerProperty getProduct_id() {
        return product_id;
    }

   

    public StringProperty getName() {
        return name;
    }

    public DoubleProperty getPrice() {
        return price;
    }

    public StringProperty getCategory() {
        return category;
    }

    public StringProperty getDescription() {
        return description;
    }

    public IntegerProperty getStock_quantity() {
        return stock_quantity;
    }

    public StringProperty getIn_out_stock() {
        return in_out_stock;
    }

    public StringProperty getSize() {
        return size;  
    }

    public StringProperty getColor() {
        return color; 
    }

   
}
