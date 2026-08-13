package petrotech;

import petrotech.Branches;
import petrotech.Order;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class NotificationService {
    private static List<String> notifications = new ArrayList<>();

    public static void sendConfirmation(Order order, Branches branch) {
        StringBuilder contractDetails = new StringBuilder("Contract for Order ID: " + order.getOrderId() + "\n");
        // Assuming orderSummary() prints to console, we might need a method to get a string summary
        // For now, let's access relevant details directly if orderSummary isn't suitable
        contractDetails.append("Client: ").append(branch.getOwner()).append(" - ").append(branch.getName()).append("\n");
        contractDetails.append("Fuel Type: ").append(order.getFuelType()).append("\n");
        contractDetails.append("Amount per delivery: ").append(order.getFuelAmount()).append(" Liters\n");
        contractDetails.append("Frequency: ").append(order.getFrequency()).append(" times per week\n");
        contractDetails.append("Valid Until: ").append(LocalDate.now().plusYears(order.getValidTill())).append("\n");
        contractDetails.append("Total per delivery: $").append(branch.total(order)); // Assuming total() is accessible

        notifications.add("Order ID: " + order.getOrderId() + " has been CONFIRMED.\n" + contractDetails.toString());
    }

    public static void sendDenial(Order order, Branches branch) {
        notifications.add("Order ID: " + order.getOrderId() + " has been DENIED for " + branch.getName() + ".");
    }

    public static void sendUpcomingDelivery(Order order, Branches branch) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<LocalDate> todayDeliveries = order.getDeliveryDates().stream()
                .filter(today::equals)
                .collect(Collectors.toList());

        List<LocalDate> tomorrowDeliveries = order.getDeliveryDates().stream()
                .filter(tomorrow::equals)
                .collect(Collectors.toList());

        StringBuilder message = new StringBuilder("Upcoming Deliveries for Order ID: " + order.getOrderId() + " at " + branch.getName() + ":\n");

        if (!todayDeliveries.isEmpty()) {
            message.append("Today (" + today + "): ");
            todayDeliveries.forEach(date -> message.append(date).append(", "));
            message.delete(message.length() - 2, message.length());
            message.append("\n");
        }

        if (!tomorrowDeliveries.isEmpty()) {
            message.append("Tomorrow (" + tomorrow + "): ");
            tomorrowDeliveries.forEach(date -> message.append(date).append(", "));
            message.delete(message.length() - 2, message.length());
            message.append("\n");
        }

        if (todayDeliveries.isEmpty() && tomorrowDeliveries.isEmpty()) {
            message.append("No deliveries scheduled for today or tomorrow.");
        }

        notifications.add(message.toString());
    }

    public static List<String> getAllNotifications() {
        return new ArrayList<>(notifications);
    }

    public static void clearNotifications() {
        notifications.clear();
    }
}
