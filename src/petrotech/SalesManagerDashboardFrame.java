package petrotech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SalesManagerDashboardFrame extends JFrame implements ActionListener {

    private final GasStation gasStation;
    private final SalesManager salesManager;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private JPanel menuPanel, fuelPanel, ordersPanel, deliveryPanel;

    // Fuel Panel components
    private JTextField petrolPriceField, dieselPriceField;
    private JButton savePricesButton, backFromFuel;

    // Orders Panel components
    private DefaultListModel<Order> pendingOrderListModel;
    private JList<Order> pendingOrderList;
    private JButton confirmOrderButton, denyOrderButton, backFromOrders;

    // Delivery Panel components (week navigator)
    private LocalDate currentWeekStart;
    private JButton prevWeekButton, nextWeekButton;
    private JLabel weekLabel;
    private JTextArea deliveriesTextArea;
    private JButton backFromDelivery;

    public SalesManagerDashboardFrame(GasStation gasStation) {
        this.gasStation = gasStation;
        this.salesManager = gasStation.getSalesManagers().values().iterator().next();

        setTitle("Sales Manager Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupMenuPanel();
        setupFuelPanel();
        setupOrdersPanel();
        setupDeliveryPanel();
        initializeWeek();

        add(mainPanel);
        setVisible(true);
    }

    private void setupMenuPanel() {
        menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JButton fuelButton = new JButton("Manage Fuel Prices");
        JButton ordersButton = new JButton("View Pending Orders");
        JButton deliveryButton = new JButton("View Weekly Deliveries");
        JButton logoutButton = new JButton("Logout");

        fuelButton.addActionListener(e -> cardLayout.show(mainPanel, "fuel"));
        ordersButton.addActionListener(e -> {
            updatePendingOrdersList();
            cardLayout.show(mainPanel, "orders");
        });
        deliveryButton.addActionListener(e -> cardLayout.show(mainPanel, "deliveries"));
        logoutButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new LoginFrame(
                java.util.Collections.singletonList(gasStation),
                gasStation.getSalesManagers(),
                gasStation.getTruckDrivers()
            ));
            dispose();
        });

        menuPanel.add(fuelButton);
        menuPanel.add(ordersButton);
        menuPanel.add(deliveryButton);
        menuPanel.add(logoutButton);

        mainPanel.add(menuPanel, "menu");
    }

    private void setupFuelPanel() {
        fuelPanel = new JPanel(new GridBagLayout());
        fuelPanel.setBorder(BorderFactory.createTitledBorder("Fuel Price Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        fuelPanel.add(new JLabel("Petrol Price:"), gbc);
        gbc.gridx = 1;
        petrolPriceField = new JTextField("2.3", 10);
        fuelPanel.add(petrolPriceField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        fuelPanel.add(new JLabel("Diesel Price:"), gbc);
        gbc.gridx = 1;
        dieselPriceField = new JTextField("1.4", 10);
        fuelPanel.add(dieselPriceField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        savePricesButton = new JButton("Save Prices");
        savePricesButton.addActionListener(this);
        fuelPanel.add(savePricesButton, gbc);

        gbc.gridy = 3;
        backFromFuel = new JButton("Go Back");
        backFromFuel.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        fuelPanel.add(backFromFuel, gbc);

        mainPanel.add(fuelPanel, "fuel");
    }

    private void setupOrdersPanel() {
        ordersPanel = new JPanel(new BorderLayout(10, 10));
        ordersPanel.setBorder(BorderFactory.createTitledBorder("Pending Orders"));

        pendingOrderListModel = new DefaultListModel<>();
        pendingOrderList = new JList<>(pendingOrderListModel);
        ordersPanel.add(new JScrollPane(pendingOrderList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        confirmOrderButton = new JButton("Confirm");
        denyOrderButton = new JButton("Deny");
        confirmOrderButton.addActionListener(this);
        denyOrderButton.addActionListener(this);
        buttons.add(confirmOrderButton);
        buttons.add(denyOrderButton);
        ordersPanel.add(buttons, BorderLayout.SOUTH);

        backFromOrders = new JButton("Go Back");
        backFromOrders.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        ordersPanel.add(backFromOrders, BorderLayout.NORTH);

        mainPanel.add(ordersPanel, "orders");
    }

    private void setupDeliveryPanel() {
        deliveryPanel = new JPanel(new BorderLayout(10,10));
        deliveryPanel.setBorder(BorderFactory.createTitledBorder("Weekly Deliveries"));

        JPanel navPanel = new JPanel(new FlowLayout());
        prevWeekButton = new JButton("‹ Previous Week");
        nextWeekButton = new JButton("Next Week ›");
        weekLabel       = new JLabel();

        prevWeekButton.addActionListener(this);
        nextWeekButton.addActionListener(this);

        navPanel.add(prevWeekButton);
        navPanel.add(weekLabel);
        navPanel.add(nextWeekButton);

        deliveriesTextArea = new JTextArea(10, 30);
        deliveriesTextArea.setEditable(false);

        backFromDelivery = new JButton("Go Back");
        backFromDelivery.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        deliveryPanel.add(navPanel, BorderLayout.NORTH);
        deliveryPanel.add(new JScrollPane(deliveriesTextArea), BorderLayout.CENTER);
        deliveryPanel.add(backFromDelivery, BorderLayout.SOUTH);

        mainPanel.add(deliveryPanel, "deliveries");
    }

    private void initializeWeek() {
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        showWeekDeliveries();
    }

    private void showWeekDeliveries() {
        LocalDate weekEnd = currentWeekStart.plusDays(6);
        weekLabel.setText(currentWeekStart + " – " + weekEnd);
        deliveriesTextArea.setText("");

        for (LocalDate day = currentWeekStart; !day.isAfter(weekEnd); day = day.plusDays(1)) {
            deliveriesTextArea.append(day + ":\n");
            boolean any = false;

            for (Branches branch : gasStation.getAllBranches().values()) {
                for (Order order : branch.getAllOrder()) {
                    if ("CONFIRMED".equals(order.getStatus()) && order.getDeliveryDates().contains(day)) {
                        deliveriesTextArea.append(
                            "  • Order " + order.getOrderId() + " @ " + branch.getName() + "\n"
                        );
                        any = true;
                    }
                }
            }
            if (!any) {
                deliveriesTextArea.append("  (no deliveries)\n");
            }
        }
    }

    private void updatePendingOrdersList() {
        pendingOrderListModel.clear();
        gasStation.getAllBranches().values().forEach(branch ->
            branch.getAllOrder().stream()
                .filter(order -> "PENDING".equals(order.getStatus()))
                .forEach(pendingOrderListModel::addElement)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Fuel price save
        if (src == savePricesButton) {
            try {
                SalesManager.setpetrolPrice(Double.parseDouble(petrolPriceField.getText()));
                SalesManager.setDieselPrice(Double.parseDouble(dieselPriceField.getText()));
                JOptionPane.showMessageDialog(this, "Prices updated.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input for price.");
            }
        }
        
        // Confirm / Deny orders
        else if (src == confirmOrderButton || src == denyOrderButton) {
            Order selectedOrder = pendingOrderList.getSelectedValue();
            if (selectedOrder != null) {
                for (Branches branch : gasStation.getAllBranches().values()) {
                    if (branch.getAllOrder().contains(selectedOrder)) {
                        if (src == confirmOrderButton) {
                            selectedOrder.confirmOrder();
                            NotificationService.sendConfirmation(selectedOrder, branch);
                        } else {
                            selectedOrder.denyOrder();
                            NotificationService.sendDenial(selectedOrder, branch);
                        }
                    }
                }
                updatePendingOrdersList();
            }
        }

        // Week navigation
        else if (src == prevWeekButton) {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            showWeekDeliveries();
        }
        else if (src == nextWeekButton) {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            showWeekDeliveries();
        }
    }
}