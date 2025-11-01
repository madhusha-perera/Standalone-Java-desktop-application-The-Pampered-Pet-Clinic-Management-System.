package view;

import controller.ReceptionistController;
import model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecManageApptsUpdateFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final Appointment appt;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManageApptsUpdateFrame(String recCode, boolean isHead, Appointment appt) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Update Appointment");
        this.recCode = recCode;
        this.isHead  = isHead;
        this.appt    = appt;
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

        // Labels & initial values
        String[] labelTexts = {
            "Appointment Code:", "Owner Code:", "Pet Code:",
            "Vet Code:", "Date & Time (yyyy-MM-dd HH:mm):",
            "Reason:", "Status:"
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

        // Fields
        JTextField tfCode   = new JTextField(appt.getAppointmentCode());
        tfCode.setEditable(false);
        JTextField tfOwner  = new JTextField(appt.getOwnerCode());
        JTextField tfPet    = new JTextField(appt.getPetCode());
        JTextField tfVet    = new JTextField(appt.getVetCode());
        JTextField tfWhen   = new JTextField(
            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(appt.getDatetime()));
        JTextField tfReason = new JTextField(appt.getReason());
        JComboBox<String> cbStatus = new JComboBox<>(
            new String[]{"REQUESTED","SCHEDULED","COMPLETED","CANCELLED"});
        cbStatus.setSelectedItem(appt.getStatus());

        JComponent[] fields = {
            tfCode, tfOwner, tfPet, tfVet, tfWhen, tfReason, cbStatus
        };

        for (int i = 0; i < fields.length; i++) {
            JComponent fld = fields[i];
            fld.setFont(fld.getFont().deriveFont(16f));
            if (fld instanceof JTextField || fld instanceof JComboBox) {
                c.anchor = GridBagConstraints.WEST;
                c.fill   = GridBagConstraints.HORIZONTAL;
                c.weightx = 1.0;
                c.gridx = 1; c.gridy = i;
                form.add(fld, c);
            }
        }

        // Buttons
        JButton btnUpdate = new JButton("Update");
        JButton btnBack   = new JButton("Back");
        btnUpdate.setFont(btnUpdate.getFont().deriveFont(16f));
        btnBack  .setFont(btnBack  .getFont().deriveFont(16f));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(form.getBackground());
        btnPanel.add(btnUpdate);
        btnPanel.add(btnBack);

        c.fill      = GridBagConstraints.NONE;
        c.anchor    = GridBagConstraints.CENTER;
        c.weightx   = 0;
        c.gridx     = 0;
        c.gridy     = fields.length;
        c.gridwidth = 2;
        form.add(btnPanel, c);

        // Actions
        btnUpdate.addActionListener(e -> {
            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                                      .parse(tfWhen.getText().trim());
                java.sql.Timestamp ts = new java.sql.Timestamp(parsed.getTime());
                boolean ok = ctrl.updateAppointment(
                    appt.getAppointmentCode(),
                    tfOwner.getText().trim(),
                    tfPet.getText().trim(),
                    tfVet.getText().trim(),
                    ts,
                    tfReason.getText().trim(),
                    cbStatus.getSelectedItem().toString()
                );
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Appointment updated!" : "Update failed!",
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