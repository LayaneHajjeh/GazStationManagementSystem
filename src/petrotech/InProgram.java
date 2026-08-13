
package petrotech;

import java.time.LocalDate;
import java.util.List;


public class InProgram extends Notifications {
    
    @Override
    public void sendConfirmation(Order order, Branches branch) {
        System.out.println("✅ Order " + order.getOrderId() + " has been confirmed.");
        System.out.println("Client: " + branch.getOwner() + " will receive deliveries at " + branch.getAddress() + ".");
        branch.contract(order);
    }

    @Override
    public void sendDenial(Order order, Branches branch) {
        System.out.println("❌ Order " + order.getOrderId() + " has been denied. Please contact +96176888999.");
    }

    @Override
    public void sendUpcomingDelivery(Order order, Branches branch) {
        if (!order.getStatus().equals("CONFIRMED")) return;

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<LocalDate> dates = order.getDeliveryDates();
        for (LocalDate date : dates) {
            if (date.isEqual(today)) {
                System.out.println(" TODAY: Delivery for " + branch.getOwner() + " at " + branch.getAddress() + ". Order: " + order.getOrderId());
            } else if (date.isEqual(tomorrow)) {
                System.out.println("TOMORROW: Delivery for " + branch.getOwner().getName() + " at " + branch.getAddress() + ". Order: " + order.getOrderId());
            }
        }
    }
}
