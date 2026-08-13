package petrotech;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import petrotech.NotificationService;

public class NotificationsDialog extends JDialog {

    private JButton closeButton; // Declare closeButton at the class level

    public NotificationsDialog(JFrame owner) {
        super(owner, "Notifications", true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font largerFont = new Font("Arial", Font.PLAIN, 16);

        closeButton = new JButton("Close"); // Initialize closeButton here
        closeButton.setFont(largerFont);
        closeButton.addActionListener(e -> dispose());

        List<String> notifications = NotificationService.getAllNotifications();

        if (notifications.isEmpty()) {
            JLabel noNotificationsLabel = new JLabel("No new notifications.");
            noNotificationsLabel.setFont(largerFont);
            noNotificationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(noNotificationsLabel, BorderLayout.CENTER);
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(closeButton);
            add(buttonPanel, BorderLayout.SOUTH);
        } else {
            DefaultListModel<String> notificationsListModel = new DefaultListModel<>();
            for (String notification : notifications) {
                notificationsListModel.addElement(notification);
            }
            JList<String> notificationsList = new JList<>(notificationsListModel);
            notificationsList.setFont(largerFont);
            add(new JScrollPane(notificationsList), BorderLayout.CENTER);

            JButton clearButton = new JButton("Clear All");
            clearButton.setFont(largerFont);
            clearButton.addActionListener(e -> {
                NotificationService.clearNotifications();
                DefaultListModel<String> model = (DefaultListModel<String>) ((JList) ((JScrollPane) getContentPane().getComponent(0)).getViewport().getView()).getModel();
                model.clear();
                JLabel noNotificationsLabel = new JLabel("No new notifications.");
                noNotificationsLabel.setFont(largerFont);
                noNotificationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                getContentPane().removeAll();
                getContentPane().add(noNotificationsLabel, BorderLayout.CENTER);
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(closeButton); // Now closeButton is always initialized
                getContentPane().add(buttonPanel, BorderLayout.SOUTH);
                revalidate();
                repaint();
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(clearButton);
            buttonPanel.add(closeButton); // And here as well
            add(buttonPanel, BorderLayout.SOUTH);
        }

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}