
package petrotech;

public abstract class Notifications {
    public abstract void sendConfirmation(Order order, Branches branch);
    public abstract void sendDenial(Order order, Branches branch);
    public abstract void sendUpcomingDelivery(Order order, Branches branch);
}
