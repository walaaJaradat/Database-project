package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Customer {
    private final IntegerProperty customer_id;
    private final StringProperty name;
    private final StringProperty address;
    private final LongProperty phone_number;
    private final StringProperty email;

  

    

	public Customer(int customer_id, String name, String address, Long phone_number,
			String email) {
		super();
		this.customer_id = new SimpleIntegerProperty (customer_id);
		this.name = new SimpleStringProperty(name);
		this.address = new SimpleStringProperty(address);
		this.phone_number = new SimpleLongProperty(phone_number);
		this.email =new SimpleStringProperty(email);
	}

	public IntegerProperty getCustomer_id() {
        return customer_id;
    }

    public StringProperty getName() {
        return name;
    }

    public LongProperty getPhone_number() {
        return phone_number;
    }

    public StringProperty getEmail() {
        return email;
    }

    public StringProperty getAddress() {
        return address;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id.set(customer_id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPhone_number(long phone_number) {
        this.phone_number.set(phone_number);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
