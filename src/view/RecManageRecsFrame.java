package view;

import controller.HeadReceptionistController;
import model.Receptionist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecManageRecsFrame extends JFrame {
    private final String headCode;
    private final HeadReceptionistController ctrl = new HeadReceptionistController();

    public RecManageRecsFrame(String headCode) {
        super("Head Receptionist — Manage Receptionists");
        this.headCode = headCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(245, 245, 245));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        // Row 0: Rec Code + Search
        JLabel lCode = new JLabel("Rec Code:");
        JTextField tfCode = new JTextField();
        JButton btnSearch = new JButton("Search");
        tfCode.setFont(tfCode.getFont().deriveFont(16f));
        btnSearch.setPreferredSize(new Dimension(100, thirtyFive()));

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0; root.add(lCode, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.weightx = 1; root.add(tfCode, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2; c.weightx = 0; root.add(btnSearch, c);

        // Rows 1–8: other fields
        String[] labels = {
            "Username:", "Password:", "First Name:", "Last Name:",
            "Role:",     "Phone #:",  "Address:",   "DOB (yyyy-MM-dd):"
        };
        Component[] fields = {
            new JTextField(), new JPasswordField(),
            new JTextField(), new JTextField(),
            new JComboBox<>(new String[]{"RECEPTIONIST","HEAD_RECEPTIONIST"}),
            new JTextField(), new JTextField(), new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(16f));
            lbl.setForeground(Color.black);
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 0; c.gridy = i + 1; c.weightx = 0;
            root.add(lbl, c);

            JComponent fld = (JComponent) fields[i];
            fld.setFont(fld.getFont().deriveFont(16f));
            fld.setPreferredSize(new Dimension(450, thirtyFive()));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1; c.gridwidth = 2; c.weightx = 1;
            root.add(fld, c);
            c.gridwidth = 1;
        }

        // Row 9: action buttons
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnBack   = new JButton("Back");
        JButton[] actions = { btnCreate, btnUpdate, btnDelete, btnBack };
        for (JButton b : actions) {
            b.setFont(b.getFont().deriveFont(16f));
            b.setPreferredSize(new Dimension(150, forty()));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(root.getBackground());
        for (JButton b : actions) actionPanel.add(b);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0; c.gridy = 9; c.gridwidth = 3;
        root.add(actionPanel, c);

        // Handlers
        btnSearch.addActionListener(e -> {
            Receptionist r = ctrl.getRecByCode(tfCode.getText().trim());
            if (r != null) {
                ((JTextField)fields[0]).setText(r.getUsername());
                ((JPasswordField) fields[1]).setText(r.getPassword());
                ((JTextField) fields[2]).setText(r.getFname());
                ((JTextField) fields[3]).setText(r.getLname());
                ((JComboBox<?>) fields[4]).setSelectedItem(r.getRole());
                ((JTextField) fields[5]).setText(r.getPhoneNumber());
                ((JTextField) fields[6]).setText(r.getAddress());
                ((JTextField) fields[7]).setText(
                    new SimpleDateFormat("yyyy-MM-dd").format(r.getDob()));
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Receptionist not found",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        btnCreate.addActionListener(e -> {
            try {
                Date ud = new SimpleDateFormat("yyyy-MM-dd")
                                      .parse(((JTextField)fields[7]).getText().trim());
                Receptionist r = new Receptionist();
                r.setUsername(((JTextField)fields[0]).getText().trim());
                r.setPassword(new String(
                  ((JPasswordField)fields[1]).getPassword()));
                r.setFname(((JTextField)fields[2]).getText().trim());
                r.setLname(((JTextField)fields[3]).getText().trim());
                r.setRole(((JComboBox<?>)fields[4]).getSelectedItem().toString());
                r.setPhoneNumber(((JTextField)fields[5]).getText().trim());
                r.setAddress(((JTextField)fields[6]).getText().trim());
                r.setDob(ud);
                boolean ok = ctrl.createReceptionist(headCode, r);
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Receptionist created" : "Creation failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Date ud = new SimpleDateFormat("yyyy-MM-dd")
                                      .parse(((JTextField)fields[7]).getText().trim());
                Receptionist r = new Receptionist();
                r.setReceptionistCode(tfCode.getText().trim());
                r.setUsername(((JTextField)fields[0]).getText().trim());
                r.setPassword(new String(
                  ((JPasswordField)fields[1]).getPassword()));
                r.setFname(((JTextField)fields[2]).getText().trim());
                r.setLname(((JTextField)fields[3]).getText().trim());
                r.setRole(((JComboBox<?>)fields[4]).getSelectedItem().toString());
                r.setPhoneNumber(((JTextField)fields[5]).getText().trim());
                r.setAddress(((JTextField)fields[6]).getText().trim());
                r.setDob(ud);
                boolean ok = ctrl.updateReceptionist(headCode, r);
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Receptionist updated" : "Update failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnDelete.addActionListener(e -> {
            boolean ok = ctrl.deleteReceptionist(headCode, tfCode.getText().trim());
            JOptionPane.showMessageDialog(
                this,
                ok ? "Receptionist deleted" : "Delete failed",
                ok ? "Success" : "Error",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
        });

        btnBack.addActionListener(e -> {
            dispose();
            new RecDashboardFrame(headCode, true).setVisible(true);
        });

        setContentPane(root);
    }

    private int thirtyFive() { return 35; }
    private int forty()      { return 40; }
}