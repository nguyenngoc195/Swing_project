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
import project.Entity.Member;
import project.Model.Utility;

/**
 *
 * @author Lan
 */
public class MemberInfor {

    private String API_URL = "https://1-dot-api-person.appspot.com/_api/v1/members/admin/";
    private Gson gson = new Gson();
    private ListMember listMember;
    private Member member;
    private String strUserName;

    public JFrame frame;
    private JPanel panel;
    private JLabel lblUserName, lblPassword, lblFullName, lblAddress, lblPhone, lblEmail;
    private JTextField txtUserName, txtFullName, txtAddress, txtPhone, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnEdit, btnDelete;

    public MemberInfor(String userName) {
        super();
        strUserName = userName;
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
        lblFullName = new JLabel("FullName");
        lblAddress = new JLabel("Address");
        lblEmail = new JLabel("Email");
    

        txtUserName = new JTextField();
        txtPassword = new JPasswordField();
        txtFullName = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
      

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        lblUserName.setBounds(50, 50, 80, 30);
        lblPassword.setBounds(50, 100, 80, 30);
        lblFullName.setBounds(50, 150, 80, 30);
        lblAddress.setBounds(50, 250, 80, 30);
    

        lblEmail.setBounds(50, 200, 80, 30);

        txtUserName.setBounds(150, 50, 300, 30);
        txtPassword.setBounds(150, 100, 300, 30);
        txtFullName.setBounds(150, 150, 300, 30);
        txtAddress.setBounds(150, 250, 300, 30);
        

        txtEmail.setBounds(150, 200, 300, 30);

        btnEdit.setBounds(200, 350, 80, 30);
        btnDelete.setBounds(300, 350, 80, 30);

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

        txtUserName.setEnabled(false);
        txtPassword.setEnabled(false);
        txtFullName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtAddress.setEnabled(false);
   
        
        btnEdit.addActionListener(new EditHandle());
        btnDelete.addActionListener(new DeleteHandle());

        panel.add(btnEdit);
        panel.add(btnDelete);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        loadMember(strUserName);
    }

    private void loadMember(String userName) {
        String url = API_URL  + userName;
        try {
            String strResponse = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .header("AdminAuth", LoginFram.tokenKey)
                    .method(Connection.Method.GET)
                    .execute()
                    .body();
            
            member = gson.fromJson(strResponse, Member.class);
            txtUserName.setText(member.getUserName());
            txtPassword.setText(Utility.digestPassword(new String(member.getPassword())));
            txtFullName.setText(member.getFullName());
            txtEmail.setText(member.getEmail());
            txtAddress.setText(String.valueOf(member.getAddress()));
          

        } catch (IOException ex) {
           ex.printStackTrace();
        }

    }
    public  void reset(){
        txtUserName.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
    }

    public class EditHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnEdit.getActionCommand().equals("Edit")) {
                btnEdit.setText("Save");
                txtPassword.setEnabled(true);
                txtFullName.setEnabled(true);
                txtEmail.setEnabled(true);
                txtAddress.setEnabled(true);

            } else if (btnEdit.getActionCommand().equals("Save")) {
                btnEdit.setText("Edit");
                Member nmember = new Member();
                nmember.setUserName(txtUserName.getText());
                nmember.setPassword(Utility.digestPassword(new String(txtPassword.getPassword())));
                nmember.setFullName(txtFullName.getText());
                nmember.setEmail(txtEmail.getText());
                nmember.setAddress(txtAddress.getText());
                nmember.setStatus(Integer.valueOf(member.getStatus()));
                HashMap<String, String> errors = member.validate();

                Gson gson = new Gson();
                String songJson = gson.toJson(member);

                String url = "https://1-dot-api-person.appspot.com/_api/v1/members/" + txtUserName.getText();

                try {
                    String response = Jsoup.connect(url).ignoreContentType(true)
                            .method(Connection.Method.PUT)
                            .requestBody(songJson)
                            .execute()
                            .body();
                    JOptionPane.showMessageDialog(null, "Edit success!");
                    reset();
                 
                    frame.setVisible(false);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Eross!");
                }
            }
        }
    }

    public class DeleteHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(null, "Are your sure?") == JOptionPane.OK_OPTION) {

                String url = "https://1-dot-api-person.appspot.com/_api/v1/members/admin/" + txtUserName.getText();

                try {
                    String response = Jsoup.connect(url).ignoreContentType(true)
                            .header("AdminAuth", LoginFram.tokenKey)
                            .method(Connection.Method.DELETE)
                            .execute()
                            .body();

                    JOptionPane.showMessageDialog(null, "Delete success!");
                    reset();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Eross!");
                }

            }
        }
    }

}
