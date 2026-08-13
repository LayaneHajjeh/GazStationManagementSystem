package petrotech;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Order {
    private LocalDate contractDate;
    private List<LocalDate> deliveryDates;
    private final String orderId;
    private double fuelAmount;
    private String fuelType;
    private double frequency; // deliveries per week
    private boolean deliveryCar;
    private long validTill; // years
    private String status;

    private static int nextOrderId = 1;

    public Order(double fuelAmount, String fuelType, double frequency, boolean deliveryCar, long validTill) {
        this.fuelAmount = fuelAmount;
        this.fuelType = fuelType;
        this.frequency = frequency;
        this.deliveryCar = deliveryCar;
        this.validTill = validTill;
        this.status = "PENDING";
        this.orderId = generateOrderId();
        this.contractDate = LocalDate.now();
        this.deliveryDates = new ArrayList<>();
    }

    public Order(Order other) {
        this.orderId = generateOrderId();
        this.fuelAmount = other.fuelAmount;
        this.fuelType = other.fuelType;
        this.frequency = other.frequency;
        this.deliveryCar = other.deliveryCar;
        this.status = other.status;
        this.contractDate = other.contractDate;
        this.deliveryDates = new ArrayList<>(other.deliveryDates);
    }

    public void orderSummary(Branches branches) {
        System.out.println("Order ID: " + getOrderId() + "\n" +
                "\nClient: " + branches.getOwner() + ", Branch: " + branches.getName() +
                "\nFuel Type: " + fuelType +
                "\nDelivery car needed: " + deliveryCar +
                "\nFuel amount per delivery: " + fuelAmount + " [Liters]" +
                "\nOrders per week: " + frequency +
                "\nNumber of orders: " + orderCount() +
                "\nDelivery: To " + branches.getAddress() + " between 8 am and 4 pm." +
                "\nValid till: " + validTill + " years (" + contractDate.plusYears(validTill) + ")" +
                "\nTotal per delivery: " + branches.total(this) + "$\n");
    }

    public void modifyOrder(double newFuelAmount, double newFrequency, boolean newDeliveryCar, long newValidTill) {
        this.fuelAmount = newFuelAmount;
        this.frequency = newFrequency;
        this.deliveryCar = newDeliveryCar;
        this.validTill = newValidTill;
        this.status = "PENDING";
        System.out.println("Order " + orderId + " has been modified.");
    }

    private static String generateOrderId() {
        return "ORD-" + String.format("%04d", nextOrderId++);
    }

    public void confirmOrder() {
        if ("PENDING".equals(this.status)) {
            this.status = "CONFIRMED";
            calculateDeliveryDates();
            System.out.println("Order " + getOrderId() + " has been confirmed.");
        } else {
            System.out.println("Order " + this + " cannot be confirmed as its status is " + this.status + ".");
        }
    }

    public void denyOrder() {
        if ("PENDING".equals(this.status)) {
            this.status = "DENIED";
            System.out.println("Order " + getOrderId() + " has been denied.");
        } else {
            System.out.println("Order " + this + " cannot be denied as its status is " + this.status + ".");
        }
    }

    public double transportFees() {
        return deliveryCar ? 1.75 * fuelAmount : 0;
    }

    public void dueDates() {
        System.out.println("Delivery Dates:");
        if (deliveryDates.isEmpty()) {
            System.out.println("No deliveries scheduled.");
        } else {
            deliveryDates.forEach(date -> {
                String status = date.isBefore(LocalDate.now()) ? "Delivered ✅" : "Scheduled";
                System.out.println("Scheduled for: " + date + " (" + status + ")");
            });
        }
        System.out.println("---------------------");
    }

    public List<LocalDate> calculateDeliveryDates() {
        deliveryDates.clear();
        if ("CONFIRMED".equals(status)) {
            long daysBetweenDeliveries = Math.round(7.0 / frequency);
            LocalDate nextDate = contractDate;

            for (int i = 0; i < orderCount(); i++) {
                while (nextDate.getDayOfWeek() == DayOfWeek.SATURDAY || nextDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    nextDate = nextDate.plusDays(1);
                }
                deliveryDates.add(nextDate);
                nextDate = nextDate.plusDays(daysBetweenDeliveries);
            }
        }
        return deliveryDates;
    }

    public int orderCount() {
        return (int) ((52 * validTill) * frequency);
    }

    // Getters and toString
    public String getOrderId() { return orderId; }
    public List<LocalDate> getDeliveryDates() { return deliveryDates; }
    public String getStatus() { return status; }
    public double getFuelAmount() { return fuelAmount; }
    public String getFuelType() { return fuelType; }
    public double getFrequency() { return frequency; }
    public boolean isDeliveryCar() { return deliveryCar; }
    public long getValidTill() { return validTill; }

    @Override
    public String toString() {
        return "Order ID: " + orderId;
    }
}