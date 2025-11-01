package view;

import controller.VetController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VetDashboardFrame extends JFrame {
    private final String vetCode;
    private final VetController ctrl = new VetController();

    public VetDashboardFrame(String vetCode) {
        super("Vet Dashboard — " + vetCode);
        this.vetCode = vetCode;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245)); // light gray
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(60));

        panel.add(makeButton("Search Pet", e -> {
            new VetSearchPetFrame(vetCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("View Appointments", e -> {
            new VetViewApptsFrame(vetCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Add Medical Record", e -> {
            new VetAddRecordFrame(vetCode).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalGlue());

        panel.add(makeButton("Log Out", e -> {
            dispose();
            new LoginFrame().setVisible(true);
        }));
        panel.add(Box.createVerticalStrut(60));

        setContentPane(panel);
    }

    private JButton makeButton(String text, ActionListener al) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setPreferredSize(new Dimension(400, 60));
        b.setMaximumSize(new Dimension(400, 60));
        b.setFont(b.getFont().deriveFont(18f));
        b.setBackground(new Color(200, 200, 200)); // neutral gray
        b.setForeground(Color.black);
        b.addActionListener(al);
        return b;
    }
}
