package petrotech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;

    private List<GasStation> gasStations;
    private Map<String, SalesManager> salesManagers;
    private Map<String, String> truckDrivers;

    public LoginFrame(List<GasStation> gasStations, Map<String, SalesManager> salesManagers, Map<String, String> truckDrivers) {
        this.gasStations = gasStations;
        this.salesManagers = salesManagers;
        this.truckDrivers = truckDrivers;

        setTitle("PetroTech Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(largerFont);
        usernameField = new JTextField(15);
        usernameField.setFont(largerFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(largerFont);
        passwordField = new JPasswordField(15);
        passwordField.setFont(largerFont);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(largerFont);
        roleComboBox = new JComboBox<>(new String[]{"Client", "Sales Manager", "Truck Driver"});
        roleComboBox.setFont(largerFont);

        loginButton = new JButton("Login");
        loginButton.setFont(largerFont);
        loginButton.addActionListener(this);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleComboBox);
        add(loginButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (selectedRole != null) {
            switch (selectedRole) {
                case "Client":
                    boolean clientLoginSuccessful = false;
                    for (GasStation station : gasStations) {
                        if (station.getAdminUsername().equals(username) && station.getAdminPassword().equals(password)) {
                            SwingUtilities.invokeLater(() -> new ClientDashboardFrame(station));
                            dispose();
                            clientLoginSuccessful = true;
                            break;
                        }
                    }
                    if (!clientLoginSuccessful) {
                        JOptionPane.showMessageDialog(this, "Invalid client credentials. Client login failed.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "Sales Manager":
                    if (salesManagers.containsKey(username)) {
                        SalesManager manager = salesManagers.get(username);
                        if (manager.getPassword().equals(password)) {
                            GasStation associatedStation = gasStations.get(0); // Replace with actual logic if needed
                            SwingUtilities.invokeLater(() -> new SalesManagerDashboardFrame(associatedStation));
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid sales manager password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid sales manager username.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

             case "Truck Driver":
    if (truckDrivers.containsKey(username) && truckDrivers.get(username).equals(password)) {
        GasStation associatedStation = gasStations.get(0); // or any logic to find the matching one
        SwingUtilities.invokeLater(() -> new TruckDriverDashboardFrame(associatedStation)); // ✅ this is now correct
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "Invalid truck driver credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    break;


            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a role.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
