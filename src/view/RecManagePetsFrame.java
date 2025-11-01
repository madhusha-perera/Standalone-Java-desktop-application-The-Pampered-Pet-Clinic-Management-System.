package view;

import controller.ReceptionistController;
import model.Pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecManagePetsFrame extends JFrame {
    private final String recCode;
    private final boolean isHead;
    private final ReceptionistController ctrl = new ReceptionistController();

    public RecManagePetsFrame(String recCode, boolean isHead) {
        super((isHead ? "Head Receptionist" : "Receptionist") + " — Manage Pets");
        this.recCode = recCode;
        this.isHead  = isHead;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(245, 245, 245));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- NORTH: Search panel ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(root.getBackground());
        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets(5, 5, 5, 5);

        JLabel lblPet    = new JLabel("Pet Code:");
        JLabel lblOwner  = new JLabel("Owner Code:");

        JTextField tfPet   = new JTextField();
        JTextField tfOwner = new JTextField();
        tfPet.setPreferredSize(new Dimension(200, thirtyFive()));
        tfOwner.setPreferredSize(new Dimension(200, thirtyFive()));

        JButton btnByPet   = new JButton("Find by Pet");
        JButton btnByOwner = new JButton("Find by Owner");
        btnByPet.setPreferredSize(new Dimension(120, thirtyFive()));
        btnByOwner.setPreferredSize(new Dimension(140, thirtyFive()));

        sc.gridx = 0; sc.gridy = 0; sc.anchor = GridBagConstraints.EAST;
        searchPanel.add(lblPet, sc);
        sc.gridx = 1; sc.anchor = GridBagConstraints.WEST;
        searchPanel.add(tfPet, sc);
        sc.gridx = 2;
        searchPanel.add(btnByPet, sc);

        sc.gridx = 0; sc.gridy = 1; sc.anchor = GridBagConstraints.EAST;
        searchPanel.add(lblOwner, sc);
        sc.gridx = 1; sc.anchor = GridBagConstraints.WEST;
        searchPanel.add(tfOwner, sc);
        sc.gridx = 2;
        searchPanel.add(btnByOwner, sc);

        root.add(searchPanel, BorderLayout.NORTH);

        // --- CENTER: Pet list ---
        DefaultListModel<Pet> model = new DefaultListModel<>();
        JList<Pet> list = new JList<>(model);
        list.setFont(list.getFont().deriveFont(14f));
        list.setFixedCellHeight(30);
        list.setCellRenderer((l, pet, idx, sel, foc) -> {
            JLabel lbl = new JLabel(pet.getPetCode() + " — " + pet.getName());
            lbl.setOpaque(true);
            lbl.setBackground(sel ? new Color(200, 215, 235) : Color.white);
            lbl.setFont(lbl.getFont().deriveFont(14f));
            return lbl;
        });
        root.add(new JScrollPane(list), BorderLayout.CENTER);

        // --- SOUTH: action buttons ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBackground(root.getBackground());
        btnPanel.setPreferredSize(new Dimension(0, 50));

        JButton btnNew    = makeButton("New");
        JButton btnUpdate = makeButton("Update");
        JButton btnDelete = makeButton("Delete");
        JButton btnBack   = makeButton("Back");

        btnPanel.add(btnNew);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);
        root.add(btnPanel, BorderLayout.SOUTH);

        // --- Load initial data ---
        List<Pet> pets = isHead
            ? ctrl.listAllPets()
            : ctrl.listPets(recCode);
        pets.forEach(model::addElement);

        // --- Handlers ---
        btnByPet.addActionListener(e -> {
            model.clear();
            Pet p = ctrl.getPetByCode(tfPet.getText().trim());
            if (p != null) {
                model.addElement(p);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Pet not found",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });

        btnByOwner.addActionListener(e -> {
            model.clear();
            List<Pet> ownerPets = ctrl.listPets(tfOwner.getText().trim());
            if (ownerPets.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No pets for that owner",
                    "Search",
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                ownerPets.forEach(model::addElement);
            }
        });

        btnNew.addActionListener(e -> {
            new RecManagePetsCreateFrame(recCode, isHead).setVisible(true);
            dispose();
        });

        btnUpdate.addActionListener(e -> {
            Pet sel = list.getSelectedValue();
            if (sel != null) {
                new RecManagePetsUpdateFrame(recCode, isHead, sel)
                    .setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Select a pet to update",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        btnDelete.addActionListener(e -> {
            Pet sel = list.getSelectedValue();
            if (sel != null) {
                boolean ok = ctrl.deletePet(sel.getPetCode());
                JOptionPane.showMessageDialog(
                    this,
                    ok ? "Pet deleted" : "Delete failed",
                    ok ? "Success" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
                );
                if (ok) model.removeElement(sel);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Select a pet to delete",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
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

    private int thirtyFive() {
        return 35;
    }
}
