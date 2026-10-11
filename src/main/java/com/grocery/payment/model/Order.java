package com.grocery.payment.model;

/** One checkout record. Saved as one line in orders.txt. */
public class Order {

    private String orderId;
    private String customerName;
    private String phone;
    private String address;
    private String city;
    private double subtotal;
    private double deliveryCharge;
    private String paymentMethod;
    private double paymentFee;
    private double total;
    private String status;

    public Order(String orderId, String customerName, String phone, String address, String city,
                 double subtotal, double deliveryCharge, String paymentMethod,
                 double paymentFee, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.subtotal = subtotal;
        this.deliveryCharge = deliveryCharge;
        this.paymentMethod = paymentMethod;
        this.paymentFee = paymentFee;
        this.status = status;
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.total = subtotal + deliveryCharge + paymentFee;
    }

    // ---- Getters / Setters (ENCAPSULATION) ----
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(double deliveryCharge) { this.deliveryCharge = deliveryCharge; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getPaymentFee() { return paymentFee; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /** Convert this object to one line of text for the file. */
    public String toFileString() {
        return String.join("|",
                orderId, clean(customerName), clean(phone), clean(address), clean(city),
                String.valueOf(subtotal), String.valueOf(deliveryCharge),
                paymentMethod, String.valueOf(paymentFee), status);
    }

    /** Convert one line of the file back to an Order object. */
    public static Order fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        return new Order(p[0], p[1], p[2], p[3], p[4],
                Double.parseDouble(p[5]), Double.parseDouble(p[6]),
                p[7], Double.parseDouble(p[8]), p[9]);
    }

    private String clean(String s) {
        return s == null ? "" : s.replace("|", " ").replace("\n", " ").trim();
    }
}
