/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Fram;

import com.google.gson.Gson;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Member;
import project.Model.MultipartUtility;
import project.Model.Utility;

/**
 *
 * @author Lan
 */
public class MemberFram {

    File upFile;
    String url_Img = "";
    String uploadUrl = "";
    String getUploadUrl = "https://1-dot-api-person.appspot.com/_api/v1/image/get";

    public JFrame frame;
    private JPanel panel;
    private JLabel lblUserName, lblPassword, lblFullName, lblAddress, lblPhone, lblEmail, lblAvatar;
    private JTextField txtUserName, txtFullName, txtAddress, txtPhone, txtEmail, txtAvatar;
    private JPasswordField txtPassword;
    private JButton btnSubmit, btnReset, btnImg;

    public MemberFram() {
        super();
        initComponent();
        getUploadUrl();
    }

    private void getUploadUrl() {
        try {
            uploadUrl = Jsoup.connect(getUploadUrl).method(Connection.Method.GET).ignoreContentType(true).execute().body();
            System.out.println(uploadUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        lblFullName = new JLabel("FullName");
        lblAddress = new JLabel("Address");
        lblAvatar = new JLabel("Avatar");

        lblEmail = new JLabel("Email");

        txtUserName = new JTextField();
        txtPassword = new JPasswordField();
        txtFullName = new JTextField();
        txtAddress = new JTextField();
        txtAvatar = new JTextField();
        txtEmail = new JTextField();

        btnSubmit = new JButton("Register");
        btnReset = new JButton("Exit");
        btnImg = new JButton("Image");

        lblUserName.setBounds(50, 50, 80, 30);
        lblPassword.setBounds(50, 100, 80, 30);
        lblFullName.setBounds(50, 150, 80, 30);
        lblAddress.setBounds(50, 250, 80, 30);
        lblAvatar.setBounds(50, 300, 80, 30);
        lblEmail.setBounds(50, 200, 80, 30);

        txtUserName.setBounds(150, 50, 300, 30);
        txtPassword.setBounds(150, 100, 300, 30);
        txtFullName.setBounds(150, 150, 300, 30);
        txtAddress.setBounds(150, 250, 300, 30);
        txtAvatar.setBounds(150, 300, 200, 30);

        txtEmail.setBounds(150, 200, 300, 30);
        btnImg.setBounds(370, 300, 80, 30);

        btnSubmit.setBounds(200, 350, 80, 30);
        btnReset.setBounds(300, 350, 80, 30);

        btnSubmit.addActionListener(new SubmitHandle());
        btnReset.addActionListener(new ResetHandle());
      

        panel.add(lblUserName);
        panel.add(lblPassword);
        panel.add(lblFullName);
        panel.add(lblAddress);
       

        panel.add(lblEmail);

        panel.add(txtUserName);
        panel.add(txtPassword);
        panel.add(txtFullName);
        panel.add(txtAddress);

        panel.add(txtEmail);
     

        panel.add(btnSubmit);
        panel.add(btnReset);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }


    public class SubmitHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Member member = new Member();

            member.setUserName(txtUserName.getText());
            member.setPassword(Utility.digestPassword(new String(txtPassword.getPassword())));
            member.setFullName(txtFullName.getText());
            member.setEmail(txtEmail.getText());
            member.setAddress(txtAddress.getText());
            member.setAvatar(txtAvatar.getText());
         

            Gson gson = new Gson();
            HashMap<String, String> errors = member.validate();

            if (errors.size() > 0) {
                JOptionPane.showMessageDialog(null, "Enter full information");
                return;
            }
            String memberJson = gson.toJson(member);

            try {
                String url = "https://1-dot-api-person.appspot.com/_api/v1/members/register";
                String response = Jsoup.connect(url).ignoreContentType(true)
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
        txtFullName.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
    }

}
