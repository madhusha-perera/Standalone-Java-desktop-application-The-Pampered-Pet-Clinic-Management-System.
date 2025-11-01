package view;

import controller.ReceptionistController;
import model.Pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecManagePetsCreateFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManagePetsCreateFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Create Pet");
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
        c.fill   = GridBagConstraints.NONE;

        // Labels
        JLabel lOwner = new JLabel("Owner Code:");
        JLabel lName  = new JLabel("Name:");
        JLabel lSpecies = new JLabel("Species:");
        JLabel lBreed   = new JLabel("Breed:");
        JLabel lGender  = new JLabel("Gender (M/F):");
        JLabel lDob     = new JLabel("DOB (yyyy-MM-dd):");
        for (JLabel lbl : new JLabel[]{lOwner, lName, lSpecies, lBreed, lGender, lDob}) {
            lbl.setFont(lbl.getFont().deriveFont(18f));
            lbl.setForeground(Color.black);
        }

        // Text fields
        Dimension fieldDim = new Dimension(400, 35);
        JTextField tfOwner   = new JTextField(); tfOwner.setPreferredSize(fieldDim);
        JTextField tfName    = new JTextField(); tfName.setPreferredSize(fieldDim);
        JTextField tfSpecies = new JTextField(); tfSpecies.setPreferredSize(fieldDim);
        JTextField tfBreed   = new JTextField(); tfBreed.setPreferredSize(fieldDim);
        JTextField tfGender  = new JTextField(); tfGender.setPreferredSize(new Dimension(80, 35));
        JTextField tfDob     = new JTextField(); tfDob.setPreferredSize(new Dimension(200, 35));

        for (JTextField tf : new JTextField[]{tfOwner, tfName, tfSpecies, tfBreed, tfGender, tfDob}) {
            tf.setFont(tf.getFont().deriveFont(16f));
            tf.setForeground(Color.black);
        }

        // Buttons
        JButton btnCreate = new JButton("Create");
        JButton btnBack   = new JButton("Back");
        Dimension btnDim = new Dimension(150, 40);
        for (JButton b : new JButton[]{btnCreate, btnBack}) {
            b.setPreferredSize(btnDim);
            b.setFont(b.getFont().deriveFont(16f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // Layout
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0; form.add(lOwner,   c);
        c.gridy = 1;          form.add(lName,    c);
        c.gridy = 2;          form.add(lSpecies, c);
        c.gridy = 3;          form.add(lBreed,   c);
        c.gridy = 4;          form.add(lGender,  c);
        c.gridy = 5;          form.add(lDob,     c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.gridy = 0; form.add(tfOwner,   c);
        c.gridy = 1;            form.add(tfName,    c);
        c.gridy = 2;            form.add(tfSpecies, c);
        c.gridy = 3;            form.add(tfBreed,   c);
        c.gridy = 4;            form.add(tfGender,  c);
        c.gridy = 5;            form.add(tfDob,     c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 6; c.gridx = 0; form.add(btnCreate, c);
                     c.gridx = 1; form.add(btnBack,   c);

        // Actions
        btnCreate.addActionListener(e -> {
            try {
                Date ud = new SimpleDateFormat("yyyy-MM-dd")
                              .parse(tfDob.getText().trim());
                java.sql.Date sd = new java.sql.Date(ud.getTime());
                boolean ok = ctrl.createPet(
                    tfOwner.getText().trim(),
                    tfName.getText().trim(),
                    tfSpecies.getText().trim(),
                    tfBreed.getText().trim(),
                    tfGender.getText().trim().charAt(0),
                    sd
                );
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Pet created!" : "Creation failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
                if (ok) {
                    dispose();
                    new RecManagePetsFrame(recCode, isHead).setVisible(true);
                }
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
            new RecManagePetsFrame(recCode, isHead).setVisible(true);
        });

        setContentPane(form);
    }

    private int thirtyFive() { return 35; }
}