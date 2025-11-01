package view;

import controller.ReceptionistController;
import model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecManageApptsFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManageApptsFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Manage Appointments");
        this.recCode = recCode;
        this.isHead  = isHead;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(new Color(245, 245, 245));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- NORTH: search panel ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets(5, 5, 5, 5);
        sc.fill = GridBagConstraints.NONE;

        JLabel lblAppt = new JLabel("Appt Code:");
        JTextField tfAppt = new JTextField();
        tfAppt.setPreferredSize(new Dimension(200,  thirtyFive()));
        JButton btnSearchAppt = new JButton("Search Appt");
        btnSearchAppt.setPreferredSize(new Dimension(120, thirtyFive()));

        JLabel lblOwner = new JLabel("Owner Code:");
        JTextField tfOwner = new JTextField();
        tfOwner.setPreferredSize(new Dimension(200,  thirtyFive()));
        JButton btnSearchOwner = new JButton("Search Owner");
        btnSearchOwner.setPreferredSize(new Dimension(120, thirtyFive()));

        sc.gridx = 0; sc.gridy = 0; sc.anchor = GridBagConstraints.EAST;
        searchPanel.add(lblAppt, sc);
        sc.gridx = 1; sc.anchor = GridBagConstraints.WEST;
        searchPanel.add(tfAppt, sc);
        sc.gridx = 2; 
        searchPanel.add(btnSearchAppt, sc);

        sc.gridx = 0; sc.gridy = 1; sc.anchor = GridBagConstraints.EAST;
        searchPanel.add(lblOwner, sc);
        sc.gridx = 1; sc.anchor = GridBagConstraints.WEST;
        searchPanel.add(tfOwner, sc);
        sc.gridx = 2;
        searchPanel.add(btnSearchOwner, sc);

        root.add(searchPanel, BorderLayout.NORTH);

        // --- CENTER: list of appointments ---
        DefaultListModel<Appointment> model = new DefaultListModel<>();
        JList<Appointment> list = new JList<>(model);
        list.setFont(list.getFont().deriveFont(14f));
        list.setVisibleRowCount(10);
        list.setCellRenderer((l, a, idx, sel, foc) -> {
            String text = String.format(
                "%s | %s | %s → %s | %s",
                a.getAppointmentCode(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(a.getDatetime()),
                a.getOwnerCode(),
                a.getPetCode(),
                a.getStatus()
            );
            JLabel lbl = new JLabel(text);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? new Color(200, 215, 235) : Color.white);
            lbl.setFont(lbl.getFont().deriveFont(14f));
            return lbl;
        });
        JScrollPane scroll = new JScrollPane(list);
        root.add(scroll, BorderLayout.CENTER);

        // --- SOUTH: action buttons ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBackground(new Color(245, 245, 245));

        JButton btnNew    = makeButton("New");
        JButton btnUpdate = makeButton("Update");
        JButton btnDelete = makeButton("Delete");
        JButton btnBack   = makeButton("Back");

        btnPanel.add(btnNew);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        root.add(btnPanel, BorderLayout.SOUTH);

        // --- Load initial list (all for head, or for this rec) ---
        List<Appointment> appts = isHead
            ? ctrl.getAppointmentsByOwnerCode("") 
            : ctrl.getAppointmentsByOwnerCode(recCode);
        appts.forEach(model::addElement);

        // --- Actions ---
        btnSearchAppt.addActionListener(e -> {
            model.clear();
            Appointment a = ctrl.getAppointmentByCode(tfAppt.getText().trim());
            if (a != null) model.addElement(a);
            else JOptionPane.showMessageDialog(
                this, "Appointment not found", "Search", JOptionPane.WARNING_MESSAGE
            );
        });

        btnSearchOwner.addActionListener(e -> {
            model.clear();
            List<Appointment> listByOwner = ctrl.getAppointmentsByOwnerCode(tfOwner.getText().trim());
            if (listByOwner.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "No appointments for that owner", "Search", JOptionPane.WARNING_MESSAGE
                );
            } else {
                listByOwner.forEach(model::addElement);
            }
        });

        btnNew.addActionListener(e -> {
            new RecManageApptsCreateFrame(recCode, isHead).setVisible(true);
            dispose();
        });
        btnUpdate.addActionListener(e -> {
            Appointment a = list.getSelectedValue();
            if (a != null) {
                new RecManageApptsUpdateFrame(recCode, isHead, a).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Select an appointment to update", "Info", JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        btnDelete.addActionListener(e -> {
            Appointment a = list.getSelectedValue();
            if (a != null) {
                boolean ok = ctrl.cancelAppointment(a.getAppointmentCode());
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Deleted" : "Failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
                if (ok) model.removeElement(a);
            } else {
                JOptionPane.showMessageDialog(
                    this, "Select an appointment to delete", "Info", JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        btnBack.addActionListener(e -> {
            dispose();
            new RecDashboardFrame(recCode, isHead).setVisible(true);
        });

        setContentPane(root);
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(16f));
        b.setBackground(new Color(200, 200, 200));
        b.setForeground(Color.black);
        return b;
    }

    // helper for text-field height
    private int thirtyFive() {
        return 35;
    }
}
