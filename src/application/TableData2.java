package application;

import javafx.beans.property.*;

public class TableData2 {
    private final StringProperty name;
    private final StringProperty category;
    private final IntegerProperty quantity;

    // Constructor for products table
    public TableData2(String name, String category, int quantity) {
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getter and setter for name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // Getter and setter for category
    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    // Getter and setter for quantity
    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }
}
