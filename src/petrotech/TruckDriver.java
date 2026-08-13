
package petrotech;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class TruckDriver extends Employee {
    private List<Order> orders;
private String carPlate;
    
    public TruckDriver(String username, String password, String employeeId, String name,String carPlate) {
        super(employeeId, name ,username,password);
        this.orders=new ArrayList<>();
        this.carPlate=carPlate;
    }


        @Override
    public void viewDeliveriesOnDate(LocalDate date, List<Branches> allBranches) {
        System.out.println("Deliveries scheduled for " + date + " (Viewed by Truck Driver " + getName() + "):");
        boolean foundOrders = false;

            for (int i = 0; i < allBranches.size(); i++) {
        Branches branch = allBranches.get(i);
            if (branch.getAllOrder() != null) {
               for (int j = 0; j < branch.getAllOrder().size(); j++) {
                Order order = branch.getAllOrder().get(j);

                    if (order.getStatus().equals("CONFIRMED")) {
                        
                        if (order.getDeliveryDates().isEmpty()) {
                        order.calculateDeliveryDates(); // Only calculate if not done
                    }
                        
                        List<LocalDate> deliveryDates = order.getDeliveryDates();
                       for (int k = 0; k < deliveryDates.size(); k++) {
                        LocalDate deliveryDate = deliveryDates.get(k);
                        if (deliveryDate.isEqual(date)) {
                            foundOrders = true;
                            System.out.println("  Branch: " + branch.getName() +
                                    ", Order ID: " + order.getOrderId());
                            break;
                        }
                        }
                    }
                }
            }
        }

        if (!foundOrders) {
            System.out.println("  No deliveries scheduled for this date.");
        }
    }
}


