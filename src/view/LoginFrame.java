package view;

import controller.AuthController;
import view.OwnerSignupFrame;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AuthController authCtrl = new AuthController();

    public LoginFrame() {
        super("Pampered Pet Clinic — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 20, 20, 20);

        // --- Username ---
        JLabel lblUser = new JLabel("Username or Email:");
        lblUser.setFont(lblUser.getFont().deriveFont(18f));
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
        panel.add(lblUser, c);

        JTextField txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(450, fortyHeight));
        txtUser.setFont(txtUser.getFont().deriveFont(16f));
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        panel.add(txtUser, c);

        // --- Password ---
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(lblPass.getFont().deriveFont(18f));
        c.gridy = 1; c.gridx = 0; c.anchor = GridBagConstraints.EAST;
        panel.add(lblPass, c);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(450, fortyHeight));
        txtPass.setFont(txtPass.getFont().deriveFont(16f));
        c.gridx = 1; c.anchor = GridBagConstraints.WEST;
        panel.add(txtPass, c);

        // --- Log In button ---
        JButton btnLogin = new JButton("Log In");
        btnLogin.setPreferredSize(new Dimension(220, fiftyHeight));
        btnLogin.setFont(btnLogin.getFont().deriveFont(18f));
        btnLogin.setBackground(new Color(200, 200, 200));
        c.gridy = 2; c.gridx = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, c);



        // --- Actions ---
        btnLogin.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword());
            AuthController.LoginResult res = authCtrl.login(u, p);
            if (res != null) {
                dispose();
                switch (res.getRole()) {
                    case "OWNER":
                        new OwnerDashboardFrame(res.getUserCode()).setVisible(true);
                        break;
                    case "VETERINARIAN":
                        new VetDashboardFrame(res.getUserCode()).setVisible(true);
                        break;
                    case "RECEPTIONIST":
                        new RecDashboardFrame(res.getUserCode(), false).setVisible(true);
                        break;
                    case "HEAD_RECEPTIONIST":
                        new RecDashboardFrame(res.getUserCode(), true).setVisible(true);
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

       

        setContentPane(panel);
    }

    // sizing helpers
    private static final int fortyHeight = 40;
    private static final int fiftyHeight = 50;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}