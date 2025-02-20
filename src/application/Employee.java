package application;

import java.sql.Timestamp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
	private final IntegerProperty Employee_id;
	private final StringProperty Employee_name;
	private final StringProperty Employee_number;
	private final StringProperty Employee_Email;
	private final StringProperty Password;
	private final StringProperty Status;
	private final DoubleProperty Salary;
    private ObjectProperty<Timestamp> createdAt;

	public Employee(int employee_id, String employee_name, String employee_number,
			String employee_Email, String password, String status, Double salary) {
		super();
		this .Employee_id = new SimpleIntegerProperty(employee_id);
		this .	Employee_name = new SimpleStringProperty(employee_name);
		this .	Employee_number = new SimpleStringProperty(employee_number);
		this .	Employee_Email = new SimpleStringProperty(employee_Email);
		this .	Password = new SimpleStringProperty(password);
		this .	Status = new SimpleStringProperty(status);
		this .Salary = new SimpleDoubleProperty(salary);

	}
	/**
	 * @return the employee_id
	 */
	public IntegerProperty getEmployee_id() {
		return Employee_id;
	}
	/**
	 * @return the employee_name
	 */
	public StringProperty getEmployee_name() {
		return Employee_name;
	}
	/**
	 * @return the employee_number
	 */
	public StringProperty getEmployee_number() {
		return Employee_number;
	}
	/**
	 * @return the employee_Email
	 */
	public StringProperty getEmployee_Email() {
		return Employee_Email;
	}
	/**
	 * @return the password
	 */
	public StringProperty getPassword() {
		return Password;
	}
	/**
	 * @return the status
	 */
	public StringProperty getStatus() {
		return Status;
	}
	/**
	 * @return the salary
	 */
	public DoubleProperty getSalary() {
		return Salary;
	}
	/**
	 * @return the createdAt
	 */
	public ObjectProperty<Timestamp> getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(ObjectProperty<Timestamp> createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Employee [Employee_id=" + Employee_id + ", Employee_name=" + Employee_name + ", Employee_number="
				+ Employee_number + ", Employee_Email=" + Employee_Email + ", Password=" + Password + ", Status="
				+ Status + ", Salary=" + Salary + ", createdAt=" + createdAt + "]";
	}
	


}
