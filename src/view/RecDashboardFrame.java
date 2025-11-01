package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecDashboardFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;

    public RecDashboardFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " Dashboard — " + recCode);
        this.recCode = recCode;
        this.isHead  = isHead;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Consistent light gray background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(60));

        panel.add(makeButton("Manage Appointments", e -> {
            new RecManageApptsFrame(recCode, isHead).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Manage Pets", e -> {
            new RecManagePetsFrame(recCode, isHead).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Manage Owners", e -> {
            new RecManageOwnersFrame(recCode, isHead).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        panel.add(makeButton("Manage Vets", e -> {
            new RecManageVetsFrame(recCode, isHead).setVisible(true);
            dispose();
        }));
        panel.add(Box.createVerticalStrut(20));

        if (isHead) {
            panel.add(makeButton("Manage Receptionists", e -> {
                new RecManageRecsFrame(recCode).setVisible(true);
                dispose();
            }));
            panel.add(Box.createVerticalStrut(20));
        }

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
        b.setFont(b.getFont().deriveFont(Font.PLAIN, 18f));
        b.setBackground(new Color(200, 200, 200));
        b.setForeground(Color.black);
        b.addActionListener(al);
        return b;
    }
}