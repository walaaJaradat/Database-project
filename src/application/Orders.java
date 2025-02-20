package application;

import java.time.LocalDate;

public class Orders {
    private int orderId;
    private int customerId;
    private double price;
    private LocalDate orderDate;
    private Integer discountId;
    private double originalPrice;
    private double discountRate;
    private double costPrice;

    public Orders(int orderId, int customerId, double price, LocalDate orderDate, Integer discountId,
                  double originalPrice, double discountRate, double costPrice) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.orderDate = orderDate;
        this.discountId = discountId;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.costPrice = costPrice;
    }

    public Orders(int orderId, double price, LocalDate orderDate, double originalPrice, double discountRate, double costPrice) {
        this.orderId = orderId;
        this.price = price;
        this.orderDate = orderDate;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.costPrice = costPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    @Override
    public String toString() {
        return "OrderID: " + orderId + ", CustomerID: " + customerId + ", Price: " + price + ", OrderDate: " + orderDate
                + ", DiscountID: " + discountId + ", OriginalPrice: " + originalPrice + ", DiscountRate: " + discountRate
                + ", CostPrice: " + costPrice;
    }
}
