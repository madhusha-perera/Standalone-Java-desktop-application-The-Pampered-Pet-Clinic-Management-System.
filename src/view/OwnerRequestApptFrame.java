package view;

import controller.OwnerController;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OwnerRequestApptFrame extends JFrame {
    private final String ownerCode;
    private final OwnerController ctrl = new OwnerController();

    public OwnerRequestApptFrame(String ownerCode) {
        super("Request Appointment — " + ownerCode);
        this.ownerCode = ownerCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Light gray background for consistency
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);

        // Labels
        JLabel lblPet    = new JLabel("Pet Code:");
        JLabel lblWhen   = new JLabel("Date & Time (yyyy‑MM‑dd HH:mm):");
        JLabel lblReason = new JLabel("Reason:");
        for (JLabel lbl : new JLabel[]{lblPet, lblWhen, lblReason}) {
            lbl.setFont(lbl.getFont().deriveFont(18f));
            lbl.setForeground(Color.black);
        }

        // Inputs
        JTextField tfPet    = new JTextField();
        JTextField tfWhen   = new JTextField();
        JTextField tfReason = new JTextField();

        // Layout labels
        c.fill   = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.gridx  = 0; c.gridy = 0; form.add(lblPet,    c);
        c.gridy = 1;           form.add(lblWhen,  c);
        c.gridy = 2;           form.add(lblReason,c);

        // Layout fields — make them wider by allowing horizontal growth
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.anchor  = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.gridx   = 1;

        c.gridy = 0; form.add(tfPet,    c);
        c.gridy = 1; form.add(tfWhen,   c);
        c.gridy = 2; form.add(tfReason, c);

        // Buttons
        JButton btnRequest = new JButton("Request");
        JButton btnBack    = new JButton("Back");
        Dimension btnSize  = new Dimension(200, 50);
        for (JButton b : new JButton[]{btnRequest, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(16f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // Layout buttons
        c.fill    = GridBagConstraints.NONE;
        c.anchor  = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridy   = 3; c.gridx = 0; form.add(btnRequest, c);
                       c.gridx = 1; form.add(btnBack,    c);

        // Actions
        btnRequest.addActionListener(e -> {
            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                    .parse(tfWhen.getText().trim());
                java.sql.Timestamp ts = new java.sql.Timestamp(parsed.getTime());
                boolean ok = ctrl.requestAppointment(
                    ownerCode,
                    tfPet.getText().trim(),
                    ts,
                    tfReason.getText().trim()
                );
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Appointment Requested!" : "Request Failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please use the format yyyy‑MM‑dd HH:mm",
                    "Invalid Date/Time",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new OwnerDashboardFrame(ownerCode).setVisible(true);
        });

        setContentPane(form);
    }
}
