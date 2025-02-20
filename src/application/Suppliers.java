package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Suppliers {
    private final IntegerProperty supplier_id;
    private final StringProperty supplier_name;
    private final StringProperty supplier_number;
    private final StringProperty supplier_email;

    public Suppliers(int supplier_id, String supplier_name, String supplier_number, String supplier_email) {
        this.supplier_id = new SimpleIntegerProperty(supplier_id);
        this.supplier_name = new SimpleStringProperty(supplier_name);
        this.supplier_number = new SimpleStringProperty(supplier_number);
        this.supplier_email = new SimpleStringProperty(supplier_email);
    }

    /**
     * @return the supplier_id
     */
    public IntegerProperty getSupplier_id() {
        return supplier_id;
    }

    /**
     * @return the supplier_name
     */
    public StringProperty getSupplier_name() {
        return supplier_name;
    }

    /**
     * @return the supplier_number
     */
    public StringProperty getSupplier_number() {
        return supplier_number;
    }

    /**
     * @return the supplier_email
     */
    public StringProperty getSupplier_email() {
        return supplier_email;
    }
}