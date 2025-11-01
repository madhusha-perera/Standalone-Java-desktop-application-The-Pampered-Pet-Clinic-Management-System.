// VetSearchPetFrame.java
package view;

import controller.VetController;
import model.Pet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class VetSearchPetFrame extends JFrame {
    private final String vetCode;
    private final VetController ctrl = new VetController();

    // panel to display pet info + "View Records" button
    private final JPanel detailsPanel = new JPanel();

    public VetSearchPetFrame(String vetCode) {
        super("Search Pet — " + vetCode);
        this.vetCode = vetCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Root panel with light gray background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);

        // Row 0: label + text field + buttons
        JLabel lblCode = new JLabel("Pet Code:");
        lblCode.setFont(lblCode.getFont().deriveFont(18f));
        lblCode.setForeground(Color.black);

        JTextField tfCode = new JTextField();
        tfCode.setPreferredSize(new Dimension(450, 40));
        tfCode.setFont(tfCode.getFont().deriveFont(16f));
        tfCode.setForeground(Color.black);

        JButton btnSearch = new JButton("Search");
        JButton btnBack   = new JButton("Back");
        Dimension btnSize = new Dimension(200, 50);
        for (JButton b : new JButton[]{btnSearch, btnBack}) {
            b.setPreferredSize(btnSize);
            b.setFont(b.getFont().deriveFont(16f));
            b.setBackground(new Color(200, 200, 200));
            b.setForeground(Color.black);
        }

        // label
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0; c.gridy = 0;
        panel.add(lblCode, c);

        // text field
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1; 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel.add(tfCode, c);

        // search/back buttons
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 1; c.gridx = 0;
        panel.add(btnSearch, c);
        c.gridx = 1;
        panel.add(btnBack, c);

        // Row 2: details panel (initially empty)
        detailsPanel.setBackground(panel.getBackground());
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        c.gridx = 0; c.gridy = 2;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        panel.add(detailsPanel, c);

        // Actions
        btnSearch.addActionListener(e -> {
            detailsPanel.removeAll();
            String code = tfCode.getText().trim();
            Pet p = ctrl.searchPet(code);
            if (p != null) {
                // show pet details
                detailsPanel.add(new JLabel("Code:    " + p.getPetCode()));
                detailsPanel.add(new JLabel("Name:    " + p.getName()));
                detailsPanel.add(new JLabel("Species: " + p.getSpecies()));
                detailsPanel.add(new JLabel("Breed:   " + p.getBreed()));
                detailsPanel.add(new JLabel("Gender:  " + p.getGender()));
                detailsPanel.add(new JLabel("DOB:     " +
                    new SimpleDateFormat("yyyy-MM-dd").format(p.getDob())));
                detailsPanel.add(Box.createRigidArea(new Dimension(0,20)));

                // "View Records" button
                JButton btnView = new JButton("View Medical Records");
                btnView.setFont(btnView.getFont().deriveFont(16f));
                btnView.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnView.addActionListener(ae -> {
                    new VetViewRecordsFrame(p.getPetCode()).setVisible(true);
                });
                detailsPanel.add(btnView);

            } else {
                detailsPanel.add(new JLabel("Pet not found"));
            }
            detailsPanel.revalidate();
            detailsPanel.repaint();
        });

        btnBack.addActionListener(e -> {
            dispose();
            new VetDashboardFrame(vetCode).setVisible(true);
        });

        setContentPane(panel);
    }
}
