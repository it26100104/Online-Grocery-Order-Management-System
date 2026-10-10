package Member_06;

public class Delivery {
        private String deliveryId;
        private String orderId;
        private String customerName;
        private String address;
        private String deliveryAgent;
        private String deliveryStatus;

        // Constructor
        public Delivery(String deliveryId, String orderId,
                        String customerName, String address,
                        String deliveryAgent, String deliveryStatus) {

            this.deliveryId = deliveryId;
            this.orderId = orderId;
            this.customerName = customerName;
            this.address = address;
            this.deliveryAgent = deliveryAgent;
            this.deliveryStatus = deliveryStatus;
        }

        // Getters
        public String getDeliveryId() {
            return deliveryId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getAddress() {
            return address;
        }

        public String getDeliveryAgent() {
            return deliveryAgent;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        // Setters
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setDeliveryAgent(String deliveryAgent) {
            this.deliveryAgent = deliveryAgent;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }

        // Display delivery details
        public void displayDelivery() {
            System.out.println("Delivery ID: " + deliveryId);
            System.out.println("Order ID: " + orderId);
            System.out.println("Customer: " + customerName);
            System.out.println("Address: " + address);
            System.out.println("Delivery Agent: " + deliveryAgent);
            System.out.println("Status: " + deliveryStatus);
        }
    }