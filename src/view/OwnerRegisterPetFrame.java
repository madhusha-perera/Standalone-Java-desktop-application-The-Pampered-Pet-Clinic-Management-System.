package view;

import controller.OwnerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OwnerRegisterPetFrame extends JFrame {
    private final String ownerCode;
    private final OwnerController ctrl = new OwnerController();

    public OwnerRegisterPetFrame(String ownerCode) {
        super("Register New Pet — " + ownerCode);
        this.ownerCode = ownerCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Consistent light gray background
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.fill = GridBagConstraints.NONE;

        // Labels
        JLabel lName    = new JLabel("Name:");
        JLabel lSpecies = new JLabel("Species:");
        JLabel lBreed   = new JLabel("Breed:");
        JLabel lGender  = new JLabel("Gender (M/F):");
        JLabel lDob     = new JLabel("DOB (yyyy-MM-dd):");

        for (JLabel lbl : new JLabel[]{lName, lSpecies, lBreed, lGender, lDob}) {
            lbl.setFont(lbl.getFont().deriveFont(18f));
            lbl.setForeground(Color.black);
        }

        // Fields
        JTextField tName    = new JTextField();
        JTextField tSpecies = new JTextField();
        JTextField tBreed   = new JTextField();
        JTextField tGender  = new JTextField();
        JTextField tDob     = new JTextField();

        Dimension fieldSize = new Dimension(450,  Forty());
        for (JTextField tf : new JTextField[]{tName, tSpecies, tBreed, tGender, tDob}) {
            tf.setPreferredSize(fieldSize);
            tf.setFont(tf.getFont().deriveFont(16f));
            tf.setForeground(Color.black);
        }

        // Buttons
        JButton btnRegister = new JButton("Register");
        JButton btnBack     = new JButton("Back");

        Dimension btnSize = new Dimension(200, Fifty());
        for (JButton b : new JButton[]{btnRegister, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(16f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // Layout
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0; form.add(lName,    c);
        c.gridy = 1;          form.add(lSpecies, c);
        c.gridy = 2;          form.add(lBreed,   c);
        c.gridy = 3;          form.add(lGender,  c);
        c.gridy = 4;          form.add(lDob,     c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.gridy = 0; form.add(tName,    c);
        c.gridy = 1;            form.add(tSpecies, c);
        c.gridy = 2;            form.add(tBreed,   c);
        c.gridy = 3;            form.add(tGender,  c);
        c.gridy = 4;            form.add(tDob,     c);

        c.gridy = 5; c.gridx = 0; form.add(btnRegister, c);
                     c.gridx = 1; form.add(btnBack,     c);

        // Actions
        btnRegister.addActionListener(e -> {
            try {
                Date ud = new SimpleDateFormat("yyyy-MM-dd")
                                  .parse(tDob.getText().trim());
                java.sql.Date sd = new java.sql.Date(ud.getTime());
                boolean ok = ctrl.registerPet(
                    ownerCode,
                    tName.getText().trim(),
                    tSpecies.getText().trim(),
                    tBreed.getText().trim(),
                    tGender.getText().trim().charAt(0),
                    sd
                );
                JOptionPane.showMessageDialog(
                  this,
                  ok ? "Pet registered!" : "Registration failed",
                  "Result",
                  ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                  this,
                  "Invalid date format. Use yyyy-MM-dd",
                  "Error",
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

    // Helpers for consistent sizing
    private int Forty() { return fortyHeight; }
    private int Fifty()  { return fiftyHeight;  }

    private static final int fortyHeight = 40;
    private static final int fiftyHeight  = 50;
}
