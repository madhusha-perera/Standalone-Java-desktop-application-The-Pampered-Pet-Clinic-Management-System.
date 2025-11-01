package view;

import controller.VetController;
import model.Appointment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VetViewApptsFrame extends JFrame {
    private final String vetCode;
    private final VetController ctrl = new VetController();

    public VetViewApptsFrame(String vetCode) {
        super("View Appointments — " + vetCode);
        this.vetCode = vetCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(new Color(245, 245, 245)); // light gray
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Scheduled Appointments", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.black);
        root.add(title, BorderLayout.NORTH);

        // Appointment list
        DefaultListModel<Appointment> model = new DefaultListModel<>();
        JList<Appointment> list = new JList<>(model);
        list.setFont(list.getFont().deriveFont(16f));
        list.setForeground(Color.black);
        list.setCellRenderer((l, a, idx, sel, foc) -> {
            String text = String.format(
                "%s | %s | %s → %s | %s",
                a.getAppointmentCode(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(a.getDatetime()),
                a.getOwnerCode(),
                a.getPetCode(),
                a.getStatus()
            );
            JLabel lbl = new JLabel(text);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? new Color(200, 215, 235) : Color.white);
            lbl.setForeground(Color.black);
            lbl.setFont(lbl.getFont().deriveFont(16f));
            return lbl;
        });
        root.add(new JScrollPane(list), BorderLayout.CENTER);

        // Load appointments
        List<Appointment> appts = ctrl.listAppointments(vetCode);
        appts.forEach(model::addElement);

        // Double-click for details
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Appointment a = list.getSelectedValue();
                    if (a != null) {
                        String details = String.format(
                            "Code: %s%nDateTime: %s%nOwner: %s%nPet: %s%nStatus: %s%nReason: %s",
                            a.getAppointmentCode(),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(a.getDatetime()),
                            a.getOwnerCode(),
                            a.getPetCode(),
                            a.getStatus(),
                            a.getReason()
                        );
                        JOptionPane.showMessageDialog(
                            VetViewApptsFrame.this,
                            details,
                            "Appointment Details",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(200, 50));
        btnBack.setFont(btnBack.getFont().deriveFont(16f));
        btnBack.setBackground(new Color(200, 200, 200));
        btnBack.setForeground(Color.black);
        btnBack.addActionListener(e -> {
            dispose();
            new VetDashboardFrame(vetCode).setVisible(true);
        });
        JPanel south = new JPanel();
        south.setBackground(new Color(245, 245, 245));
        south.add(btnBack);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }
}
