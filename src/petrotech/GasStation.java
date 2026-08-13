package petrotech;


import java.util.HashMap;
import java.util.Map;

public class GasStation {
    private String name;
    private String email;
    private String adminUsername;
    private String adminPassword;
    private Map<String, Branches> branches = new HashMap<>();
    private Map<String, SalesManager> salesManagers = new HashMap<>();
    private Map<String, String> truckDrivers = new HashMap<>();

    public GasStation(String name, String email, String adminUsername, String adminPassword) {
        this.name = name;
        this.email = email;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public void addBranches(Branches branch) {
        if (!branches.containsKey(branch.getName())) {
            branches.put(branch.getName(), branch);
        }
    }

    public Branches getBranches(String name) {
        return branches.get(name);
    }

    public Map<String, Branches> getAllBranches() {
        return branches;
    }

    public void addSalesManager(String username, SalesManager manager) {
        this.salesManagers.put(username, manager);
    }

    public Map<String, SalesManager> getSalesManagers() {
        return this.salesManagers;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setTruckDrivers(Map<String, String> truckDrivers) {
        this.truckDrivers = truckDrivers;
    }

    public Map<String, String> getTruckDrivers() {
        return truckDrivers;
    }
}