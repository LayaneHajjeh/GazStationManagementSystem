package petrotech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map; // Import Map

public class ClientDashboardFrame extends JFrame implements ActionListener {

    private GasStation loggedInGasStation;
    private JLabel welcomeLabel;
    private JComboBox<Branches> branchComboBox;
    private JButton makeOrderButton;
    private JButton modifyOrderButton;
    private JButton viewDueDatesButton;
    private JButton notificationsButton;
    private JButton returnButton;
    
        public ClientDashboardFrame(GasStation gasStation) {
        this.loggedInGasStation = gasStation;
        setTitle("Client Dashboard - " + loggedInGasStation.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        welcomeLabel = new JLabel("Welcome, " + loggedInGasStation.getName() + "!");
        welcomeLabel.setFont(largerFont);
        add(welcomeLabel);

        JLabel branchLabel = new JLabel("Select Branch:");
        branchLabel.setFont(largerFont);
        add(branchLabel);

       branchComboBox = new JComboBox<>(loggedInGasStation.getAllBranches().values().toArray(new Branches[0])); // ✅ Correct
        branchComboBox.setFont(largerFont);
        add(branchComboBox);

        makeOrderButton = new JButton("Make an Order");
        makeOrderButton.setFont(largerFont);
        makeOrderButton.addActionListener(this);
        add(makeOrderButton);

        modifyOrderButton = new JButton("Modify Order");
        modifyOrderButton.setFont(largerFont);
        modifyOrderButton.addActionListener(this);
        add(modifyOrderButton);

        viewDueDatesButton = new JButton("View Due Dates");
        viewDueDatesButton.setFont(largerFont);
        viewDueDatesButton.addActionListener(this);
        add(viewDueDatesButton);

        notificationsButton = new JButton("Notifications");
        notificationsButton.setFont(largerFont);
        notificationsButton.addActionListener(this);
        add(notificationsButton);

        returnButton = new JButton("Return to Login"); // Create the button
        returnButton.setFont(largerFont);
        returnButton.addActionListener(this); // Add action listener
        add(returnButton); // Add it to the frame

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

@Override
public void actionPerformed(ActionEvent e) {
    Branches selectedBranch = (Branches) branchComboBox.getSelectedItem();
    if (selectedBranch == null && e.getSource() != returnButton) {
        JOptionPane.showMessageDialog(this, "Please select a branch.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (e.getSource() == makeOrderButton) {
        MakeOrderDialog makeOrderDialog = new MakeOrderDialog(this, selectedBranch);
        makeOrderDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Simulate upcoming delivery notification after making an order
                if (selectedBranch.getAllOrder().size() > 0) {
                    Order lastOrder = selectedBranch.getAllOrder().get(selectedBranch.getAllOrder().size() - 1);
                    lastOrder.calculateDeliveryDates(); // Ensure delivery dates are calculated
                    NotificationService.sendUpcomingDelivery(lastOrder, selectedBranch);
                }
                // The confirmation notification will now be triggered by the Sales Manager's actions (to be implemented later)
            }
        });
    } else if (e.getSource() == modifyOrderButton) {
        new ModifyOrderDialog(this, selectedBranch);
    } else if (e.getSource() == viewDueDatesButton) {
        new ViewDueDatesDialog(this, selectedBranch);
    } else if (e.getSource() == notificationsButton) {
        new NotificationsDialog(this);
} else if (e.getSource() == returnButton) {
    SwingUtilities.invokeLater(() -> new LoginFrame(
        PetroTech.getAllGasStations(),
        loggedInGasStation.getSalesManagers(),
        loggedInGasStation.getTruckDrivers()
    ));
    this.dispose();
}


    }
}


