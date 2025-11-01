package view;

import controller.VetController;
import model.MedicalRecord;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class VetViewRecordsFrame extends JFrame {
    private final String petCode;
    private final VetController ctrl = new VetController();

    public VetViewRecordsFrame(String petCode) {
        super("Medical Records — " + petCode);
        this.petCode = petCode;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(new Color(245, 245, 245));
        root.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Medical Records for " + petCode, SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        root.add(title, BorderLayout.NORTH);

        DefaultListModel<MedicalRecord> model = new DefaultListModel<>();
        JList<MedicalRecord> list = new JList<>(model);
        list.setFont(list.getFont().deriveFont(16f));
        list.setCellRenderer((l, rec, idx, sel, foc) -> {
            String text = String.format(
                "%s | %s | %s | %s | %s",
                rec.getRecordCode(),
                new SimpleDateFormat("yyyy-MM-dd").format(rec.getVisitDate()),
                rec.getDiagnosis(),
                rec.getTreatment(),
                rec.getMedication()
            );
            JLabel lbl = new JLabel(text);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? new Color(200,220,240) : Color.white);
            lbl.setFont(lbl.getFont().deriveFont(16f));
            return lbl;
        });

        // load records
        List<MedicalRecord> records = ctrl.listMedicalRecords(petCode);
        records.forEach(model::addElement);

        root.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton btnBack = new JButton("Back");
        btnBack.setFont(btnBack.getFont().deriveFont(16f));
        btnBack.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.setBackground(root.getBackground());
        south.add(btnBack);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }
}