
package petrotech;

import java.time.LocalDate;
import java.util.List;


public abstract class Employee {
    private final String employeeId;
    private final String name;
    private final String username;
private final String password;
    
    
   public Employee(String employeeId, String name, String username,String password) {
        this.employeeId = employeeId;
        this.name = name;
        this.password=password;
        this.username=username;
    }  
  
      public abstract void viewDeliveriesOnDate(LocalDate date, List<Branches> allBranches);

    public String getName() {
        return name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

   

}
