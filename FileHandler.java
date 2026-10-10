package Member_06.util;

import Member_06.Delivery;
import Member_06.Service.DeliveryManager;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE_NAME = "deliveries.txt";

    // Save delivery to file
    public static void saveDelivery(Delivery delivery) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_NAME, true))) {

            writer.write(
                    delivery.getDeliveryId() + "," +
                            delivery.getOrderId() + "," +
                            delivery.getCustomerName() + "," +
                            delivery.getAddress() + "," +
                            delivery.getDeliveryAgent() + "," +
                            delivery.getDeliveryStatus()
            );

            writer.newLine();

            System.out.println("Delivery saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving delivery: "
                    + e.getMessage());
        }
    }

    // Read all deliveries
    public static ArrayList<Delivery> loadDeliveries() {

        ArrayList<Delivery> deliveries = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 6) {

                    Delivery delivery = new Delivery(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            data[5]
                    );

                    deliveries.add(delivery);
                }
            }

        } catch (FileNotFoundException e) {

            System.out.println("No deliveries file found yet.");

        } catch (IOException e) {

            System.out.println("Error reading deliveries: "
                    + e.getMessage());
        }

        return deliveries;
    }

    // Rewrite the file with the current delivery list
    public static void saveAllDeliveries(
            ArrayList<Delivery> deliveries) {

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(FILE_NAME, false))) {

            for (Delivery delivery : deliveries) {

                writer.write(
                        delivery.getDeliveryId() + "," +
                                delivery.getOrderId() + "," +
                                delivery.getCustomerName() + "," +
                                delivery.getAddress() + "," +
                                delivery.getDeliveryAgent() + "," +
                                delivery.getDeliveryStatus()
                );

                writer.newLine();
            }

            System.out.println("File updated successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error updating file: " + e.getMessage()
            );
        }
    }
}
