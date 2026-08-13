
package petrotech;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;


public class SalesManager extends Employee {

      
 public static double petrolPrice;
public static double dieselPrice;
 private List<Order> orders;
 

    public SalesManager(String username, String password, String name,String employeeId) {
        super(employeeId, name ,username,password);
        this.orders=new ArrayList<>();
    }
      
 public void confirmOrder(Branches branch, Order order) {

        if (branch.getAllOrder().contains(order)) {
            order.confirmOrder();
        } else {
            System.out.println("Error: Order not found at branch " + branch.getName());
        }
    }

    public void denyOrder(Branches branch, Order order) {
        if (branch.getAllOrder().contains(order)) {
            order.denyOrder();
        } else {
            System.out.println("Error: Order not found at branch " + branch.getName());
        }
    }
     public static void setpetrolPrice(double petrolPrice) {
        SalesManager.petrolPrice = petrolPrice;
    }

    public static void setDieselPrice(double dieselPrice) {
        SalesManager.dieselPrice = dieselPrice;
    }

    
        @Override
    public void viewDeliveriesOnDate(LocalDate date, List<Branches> allBranches) {
        System.out.println("Deliveries scheduled for " + date + " (Viewed by Sales Manager " + getName() + "):");
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

    public static double getPetrolPrice() {
        return petrolPrice;
    }

    public static double getDieselPrice() {
        return dieselPrice;
    }
    
    
    
}
