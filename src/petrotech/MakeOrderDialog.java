
package petrotech;

   

import petrotech.Branches;
import petrotech.Order;
import petrotech.SalesManager; // Import SalesManager
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MakeOrderDialog extends JDialog implements ActionListener, DocumentListener {

    private JTextField fuelAmountField;
    private JComboBox<String> fuelTypeComboBox;
    private JComboBox<Double> frequencyComboBox;
    private JCheckBox deliveryCarCheckBox;
    private JComboBox<Long> validityComboBox;
    private JButton placeOrderButton;
    private JButton cancelButton;
    private Branches branch;
    private JLabel totalCostLabel;

    public MakeOrderDialog(JFrame owner, Branches branch) {
        super(owner, "Make a New Order", true);
        this.branch = branch;
        setLayout(new GridLayout(7, 2, 10, 10)); // Increased rows for total cost
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        JLabel fuelAmountLabel = new JLabel("Fuel Amount (Liters):");
        fuelAmountLabel.setFont(largerFont);
        fuelAmountField = new JTextField(10);
        fuelAmountField.setFont(largerFont);
        fuelAmountField.getDocument().addDocumentListener(this); // Listen for changes
        add(fuelAmountLabel);
        add(fuelAmountField);

        JLabel fuelTypeLabel = new JLabel("Fuel Type:");
        fuelTypeLabel.setFont(largerFont);
        fuelTypeComboBox = new JComboBox<>(new String[]{"petrol", "diesel"});
        fuelTypeComboBox.setFont(largerFont);
        fuelTypeComboBox.addActionListener(this); // Listen for changes
        add(fuelTypeLabel);
        add(fuelTypeComboBox);

        JLabel frequencyLabel = new JLabel("Orders per Week:");
        frequencyLabel.setFont(largerFont);
        frequencyComboBox = new JComboBox<>(new Double[]{1.0, 2.0, 3.0, 7.0});
        frequencyComboBox.setFont(largerFont);
        add(frequencyLabel);
        add(frequencyComboBox);

        JLabel deliveryCarLabel = new JLabel("Delivery Car Needed:");
        deliveryCarLabel.setFont(largerFont);
        deliveryCarCheckBox = new JCheckBox();
        deliveryCarCheckBox.setFont(largerFont);
        add(deliveryCarLabel);
        add(deliveryCarCheckBox);

        JLabel validityLabel = new JLabel("Validity (Years):");
        validityLabel.setFont(largerFont);
        validityComboBox = new JComboBox<>(new Long[]{1L, 2L, 3L, 5L});
        validityComboBox.setFont(largerFont);
        add(validityLabel);
        add(validityComboBox);

        JLabel totalCostStaticLabel = new JLabel("Total Cost per Delivery:");
        totalCostStaticLabel.setFont(largerFont);
        add(totalCostStaticLabel);

        totalCostLabel = new JLabel("$0.00"); // Initial value
        totalCostLabel.setFont(largerFont);
        add(totalCostLabel);

        placeOrderButton = new JButton("Place Order");
        placeOrderButton.setFont(largerFont);
        placeOrderButton.addActionListener(this);
        add(placeOrderButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(largerFont);
        cancelButton.addActionListener(this);
        add(cancelButton);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void calculateTotal() {
    try {
        double fuelAmount = Double.parseDouble(fuelAmountField.getText());
        String fuelType = (String) fuelTypeComboBox.getSelectedItem();
        double pricePerLiter = 0.0;
        double transportFee = 0.0;

        if (fuelType != null) {
            if (fuelType.equals("petrol")) {
                pricePerLiter = SalesManager.getPetrolPrice();
            } else if (fuelType.equals("diesel")) {
                pricePerLiter = SalesManager.getDieselPrice();
            }
        }

        if (deliveryCarCheckBox.isSelected()) { // aam tenzed bl notif bas mesh bl service
            transportFee = 1.75 * fuelAmount; // in cents
            transportFee /= 100; // Convert cents to dollars
        }

        double total = (fuelAmount * pricePerLiter) + transportFee;

        totalCostLabel.setText(String.format("$%.2f", total));

    } catch (NumberFormatException ex) {
        totalCostLabel.setText("Invalid Amount");
    }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == placeOrderButton) {
            try {
                double fuelAmount = Double.parseDouble(fuelAmountField.getText());
                String fuelType = (String) fuelTypeComboBox.getSelectedItem();
                double frequency = (Double) frequencyComboBox.getSelectedItem();
                boolean deliveryCar = deliveryCarCheckBox.isSelected();
                long validTill = (Long) validityComboBox.getSelectedItem();

                if (fuelAmount > 0 && frequency > 0 && validTill > 0 && fuelType != null && !fuelType.isEmpty()) {
                    Order newOrder = new Order(fuelAmount, fuelType, frequency, deliveryCar, validTill);
                    branch.makeAnOrder(newOrder);
                    JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields with valid values.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid fuel amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == cancelButton) {
            dispose();
        } else if (e.getSource() == fuelTypeComboBox) {
            calculateTotal(); // Recalculate when fuel type changes
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        calculateTotal(); // Recalculate when fuel amount changes
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        calculateTotal(); // Recalculate when fuel amount changes
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Not needed for JTextField
    }
}