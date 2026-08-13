package petrotech;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Branches {
 private final GasStation owner;
 private final String address;  
 private final String name; 
 private final List<Order> allOrder;
 private Notifications notificationService;

    public Branches(GasStation owner,String name,String address) {
this.owner=owner;
        this.address = address;
        this.name=name;
       this.allOrder= new ArrayList<>();
      this.notificationService = new InProgram();
    }
public static void viewScheduledOrdersForDay(LocalDate date, List<Branches> allBranches) {
    System.out.println("Orders scheduled for " + date + ":");
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
        System.out.println("  No orders scheduled for this day in any branch.");
    }
}

public void makeAnOrder(Order order){
    if(order.orderCount() > 0){
        this.allOrder.add(order); 
        System.out.println("Recurring order placed at " + this.name + ", status: " + order.getStatus());

        if(order.getStatus().equals("PENDING")) {
            System.out.println(order.orderCount() + " deliveries scheduled. Awaiting confirmation.");
        }
    }
}

  public double total(Order order) {
      try {
            double fuelPrice = getFuelPrice(order.getFuelType()); // might ykun fi ghlt, iza telee fi ghlt betruh aal catch
            return fuelPrice * order.getFuelAmount() + (order.transportFees() / 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error calculating total cost: " + e.getMessage());
            return 0; 
        }
    }

private double getFuelPrice(String fuelType) {
     if (fuelType == null || fuelType.isEmpty()) {
        throw new IllegalArgumentException("Error: Insert fuel type.");} //menwarji wen l ghlt mmken ykun 
    if (fuelType.equals("petrol")) {
        if (SalesManager.petrolPrice == 0.0) {
            throw new IllegalArgumentException("Error: Petrol price has not been set yet.");
        }
        return SalesManager.petrolPrice;
    } else {
        if (SalesManager.dieselPrice == 0.0) {
            throw new IllegalArgumentException("Error: Diesel price has not been set yet.");
        }
        return SalesManager.dieselPrice;
    }
}

  
    public void contract(Order order){
        System.out.println("FUEL ORDER CONTRACT\n" +
"\n" +
"This Fuel Order Contract is made on " +LocalDate.now()+" between PetroTech Supplier and "+name+" .\n" +
"\n" +
"1. Order\n" +
"\n" +
"Supplier confirms Client's order (Order ID: "+order.getOrderId()+" ).\n" +
"\n" +
"2. Order Details\n");
                order.orderSummary(this);
                       
 System.out.println("\n" +
"3. Payment\n" +
"\n" +
"Client will pay "+total(order)+" on each delivery. Late payments will cause order cancellation.\n" +
"\n" +
"4. Delivery\n" +
"\n" +
"Supplier will deliver the fuel to the address in Section 2.  Client must provide safe access.\n" +
"\n" +
"5. Fuel\n" +
"\n" +
"Supplier guarantees the fuel meets quality standards.\n" +
"\n" +
"6. Cancellation\n" +
"\n" +
"Client may cancel the order as per the following: Fill out a cancellation form in the \"modify order\" section before a week from delivery date.\n" +
"\n" +
"7. Important\n" +
"\n" +
"The fuel delivered yearly average is allowed to be less than or more than the quantity of the agreement by 15% .\n" +
"\n" +
"8. Other Terms\n" +
"\n" +
"This Contract is governed by the laws of Lebanon.\n" +
"\n" +
"This is the complete agreement.\n" +
"\n" +
"Any changes must be in writing and signed by both parties.\n" +
"\n" +
"Signatures\n" +
"\n" +
"Supplier\n" +
"\n" +
"PetroTech\n" +
"\n" +
"By CEO: _________________________ \n" +
"\n" +
"Signature: _________________________\n" +
"\n" +
"Client\n" +
"\n" +
owner+"\n" +
"\n" +
"By: _________________________\n" +
"\n" +
"Signature: _________________________\n");
    }
    
   public void notifyConfirmation(Order o) {
        notificationService.sendConfirmation(o, this);
    }

    public void notifyDenial(Order o) {
        notificationService.sendDenial(o, this);
    }

    public void notifyUpcomingDelivery(Order o) {
        notificationService.sendUpcomingDelivery(o, this);
    }

    
  @Override
public String toString() { // without it l branches ma byentaba3 bi esmu; 
    return name;
}
  public List<Order> getAllOrder() {
        return allOrder;
    }
    


    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }

    public GasStation getOwner() {
        return owner;
    }

 
}
