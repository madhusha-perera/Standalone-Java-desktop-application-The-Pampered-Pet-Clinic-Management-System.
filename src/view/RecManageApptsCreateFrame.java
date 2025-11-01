package view;

import controller.ReceptionistController;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecManageApptsCreateFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManageApptsCreateFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — New Appointment");
        this.recCode = recCode;
        this.isHead  = isHead;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(15, 15, 15, 15);

        // Labels
        String[] labelTexts = {
            "Owner Code:", "Pet Code:", "Vet Code:",
            "Date & Time (yyyy-MM-dd HH:mm):", "Reason:"
        };
        JLabel[] labels = new JLabel[labelTexts.length];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel(labelTexts[i]);
            labels[i].setFont(labels[i].getFont().deriveFont(16f));
            c.anchor = GridBagConstraints.EAST;
            c.fill   = GridBagConstraints.NONE;
            c.gridx = 0; c.gridy = i;
            form.add(labels[i], c);
        }

        // Text fields
        JTextField tfOwner  = new JTextField();
        JTextField tfPet    = new JTextField();
        JTextField tfVet    = new JTextField();
        JTextField tfWhen   = new JTextField();
        JTextField tfReason = new JTextField();
        JTextField[] fields = { tfOwner, tfPet, tfVet, tfWhen, tfReason };

        for (int i = 0; i < fields.length; i++) {
            fields[i].setFont(fields[i].getFont().deriveFont(16f));
            c.anchor = GridBagConstraints.WEST;
            c.fill   = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.gridx = 1; c.gridy = i;
            form.add(fields[i], c);
        }

        // Buttons
        JButton btnCreate = new JButton("Create");
        JButton btnBack   = new JButton("Back");
        btnCreate.setFont(btnCreate.getFont().deriveFont(16f));
        btnBack  .setFont(btnBack  .getFont().deriveFont(16f));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(form.getBackground());
        btnPanel.add(btnCreate);
        btnPanel.add(btnBack);

        c.fill   = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.gridx = 0; c.gridy = fields.length; c.gridwidth = 2;
        form.add(btnPanel, c);

        // Actions
        btnCreate.addActionListener(e -> {
            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                      .parse(tfWhen.getText().trim());
                java.sql.Timestamp ts = new java.sql.Timestamp(parsed.getTime());
                boolean ok = ctrl.createAppointment(
                    recCode,
                    tfOwner.getText().trim(),
                    tfPet.getText().trim(),
                    tfVet.getText().trim(),
                    ts,
                    tfReason.getText().trim()
                );
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Appointment Created!" : "Creation Failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
                if (ok) {
                    dispose();
                    new RecManageApptsFrame(recCode, isHead).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid date/time format. Use yyyy-MM-dd HH:mm",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new RecManageApptsFrame(recCode, isHead).setVisible(true);
        });

        setContentPane(form);
    }
}