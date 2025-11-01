package view;

import controller.OwnerController;
import model.Pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class OwnerViewPetsFrame extends JFrame {
    private final String ownerCode;
    private final OwnerController ctrl = new OwnerController();

    public OwnerViewPetsFrame(String ownerCode) {
        super("View My Pets — " + ownerCode);
        this.ownerCode = ownerCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Root panel
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel lbl = new JLabel("My Pets", SwingConstants.CENTER);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 24f));
        lbl.setForeground(Color.black);
        panel.add(lbl, BorderLayout.NORTH);

        // Pet list
        DefaultListModel<Pet> listModel = new DefaultListModel<>();
        JList<Pet> list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFont(list.getFont().deriveFont(16f));
        list.setForeground(Color.black);
        list.setCellRenderer((l, pet, idx, sel, foc) -> {
            JLabel cell = new JLabel(pet.getPetCode() + " – " + pet.getName());
            cell.setOpaque(true);
            cell.setBackground(sel ? new Color(200, 215, 235) : Color.white);
            cell.setFont(cell.getFont().deriveFont(16f));
            cell.setForeground(Color.black);
            return cell;
        });
        JScrollPane scroll = new JScrollPane(list);
        panel.add(scroll, BorderLayout.CENTER);

        // Buttons: Back + View Records
        JButton btnBack    = new JButton("Back");
        JButton btnHistory = new JButton("View Medical History");
        Dimension btnSize  = new Dimension(240, 50);
        for (JButton b : new JButton[]{btnHistory, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(18f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        south.setBackground(panel.getBackground());
        south.add(btnHistory);
        south.add(btnBack);
        panel.add(south, BorderLayout.SOUTH);

        // Load pets
        List<Pet> pets = ctrl.listPets(ownerCode);
        pets.forEach(listModel::addElement);

        // Double‑click to show details (optional)
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Pet p = list.getSelectedValue();
                    if (p != null) {
                        JOptionPane.showMessageDialog(
                          OwnerViewPetsFrame.this,
                          String.format(
                            "Code: %s%nName: %s%nSpecies: %s%nBreed: %s%n" +
                            "Gender: %s%nDOB: %s",
                            p.getPetCode(),
                            p.getName(),
                            p.getSpecies(),
                            p.getBreed(),
                            p.getGender(),
                            new SimpleDateFormat("yyyy-MM-dd")
                                .format(p.getDob())
                          ),
                          "Pet Details",
                          JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        // View Medical History action
        btnHistory.addActionListener(e -> {
            Pet selected = list.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a pet first.",
                    "No Pet Selected",
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                new OwnerMedHistoryFrame(selected.getPetCode()).setVisible(true);
            }
        });

        // Back action
        btnBack.addActionListener(e -> {
            dispose();
            new OwnerDashboardFrame(ownerCode).setVisible(true);
        });

        setContentPane(panel);
    }
}