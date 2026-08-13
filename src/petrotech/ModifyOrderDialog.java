package petrotech;

import petrotech.Branches;
import petrotech.Order;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;

public class ModifyOrderDialog extends JDialog implements ActionListener, ListSelectionListener {

    private JList<Order> orderList;
    private DefaultListModel<Order> orderListModel;
    private JTextField fuelAmountField;
    private JComboBox<String> fuelTypeComboBox;
    private JComboBox<Double> frequencyComboBox;
    private JCheckBox deliveryCarCheckBox;
    private JComboBox<Long> validityComboBox;
    private JButton requestModificationButton; // Renamed button
    private JButton cancelButton;
    private Branches branch;
    private Order selectedOrder;

    public ModifyOrderDialog(JFrame owner, Branches branch) {
        super(owner, "Request Order Modification", true); // Updated title
        this.branch = branch;
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        // Panel for the list of orders
        JPanel listPanel = new JPanel(new BorderLayout());
        JLabel orderListLabel = new JLabel("Select Order to Modify:");
        orderListLabel.setFont(largerFont);
        listPanel.add(orderListLabel, BorderLayout.NORTH);

        orderListModel = new DefaultListModel<>();
        List<Order> orders = branch.getAllOrder();
        for (Order order : orders) {
            orderListModel.addElement(order);
        }
        orderList = new JList<>(orderListModel);
        orderList.setFont(largerFont);
        orderList.addListSelectionListener(this);
        listPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        add(listPanel, BorderLayout.WEST);

        // Panel for order details
        JPanel detailsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JLabel fuelAmountLabel = new JLabel("Fuel Amount (Liters):");
        fuelAmountLabel.setFont(largerFont);
        fuelAmountField = new JTextField(10);
        fuelAmountField.setFont(largerFont);
        detailsPanel.add(fuelAmountLabel);
        detailsPanel.add(fuelAmountField);

        JLabel fuelTypeLabel = new JLabel("Fuel Type:");
        fuelTypeLabel.setFont(largerFont);
        fuelTypeComboBox = new JComboBox<>(new String[]{"petrol", "diesel"});
        fuelTypeComboBox.setFont(largerFont);
        detailsPanel.add(fuelTypeLabel);
        detailsPanel.add(fuelTypeComboBox);

        JLabel frequencyLabel = new JLabel("Orders per Week:");
        frequencyLabel.setFont(largerFont);
        frequencyComboBox = new JComboBox<>(new Double[]{1.0, 2.0, 3.0, 7.0});
        frequencyComboBox.setFont(largerFont);
        detailsPanel.add(frequencyLabel);
        detailsPanel.add(frequencyComboBox);

        JLabel deliveryCarLabel = new JLabel("Delivery Car Needed:");
        deliveryCarLabel.setFont(largerFont);
        deliveryCarCheckBox = new JCheckBox();
        deliveryCarCheckBox.setFont(largerFont);
        detailsPanel.add(deliveryCarLabel);
        detailsPanel.add(deliveryCarCheckBox);

        JLabel validityLabel = new JLabel("Validity (Years):");
        validityLabel.setFont(largerFont);
        validityComboBox = new JComboBox<>(new Long[]{1L, 2L, 3L, 5L});
        validityComboBox.setFont(largerFont);
        detailsPanel.add(validityLabel);
        detailsPanel.add(validityComboBox);

        requestModificationButton = new JButton("Request Modification"); // Renamed button
        requestModificationButton.setFont(largerFont);
        requestModificationButton.addActionListener(this);
        detailsPanel.add(requestModificationButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(largerFont);
        cancelButton.addActionListener(this);
        detailsPanel.add(cancelButton);

        add(detailsPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(owner);
        setOrderDetailsEnabled(false);
        setVisible(true);
    }

    private void setOrderDetailsEnabled(boolean enabled) {
        fuelAmountField.setEnabled(enabled);
        fuelTypeComboBox.setEnabled(enabled);
        frequencyComboBox.setEnabled(enabled);
        deliveryCarCheckBox.setEnabled(enabled);
        validityComboBox.setEnabled(enabled);
        requestModificationButton.setEnabled(enabled); // Enable/disable the request button
    }

    private void populateOrderDetails(Order order) {
        if (order != null) {
            fuelAmountField.setText(String.valueOf(order.getFuelAmount()));
            fuelTypeComboBox.setSelectedItem(order.getFuelType());
            frequencyComboBox.setSelectedItem(order.getFrequency());
            deliveryCarCheckBox.setSelected(order.isDeliveryCar());
            validityComboBox.setSelectedItem(order.getValidTill());
            setOrderDetailsEnabled(true);
        } else {
            fuelAmountField.setText("");
            fuelTypeComboBox.setSelectedIndex(0);
            frequencyComboBox.setSelectedIndex(0);
            deliveryCarCheckBox.setSelected(false);
            validityComboBox.setSelectedIndex(0);
            setOrderDetailsEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == requestModificationButton) {
            if (selectedOrder != null) {
                try {
                    double newFuelAmount = Double.parseDouble(fuelAmountField.getText());
                    String newFuelType = (String) fuelTypeComboBox.getSelectedItem();
                    double newFrequency = (Double) frequencyComboBox.getSelectedItem();
                    boolean newDeliveryCar = deliveryCarCheckBox.isSelected();
                    long newValidTill = (Long) validityComboBox.getSelectedItem();

                    if (newFuelAmount > 0 && newFrequency > 0 && newValidTill > 0 && newFuelType != null && !newFuelType.isEmpty()) {
                        // In a real application, you would create a ModificationRequest object
                        // and send it to the system for the Sales Manager to review.
                        String message = "Modification requested for Order ID: " + selectedOrder.getOrderId() + "\n" +
                                "Requested Fuel Amount: " + newFuelAmount + "\n" +
                                "Requested Fuel Type: " + newFuelType + "\n" +
                                "Requested Frequency: " + newFrequency + "\n" +
                                "Requested Delivery Car: " + newDeliveryCar + "\n" +
                                "Requested Validity: " + newValidTill + " years";
                        JOptionPane.showMessageDialog(this, "Modification request sent to Sales Manager for Order ID: " + selectedOrder.getOrderId(), "Request Sent", JOptionPane.INFORMATION_MESSAGE);

                        // Optionally, disable the fields after sending the request
                        setOrderDetailsEnabled(false);
                        requestModificationButton.setEnabled(false);
                        selectedOrder = null; // Deselect after request
                        populateOrderDetails(null);

                    } else {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields with valid values.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order to modify.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            dispose();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            selectedOrder = orderList.getSelectedValue();
            populateOrderDetails(selectedOrder);
        }
    }
}