/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Fram;

import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Admin;
import project.Model.Utility;

/**
 *
 * @author Lan
 */
public class AdminFram {

    public JFrame frame;
    private JPanel panel;
    private JLabel lblUserName, lblPassword, lblName, lblEmail, lblPhone;
    private JTextField txtUserName, txtName, txtEmail, txtPhone;
    private JPasswordField txtPassword;
    private JButton btnSubmit, btnReset;

    public AdminFram() {
        super();
        initComponent();
    }

    private void initComponent() {
        frame = new JFrame();
        frame.setTitle("");
        frame.setSize(600, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setSize(600, 500);
        panel.setLayout(null);
        
         panel = new ImagePanel(new ImageIcon("src\\project\\Img\\e7.png").getImage());

        lblUserName = new JLabel("UserName");
        lblPassword = new JLabel("Password");
        lblName = new JLabel("Name");
        lblEmail = new JLabel("Email");
        lblPhone = new JLabel("Phone");

        txtUserName = new JTextField();
        txtPassword = new JPasswordField();
        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();

        btnSubmit = new JButton("Register");
        btnReset = new JButton("Exit");

        lblUserName.setBounds(50, 50, 80, 30);
        lblPassword.setBounds(50, 100, 80, 30);
        lblName.setBounds(50, 150, 80, 30);
        lblEmail.setBounds(50, 200, 80, 30);
        lblPhone.setBounds(50, 250, 80, 30);

        txtUserName.setBounds(150, 50, 300, 30);
        txtPassword.setBounds(150, 100, 300, 30);
        txtName.setBounds(150, 150, 300, 30);
        txtEmail.setBounds(150, 200, 300, 30);
        txtPhone.setBounds(150, 250, 300, 30);

        btnSubmit.setBounds(200, 350, 80, 30);
        btnReset.setBounds(300, 350, 80, 30);

        btnSubmit.addActionListener(new SubmitHandle());
        btnReset.addActionListener(new ResetHandle());

        panel.add(lblUserName);
        panel.add(lblPassword);
        panel.add(lblName);
        panel.add(lblEmail);
        panel.add(lblPhone);

        panel.add(txtUserName);
        panel.add(txtPassword);
        panel.add(txtName);
        panel.add(txtEmail);
        panel.add(txtPhone);

        panel.add(btnSubmit);
        panel.add(btnReset);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public class SubmitHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Admin admin = new Admin();

            admin.setUserName(txtUserName.getText());
            admin.setPassword(Utility.digestPassword(new String(txtPassword.getPassword())));
            admin.setName(txtName.getText());
            admin.setEmail(txtEmail.getText());
            admin.setPhone(txtPhone.getText());

            Gson gson = new Gson();
            HashMap<String, String> errors = admin.validate();

            if (errors.size() > 0) {
                JOptionPane.showMessageDialog(null, "Enter full information");
                return;
            }
            String memberJson = gson.toJson(admin);

            try {
                String url = "https://1-dot-api-person.appspot.com/_api/v1/admins/register";
                String response = Jsoup.connect(url).ignoreContentType(true)
                        .header("AdminAuth", LoginFram.tokenKey)
                        .method(Connection.Method.POST)
                        .requestBody(memberJson)
                        .execute()
                        .body();
                JOptionPane.showMessageDialog(null, "Success!");
                reset();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "eross");
            }
        }

    }

    public class ResetHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);

        }

    }

    public void reset() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");

    }

}
