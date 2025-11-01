package view;

import controller.ReceptionistController;
import model.Owner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RecManageOwnersFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManageOwnersFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Manage Owners");
        this.recCode = recCode;
        this.isHead  = isHead;
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

        // Labels and fields
        JLabel lCode   = new JLabel("Owner Code:");
        JLabel lUser   = new JLabel("Username:");
        JLabel lPass   = new JLabel("Password:");
        JLabel lFname  = new JLabel("First Name:");
        JLabel lLname  = new JLabel("Last Name:");
        JLabel lEmail  = new JLabel("Email:");
        JLabel lAddr   = new JLabel("Address:");
        JLabel lPhone  = new JLabel("Phone #:");

        JTextField tfCode  = new JTextField();
        JTextField tfUser  = new JTextField();
        JPasswordField tfPass = new JPasswordField();
        JTextField tfFname = new JTextField();
        JTextField tfLname = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfAddr  = new JTextField();
        JTextField tfPhone = new JTextField();

        // Search button next to Owner Code
        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(100,  thirtyFive()));

        // Make all text fields fill horizontally
        c.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Owner Code + Search
        c.gridx = 0; c.gridy = 0; c.weightx = 0; root.add(lCode, c);
        c.gridx = 1; c.weightx = 1; root.add(tfCode, c);
        c.gridx = 2; c.weightx = 0; root.add(btnSearch, c);

        // Common: labels on col 0, fields on col 1 spanning two cols
        JTextField[] fields = { tfUser, tfPass, tfFname, tfLname, tfEmail, tfAddr, tfPhone };
        JLabel[] labels     = { lUser, lPass, lFname, lLname, lEmail, lAddr, lPhone };

        for (int i = 0; i < labels.length; i++) {
            c.gridy = i + 1;
            c.gridx = 0; c.weightx = 0; root.add(labels[i], c);
            c.gridx = 1; c.weightx = 1; root.add(fields[i], c);
        }

        // Buttons row
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnBack   = new JButton("Back");
        JButton[] actions = { btnCreate, btnUpdate, btnDelete, btnBack };

        for (JButton b : actions) {
            b.setPreferredSize(new Dimension(150,  forty()));
        }

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(root.getBackground());
        for (JButton b : actions) actionPanel.add(b);

        c.gridy = labels.length + 1;
        c.gridx = 0; c.gridwidth = 3; c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        root.add(actionPanel, c);

        // Action listeners
        btnSearch.addActionListener(e -> {
            Owner o = ctrl.getOwnerByCode(tfCode.getText().trim());
            if (o != null) {
                tfUser .setText(o.getUsername());
                tfPass .setText(o.getPassword());
                tfFname.setText(o.getFname());
                tfLname.setText(o.getLname());
                tfEmail.setText(o.getEmail());
                tfAddr .setText(o.getAddress());
                tfPhone.setText(o.getPhoneNumber());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Owner not found", "Search", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        

        btnCreate.addActionListener(e -> {
              String username = tfUser.getText().trim();
    String password = new String(tfPass.getPassword());
    String fname = tfFname.getText().trim();
    String lname = tfLname.getText().trim();
    String email = tfEmail.getText().trim();
    String address = tfAddr.getText().trim();
    String phone = tfPhone.getText().trim();

    // Validate email
    if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(this, "Invalid email format.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Validate phone number
    if (!isValidPhoneNumber(phone)) {
        JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // If all valid, proceed
    Owner o = new Owner(username, password, fname, lname, email, address, phone);
    boolean ok = ctrl.createOwner(o);

    JOptionPane.showMessageDialog(this,
        ok ? "Owner created" : "Creation failed",
        ok ? "Success" : "Error",
        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });  

        btnUpdate.addActionListener(e -> {
            Owner o = new Owner();
            o.setOwnerCode(tfCode.getText().trim());
            o.setUsername(tfUser.getText().trim());
            o.setPassword(new String(tfPass.getPassword()));
            o.setFname(tfFname.getText().trim());
            o.setLname(tfLname.getText().trim());
            o.setEmail(tfEmail.getText().trim());
            o.setAddress(tfAddr.getText().trim());
            o.setPhoneNumber(tfPhone.getText().trim());
            boolean ok = ctrl.updateOwner(o);
            JOptionPane.showMessageDialog(this,
                ok ? "Owner updated" : "Update failed",
                ok ? "Success" : "Error",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });

        btnDelete.addActionListener(e -> {
            boolean ok = ctrl.deleteOwner(tfCode.getText().trim());
            JOptionPane.showMessageDialog(this,
                ok ? "Owner deleted" : "Delete failed",
                ok ? "Success" : "Error",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });

        btnBack.addActionListener(e -> {
            dispose();
            new RecDashboardFrame(recCode, isHead).setVisible(true);
        });

        setContentPane(root);
    }
    
    
    private boolean isValidEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
}

    private boolean isValidPhoneNumber(String phone) {
    return phone.matches("\\d{10}"); // Exactly 10 digits
}

    private int thirtyFive() { return 35; }
    private int forty()      { return 40; }
}