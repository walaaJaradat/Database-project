package application;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Discount {
    private final SimpleIntegerProperty discountID;
    private final SimpleFloatProperty discountPercentage;
    private final SimpleObjectProperty<LocalDateTime> startDate;
    private final SimpleObjectProperty<LocalDateTime> endDate;

    public Discount(int discountID, float discountPercentage, LocalDateTime startDate, LocalDateTime endDate) {
        this.discountID = new SimpleIntegerProperty(discountID);
        this.discountPercentage = new SimpleFloatProperty(discountPercentage);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
    }

    // Getters
    public int getDiscountID() {
        return discountID.get();
    }

    public float getDiscountPercentage() {
        return discountPercentage.get();
    }

    public LocalDateTime getStartDate() {
        return startDate.get();
    }

    public LocalDateTime getEndDate() {
        return endDate.get();
    }

    // Properties
    public SimpleIntegerProperty discountIDProperty() {
        return discountID;
    }

    public SimpleFloatProperty discountPercentageProperty() {
        return discountPercentage;
    }

    public SimpleObjectProperty<LocalDateTime> startDateProperty() {
        return startDate;
    }

    public SimpleObjectProperty<LocalDateTime> endDateProperty() {
        return endDate;
    }

    public void setDiscountID(int discountID) {
        this.discountID.set(discountID);
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage.set(discountPercentage);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate.set(endDate);
    }
}