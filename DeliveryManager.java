package Member_06.Service;

import Member_06.Delivery;
import Member_06.util.FileHandler;

import java.util.ArrayList;

public class DeliveryManager {

    // CREATE
    public void addDelivery(Delivery delivery) {

        FileHandler.saveDelivery(delivery);

    }

    // READ
    public void viewAllDeliveries() {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        if (deliveries.isEmpty()) {

            System.out.println("No deliveries found.");

            return;
        }

        for (Delivery delivery : deliveries) {

            System.out.println("-------------------------");

            delivery.displayDelivery();
        }
    }


    // UPDATE
    public void updateDeliveryStatus(
            String deliveryId,
            String newStatus) {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        boolean found = false;

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId()
                    .equalsIgnoreCase(deliveryId)) {

                delivery.setDeliveryStatus(newStatus);
                found = true;
                break;
            }
        }

        if (found) {

            FileHandler.saveAllDeliveries(deliveries);

            System.out.println(
                    "Delivery status updated successfully."
            );

        } else {

            System.out.println("Delivery not found.");
        }
    }


    // DELETE
    public void deleteDelivery(String deliveryId) {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        boolean removed = deliveries.removeIf(
                delivery -> delivery.getDeliveryId()
                        .equalsIgnoreCase(deliveryId)
        );

        if (removed) {

            FileHandler.saveAllDeliveries(deliveries);

            System.out.println(
                    "Delivery deleted successfully."
            );

        } else {

            System.out.println("Delivery not found.");
        }
    }

    // Assign a delivery agent
    public void assignDeliveryAgent(
            String deliveryId,
            String agentName) {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        boolean found = false;

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId()
                    .equalsIgnoreCase(deliveryId)) {

                delivery.setDeliveryAgent(agentName);
                found = true;
                break;
            }
        }

        if (found) {
            FileHandler.saveAllDeliveries(deliveries);
            System.out.println("Delivery agent assigned successfully.");
        } else {
            System.out.println("Delivery not found.");
        }
    }


    // Update delivery address
    public void updateDeliveryAddress(
            String deliveryId,
            String newAddress) {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        boolean found = false;

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId()
                    .equalsIgnoreCase(deliveryId)) {

                delivery.setAddress(newAddress);
                found = true;
                break;
            }
        }

        if (found) {
            FileHandler.saveAllDeliveries(deliveries);
            System.out.println("Delivery address updated successfully.");
        } else {
            System.out.println("Delivery not found.");
        }
    }


    // Track one delivery
    public void trackDelivery(String deliveryId) {

        ArrayList<Delivery> deliveries =
                FileHandler.loadDeliveries();

        for (Delivery delivery : deliveries) {

            if (delivery.getDeliveryId()
                    .equalsIgnoreCase(deliveryId)) {

                System.out.println("\n--- DELIVERY TRACKING ---");
                delivery.displayDelivery();
                return;
            }
        }

        System.out.println("Delivery not found.");
    }
}
