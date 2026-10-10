
package Member_06.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SalesAnalytics {

    private static final String FILE_NAME = "orders.txt";

    // Calculate total revenue from delivered orders
    public double calculateTotalRevenue() {

        double totalRevenue = 0.0;

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length != 6) {
                    continue;
                }

                String status = data[2];

                if (status.equalsIgnoreCase("Delivered")) {

                    int quantity = Integer.parseInt(data[4]);
                    double unitPrice = Double.parseDouble(data[5]);

                    totalRevenue += quantity * unitPrice;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not read orders.txt: " + e.getMessage()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid quantity or price in orders.txt."
            );
        }

        return totalRevenue;
    }
}