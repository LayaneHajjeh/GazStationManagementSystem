package petrotech;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetroTech {

    private static final List<GasStation> allGasStations = new ArrayList<>();

    public static List<GasStation> getAllGasStations() {
        return allGasStations;
    }

    public static void main(String[] args) {
        // Create GasStations
        GasStation apec = new GasStation("Apec", "apec@gmail.com", "userApec", "asma");
        GasStation ojm = new GasStation("OJM", "ojm@gmail.com", "userOJM", "jdnv32jsn");

        // Create branches
        Branches a1 = new Branches(apec, "Apec-koura", "koura");
        Branches a2 = new Branches(apec, "Apec-tripoli", "tripoli");
        Branches a3 = new Branches(apec, "Apec-akkar", "akkar");
        Branches a4 = new Branches(apec, "Apec-denniyeh", "denniyeh");
        Branches o1 = new Branches(ojm, "OJM-tripoli", "tripoli");

        apec.addBranches(a1);
        apec.addBranches(a2);
        apec.addBranches(a3);
        apec.addBranches(a4);
        ojm.addBranches(o1);

        allGasStations.add(apec);
        allGasStations.add(ojm);

        // Create Sales Manager
        SalesManager mgr = new SalesManager("username", "password", "Alice", "ID23");
        SalesManager.setpetrolPrice(2.5);
        SalesManager.setDieselPrice(3.0);
        apec.addSalesManager("username", mgr);
        ojm.addSalesManager("username", mgr);

        // Create Truck Driver
        TruckDriver driver = new TruckDriver("driver1", "driverPass", "TD01", "Bob", "ABC-123");
        apec.getTruckDrivers().put("driver1", "driverPass");
        ojm.getTruckDrivers().put("driver1", "driverPass");

        // Seed sample orders (status: PENDING)
        Order sampleOrder1 = new Order(1000, "petrol", 1.0, true, 1);
        Order sampleOrder2 = new Order(1500, "diesel", 2.0, true, 1);

        a4.makeAnOrder(sampleOrder1); // Apec-denniyeh
        a2.makeAnOrder(sampleOrder2); // Apec-tripoli

        // Note: DO NOT confirm here, confirm via Sales Manager dashboard.

        // Login maps
        Map<String, SalesManager> salesManagerMap = new HashMap<>();
        salesManagerMap.put("username", mgr);

        Map<String, String> truckDriverMap = new HashMap<>();
        truckDriverMap.put("driver1", "driverPass");

        // Launch login interface
        SwingUtilities.invokeLater(() ->
            new LoginFrame(allGasStations, salesManagerMap, truckDriverMap)
        );
    }
}