package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableData {
    private final StringProperty category;
    private final StringProperty customerName;
    private final DoubleProperty totalSpending;
    private final StringProperty lastPurchase;
    private final DoubleProperty totalInventoryValue;

    public TableData(String category, Double totalInventoryValue) {
        this.category = new SimpleStringProperty(category);
        this.totalInventoryValue = new SimpleDoubleProperty(totalInventoryValue);
        this.customerName = null;
        this.totalSpending = null;
        this.lastPurchase = null;
    }

    public TableData(String customerName, Double totalSpending, String lastPurchase) {
        this.category = null;
        this.customerName = new SimpleStringProperty(customerName);
        this.totalSpending = new SimpleDoubleProperty(totalSpending);
        this.lastPurchase = new SimpleStringProperty(lastPurchase);
        this.totalInventoryValue = null;
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty customerNameProperty() {
        return customerName;
    }

    public DoubleProperty totalSpendingProperty() {
        return totalSpending;
    }

    public StringProperty lastPurchaseProperty() {
        return lastPurchase;
    }

    public DoubleProperty totalInventoryValueProperty() {
        return totalInventoryValue;
    }

    public String getCategory() {
        return category.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public double getTotalSpending() {
        return totalSpending.get();
    }

    public String getLastPurchase() {
        return lastPurchase.get();
    }

    public double getTotalInventoryValue() {
        return totalInventoryValue.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public void setTotalSpending(double totalSpending) {
        this.totalSpending.set(totalSpending);
    }

    public void setLastPurchase(String lastPurchase) {
        this.lastPurchase.set(lastPurchase);
    }

    public void setTotalInventoryValue(double totalInventoryValue) {
        this.totalInventoryValue.set(totalInventoryValue);
    }
}

