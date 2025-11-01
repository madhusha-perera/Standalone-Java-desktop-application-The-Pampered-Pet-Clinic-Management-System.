package view;

import controller.OwnerController;
import model.Owner;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class OwnerSignupFrame extends JFrame {
    private final OwnerController ctrl = new OwnerController();

    public OwnerSignupFrame() {
        super("Pampered Pet Clinic — Sign Up");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        var panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints c = new GridBagConstraints();

        // tighter insets: 8px horizontal, 12px vertical
        c.insets = new Insets(12, 8, 12, 8);

        String[] labels = {
            "Username:", "Password:", "First Name:", "Last Name:",
            "Email Address:", "Phone Number:", "Address:"
        };
        JComponent[] fields = {
            new JTextField(),
            new JPasswordField(),
            new JTextField(),
            new JTextField(),
            new JTextField(),
            new JTextField(),
            new JTextField()
        };

        // STEP 1: add label + field rows
        for (int i = 0; i < labels.length; i++) {
            // common label config
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(lbl.getFont().deriveFont(18f));
            lbl.setForeground(Color.black);

            // place label: column 0, weightx=0, no fill
            c.gridx = 0;
            c.gridy = i;
            c.weightx = 0;
            c.fill    = GridBagConstraints.NONE;
            c.anchor  = GridBagConstraints.EAST;
            panel.add(lbl, c);

            // place field: column 1, weightx=1, horizontal fill
            JComponent fld = fields[i];
            fld.setFont(fld.getFont().deriveFont(16f));
            fld.setPreferredSize(new Dimension(650, 40));
            fld.setForeground(Color.black);

            c.gridx = 1;
            c.weightx = 1.0;
            c.fill    = GridBagConstraints.HORIZONTAL;
            c.anchor  = GridBagConstraints.WEST;
            panel.add(fld, c);
        }

        // STEP 2: buttons row
        JButton btnSubmit = new JButton("Sign Up");
        JButton btnBack   = new JButton("Back");
        Dimension btnSize = new Dimension(180, 50);
        for (JButton b : new JButton[]{btnSubmit, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(18f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // Submit button at (0, rowCount)
        int row = labels.length;
        c.fill    = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx   = 0;
        c.gridy   = row;
        c.anchor  = GridBagConstraints.CENTER;
        panel.add(btnSubmit, c);

        // Back button at (1, rowCount)
        c.gridx = 1;
        panel.add(btnBack, c);

        // STEP 3: wiring
        btnSubmit.addActionListener(e -> {
            String username = ((JTextField)    fields[0]).getText().trim();
            String password = new String(((JPasswordField)fields[1]).getPassword());
            String fname    = ((JTextField)    fields[2]).getText().trim();
            String lname    = ((JTextField)    fields[3]).getText().trim();
            String email    = ((JTextField)    fields[4]).getText().trim();
            String phone    = ((JTextField)    fields[5]).getText().trim();
            String address  = ((JTextField)    fields[6]).getText().trim();

            if (username.isEmpty() || password.isEmpty() ||
                fname.isEmpty()    || lname.isEmpty()    ||
                email.isEmpty()    || phone.isEmpty()    ||
                address.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$", email)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid email format.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!Pattern.matches("^\\+?[0-9\\- ]{7,15}$", phone)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid phone number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Owner o = new Owner();
            o.setUsername(username);
            o.setPassword(password);
            o.setFname(fname);
            o.setLname(lname);
            o.setEmail(email);
            o.setPhoneNumber(phone);
            o.setAddress(address);

            boolean created = ctrl.createOwner(o);
            if (created) {
                JOptionPane.showMessageDialog(
                    this,
                    "Sign up successful! You may now log in.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Sign up failed—username or email may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        setContentPane(panel);
    }
}