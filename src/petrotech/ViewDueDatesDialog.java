package petrotech;

import petrotech.Branches;
import petrotech.Order;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewDueDatesDialog extends JDialog {

    public ViewDueDatesDialog(JFrame owner, Branches branch) {
        super(owner, "Upcoming Delivery Dates - " + branch.getName(), true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        List<LocalDate> allDeliveryDates = getAllDeliveryDates(branch);

        if (allDeliveryDates.isEmpty()) {
            JLabel noDatesLabel = new JLabel("No upcoming delivery dates scheduled for this branch.");
            noDatesLabel.setFont(largerFont);
            noDatesLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(noDatesLabel, BorderLayout.CENTER);
        } else {
            DefaultListModel<String> datesListModel = new DefaultListModel<>();
            for (LocalDate date : allDeliveryDates) {
                datesListModel.addElement(date.toString());
            }
            JList<String> datesList = new JList<>(datesListModel);
            datesList.setFont(largerFont);
            add(new JScrollPane(datesList), BorderLayout.CENTER);
        }

        JButton closeButton = new JButton("Close");
        closeButton.setFont(largerFont);
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private List<LocalDate> getAllDeliveryDates(Branches branch) {
        List<LocalDate> deliveryDates = new ArrayList<>();
        for (Order order : branch.getAllOrder()) {
            if (order.getStatus().equals("CONFIRMED")) {
                deliveryDates.addAll(order.getDeliveryDates());
            }
        }
        // Sort the dates for better readability
        deliveryDates.sort(LocalDate::compareTo);
        return deliveryDates;
    }
}
