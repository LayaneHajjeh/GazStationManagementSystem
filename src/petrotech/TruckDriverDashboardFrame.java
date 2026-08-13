package petrotech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class TruckDriverDashboardFrame extends JFrame implements ActionListener {

    private JButton prevWeekButton, nextWeekButton, logoutButton;
    private JTextArea deliveriesTextArea;
    private LocalDate currentWeek;

    private final GasStation gasStation;

    public TruckDriverDashboardFrame(GasStation gasStation) {
        this.gasStation = gasStation;
        currentWeek = LocalDate.now().with(DayOfWeek.MONDAY);

        setTitle("Truck Driver Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setFont(new Font("Arial", Font.PLAIN, 16));

        // Week Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout());
        prevWeekButton = new JButton("<< Previous Week");
        nextWeekButton = new JButton("Next Week >>");
        logoutButton = new JButton("Logout");

        prevWeekButton.addActionListener(this);
        nextWeekButton.addActionListener(this);
        logoutButton.addActionListener(this);

        navPanel.add(prevWeekButton);
        navPanel.add(nextWeekButton);
        navPanel.add(logoutButton);

        // Deliveries display area
        deliveriesTextArea = new JTextArea(15, 40);
        deliveriesTextArea.setFont(new Font("Arial", Font.PLAIN, 15));
        deliveriesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(deliveriesTextArea);

        add(navPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        updateDeliveriesView();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateDeliveriesView() {
        deliveriesTextArea.setText("Deliveries for week starting: " + currentWeek.format(DateTimeFormatter.ISO_DATE) + "\n\n");

        boolean foundDeliveries = false;

        for (int i = 0; i < 7; i++) {
            LocalDate date = currentWeek.plusDays(i);
            deliveriesTextArea.append(date.getDayOfWeek() + " " + date + ":\n");

            boolean dayHasDelivery = false;

            for (Branches branch : gasStation.getAllBranches().values()) {
                for (Order order : branch.getAllOrder()) {
                    if (order.getStatus().equals("CONFIRMED") && order.getDeliveryDates().contains(date)) {
                        deliveriesTextArea.append("  - Order ID: " + order.getOrderId() + ", Branch: " + branch.getName() + "\n");
                        foundDeliveries = true;
                        dayHasDelivery = true;
                    }
                }
            }

            if (!dayHasDelivery) {
                deliveriesTextArea.append("  (no deliveries)\n");
            }
        }

        if (!foundDeliveries) {
            deliveriesTextArea.append("\nNo deliveries scheduled for this entire week.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevWeekButton) {
            currentWeek = currentWeek.minusWeeks(1);
            updateDeliveriesView();
        } else if (e.getSource() == nextWeekButton) {
            currentWeek = currentWeek.plusWeeks(1);
            updateDeliveriesView();
        } else if (e.getSource() == logoutButton) {
            SwingUtilities.invokeLater(() -> new LoginFrame(
                    Collections.singletonList(gasStation),
                    gasStation.getSalesManagers(),
                    gasStation.getTruckDrivers()
            ));
            dispose();
        }
    }
}
