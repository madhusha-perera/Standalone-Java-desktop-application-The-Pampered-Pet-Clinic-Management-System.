package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.OwnerController;

public class OwnerDashboardFrame extends JFrame {
    private final String ownerCode;
    private final OwnerController ctrl = new OwnerController();

    public OwnerDashboardFrame(String ownerCode) {
        super("Owner Dashboard — " + ownerCode);
        this.ownerCode = ownerCode;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Light gray background for consistency
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(60));

        panel.add(makeButton("View My Pets", e -> {
            new OwnerViewPetsFrame(ownerCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Register a New Pet", e -> {
            new OwnerRegisterPetFrame(ownerCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Request Appointment", e -> {
            new OwnerRequestApptFrame(ownerCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalGlue());

        panel.add(makeButton("Log Out", e -> {
            dispose();
            new LoginFrame().setVisible(true);
        }));
        panel.add(Box.createVerticalStrut(40));

        setContentPane(panel);
    }

    private JButton makeButton(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setPreferredSize(new Dimension(400, 60));
        b.setMaximumSize(new Dimension(400, 60));
        b.setFont(b.getFont().deriveFont(18f));
        b.setBackground(new Color(200, 200, 200));
        b.setForeground(Color.black);
        b.addActionListener(al);
        return b;
    }
}
