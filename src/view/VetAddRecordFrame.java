package view;

import controller.VetController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VetAddRecordFrame extends JFrame {
    private final String vetCode;
    private final VetController ctrl = new VetController();

    public VetAddRecordFrame(String vetCode) {
        super("Add Medical Record — " + vetCode);
        this.vetCode = vetCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 245, 245));  // light gray
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);
        c.fill = GridBagConstraints.NONE;

        // Labels
        JLabel lPet   = new JLabel("Pet Code:");
        JLabel lDate  = new JLabel("Visit Date (yyyy-MM-dd):");
        JLabel lDiag  = new JLabel("Diagnosis:");
        JLabel lTreat = new JLabel("Treatment:");
        JLabel lMed   = new JLabel("Medication:");

        for (JLabel lbl : new JLabel[]{lPet, lDate, lDiag, lTreat, lMed}) {
            lbl.setFont(lbl.getFont().deriveFont(18f));
            lbl.setForeground(Color.black);
        }

        // Fields
        Dimension fieldSize = new Dimension(450, 40);
        JTextField tfPet   = new JTextField(); tfPet.setPreferredSize(fieldSize);
        JTextField tfDate  = new JTextField(); tfDate.setPreferredSize(fieldSize);
        JTextField tfDiag  = new JTextField(); tfDiag.setPreferredSize(fieldSize);
        JTextField tfTreat = new JTextField(); tfTreat.setPreferredSize(fieldSize);
        JTextField tfMed   = new JTextField(); tfMed.setPreferredSize(fieldSize);

        for (JTextField tf : new JTextField[]{tfPet, tfDate, tfDiag, tfTreat, tfMed}) {
            tf.setFont(tf.getFont().deriveFont(16f));
            tf.setForeground(Color.black);
        }

        // Buttons
        JButton btnAdd  = new JButton("Add Record");
        JButton btnBack = new JButton("Back");
        Dimension btnSize = new Dimension(200, 50);

        for (JButton b : new JButton[]{btnAdd, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(16f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // Layout
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0; form.add(lPet,   c);
        c.gridy = 1;             form.add(lDate,  c);
        c.gridy = 2;             form.add(lDiag,  c);
        c.gridy = 3;             form.add(lTreat, c);
        c.gridy = 4;             form.add(lMed,   c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; c.gridy = 0; form.add(tfPet,   c);
        c.gridy = 1;            form.add(tfDate,  c);
        c.gridy = 2;            form.add(tfDiag,  c);
        c.gridy = 3;            form.add(tfTreat, c);
        c.gridy = 4;            form.add(tfMed,   c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 5; c.gridx = 0; form.add(btnAdd,  c);
                     c.gridx = 1; form.add(btnBack, c);

        // Actions
        btnAdd.addActionListener(e -> {
            try {
                Date parsed = new SimpleDateFormat("yyyy-MM-dd")
                                   .parse(tfDate.getText().trim());
                boolean ok = ctrl.addMedicalRecord(
                    vetCode,
                    tfPet.getText().trim(),
                    parsed,
                    tfDiag.getText().trim(),
                    tfTreat.getText().trim(),
                    tfMed.getText().trim()
                );
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Record added successfully!" : "Failed to add record.",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
                if (ok) {
                    dispose();
                    new VetDashboardFrame(vetCode).setVisible(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid date format. Please use yyyy-MM-dd",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
            new VetDashboardFrame(vetCode).setVisible(true);
        });

        setContentPane(form);
    }
}
