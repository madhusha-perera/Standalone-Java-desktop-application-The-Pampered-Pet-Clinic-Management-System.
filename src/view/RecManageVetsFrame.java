package view;

import controller.ReceptionistController;
import model.Veterinarian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecManageVetsFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManageVetsFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Manage Vets");
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

        // Row 0: Vet Code + Search
        JLabel lCode = new JLabel("Vet Code:");
        JTextField tfCode = new JTextField();
        JButton btnSearch = new JButton("Search");
        tfCode.setFont(tfCode.getFont().deriveFont(16f));
        btnSearch.setFont(btnSearch.getFont().deriveFont(16f));
        btnSearch.setPreferredSize(new Dimension(100,  thirtyFive()));

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0; root.add(lCode, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.weightx = 1; root.add(tfCode, c);

        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 2; c.weightx = 0; root.add(btnSearch, c);

        // Rows 1–7: other fields
        String[] labels = {
            "First Name:", "Last Name:", "Specialty:",
            "Phone #:",    "Username:", "Password:",
            "DOB (yyyy-MM-dd):", "Address:"
        };
        JComponent[] fields = {
            new JTextField(), new JTextField(), new JTextField(),
            new JTextField(), new JTextField(), new JPasswordField(),
            new JTextField(), new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(16f));
            lbl.setForeground(Color.black);
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 0; c.gridy = i + 1; c.weightx = 0;
            root.add(lbl, c);

            JComponent fld = fields[i];
            fld.setFont(fld.getFont().deriveFont(16f));
            fld.setPreferredSize(new Dimension(450,  thirtyFive()));
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            c.gridx = 1; c.gridwidth = 2; c.weightx = 1;
            root.add(fld, c);
            c.gridwidth = 1;
        }

        // Row 8: action buttons
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
        c.gridx = 0; c.gridy = labels.length + 1; c.gridwidth = 3;
        root.add(actionPanel, c);

        // Handlers
        btnSearch.addActionListener(e -> {
            Veterinarian v = ctrl.getVetByCode(tfCode.getText().trim());
            if (v != null) {
                ((JTextField)fields[0]).setText(v.getFname());
                ((JTextField)fields[1]).setText(v.getLname());
                ((JTextField)fields[2]).setText(v.getSpecialization());
                ((JTextField)fields[3]).setText(v.getPhoneNumber());
                ((JTextField)fields[4]).setText(v.getUsername());
                ((JPasswordField)fields[5]).setText(v.getPassword());
                ((JTextField)fields[6]).setText(
                    new SimpleDateFormat("yyyy-MM-dd").format(v.getDob()));
                ((JTextField)fields[7]).setText(v.getAddress());
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Vet not found",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        btnCreate.addActionListener(e -> {
            // Step 1: Get all inputs
    String fname = ((JTextField)fields[0]).getText().trim();
    String lname = ((JTextField)fields[1]).getText().trim();
    String specialization = ((JTextField)fields[2]).getText().trim();
    String phone = ((JTextField)fields[3]).getText().trim();
    String username = ((JTextField)fields[4]).getText().trim();
    String password = new String(((JPasswordField)fields[5]).getPassword());
    String dobText = ((JTextField)fields[6]).getText().trim();
    String address = ((JTextField)fields[7]).getText().trim();

    // Step 2: Validate inputs
    if (fname.isEmpty() || lname.isEmpty() || specialization.isEmpty() ||
        phone.isEmpty() || username.isEmpty() || password.isEmpty() ||
        dobText.isEmpty() || address.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!phone.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!fname.matches("[a-zA-Z]+") || !lname.matches("[a-zA-Z]+")) {
        JOptionPane.showMessageDialog(this, "Names must contain only letters.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Step 3: Parse date
    Date ud;
    try {
        ud = new SimpleDateFormat("yyyy-MM-dd").parse(dobText);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Date Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Step 4: Create Veterinarian object
    Veterinarian v = new Veterinarian();
    v.setUsername(username);
    v.setPassword(password);
    v.setFname(fname);
    v.setLname(lname);
    v.setSpecialization(specialization);
    v.setPhoneNumber(phone);
    v.setDob(ud);
    v.setAddress(address);

    // Step 5: Save to database
    boolean ok = ctrl.createVet(v);
    JOptionPane.showMessageDialog(
        this,
        ok ? "Vet created" : "Creation failed",
        ok ? "Success" : "Error",
        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
    );
});
        btnUpdate.addActionListener(e -> {
            try {
                Date ud = new SimpleDateFormat("yyyy-MM-dd")
                              .parse(((JTextField)fields[6]).getText().trim());
                Veterinarian v = new Veterinarian();
                v.setVetCode(tfCode.getText().trim());
                v.setUsername(((JTextField)fields[4]).getText().trim());
                v.setPassword(new String(
                    ((JPasswordField)fields[5]).getPassword()));
                v.setFname(((JTextField)fields[0]).getText().trim());
                v.setLname(((JTextField)fields[1]).getText().trim());
                v.setSpecialization(((JTextField)fields[2]).getText().trim());
                v.setPhoneNumber(((JTextField)fields[3]).getText().trim());
                v.setDob(ud);
                v.setAddress(((JTextField)fields[7]).getText().trim());
                boolean ok = ctrl.updateVet(v);
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Vet updated" : "Update failed",
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
            boolean ok = ctrl.deleteVet(tfCode.getText().trim());
            JOptionPane.showMessageDialog(
                this,
                ok ? "Vet deleted" : "Delete failed",
                ok ? "Success" : "Error",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
        });

        btnBack.addActionListener(e -> {
            dispose();
            new RecDashboardFrame(recCode, isHead).setVisible(true);
        });

        setContentPane(root);
    }

    private int thirtyFive() { return 35; }
    private int forty()      { return 40; }
}