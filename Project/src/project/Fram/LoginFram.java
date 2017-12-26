/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Fram;

import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Font;
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
import project.Entity.Credential;
import project.Model.Utility;




/**
 *
 * @author Lan
 */
public class LoginFram {
    public static String tokenKey;
    public static String nameAdmin;

   
    private JFrame frame;
    private JPanel panel;
    private JLabel lblAccount;
    private JLabel lblPassWord;
    private JTextField txtAccount;
    private JPasswordField txtPassWord;
    private JButton btnLogin, btnExit;

    public LoginFram() {
        super();
        initComponent();

    }

    private void initComponent() {

        frame = new JFrame();
        frame.setSize(800, 620);
        frame.setLocationRelativeTo(null);
        panel = new JPanel();
        
        panel = new ImagePanel(new ImageIcon("src\\project\\Img\\login1.png").getImage());
       
     
       
        lblAccount = new JLabel("Account");
        lblPassWord = new JLabel("Password");
        lblAccount.setFont(new Font("Arial", 2, 16));
        lblPassWord.setFont(new Font("Arial", 2, 16));
        txtAccount = new JTextField("admin");
        txtPassWord = new JPasswordField("admin");
        btnLogin = new JButton("Login");
        btnExit = new JButton("Exit");

        panel.setBounds(0, 0, 800, 620);
        
        
        lblAccount.setBounds(250, 198, 100, 40);
        lblPassWord.setBounds(250, 248, 100, 40);
        txtAccount.setBounds(320, 200, 200, 30);
        txtPassWord.setBounds(320, 250, 200, 30);
        btnLogin.setBounds(300, 300, 80, 30);
        btnExit.setBounds(420, 300, 80, 30);
        
       // btnLogin.setBackground(Color.red);
       lblAccount.setForeground(Color.white);
       lblPassWord.setForeground(Color.white);

        btnLogin.addActionListener(new LoginHandle());
        btnExit.addActionListener(new ExitHandle());

        panel.add(lblAccount);
        panel.add(lblPassWord);
        panel.add(txtAccount);
        panel.add(txtPassWord);
        panel.add(btnLogin);
        panel.add(btnExit);
    
        frame.add(panel);
      
        panel.setLayout(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

    }

    public class LoginHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Admin admin = new Admin();
            admin.setUserName(txtAccount.getText());
            admin.setPassword(Utility.digestPassword(new String(txtPassWord.getPassword())));
            
             HashMap<String, String> errors = admin.login();

            if (errors.size() > 0) {
                JOptionPane.showMessageDialog(null, "Enter user name and password");
                return;
            }
            
         
                  Gson gson = new Gson();
            String adminJson = gson.toJson(admin);
            try {
                String url = "https://1-dot-api-person.appspot.com/_api/v1/admins/login";
                String response = Jsoup.connect(url).ignoreContentType(true)
                        .method(Connection.Method.POST)
                        .requestBody(adminJson)
                        .execute()
                        .body();

          
                
                Credential c = gson.fromJson(response, Credential.class);
                tokenKey = c.getTokenKey();
               nameAdmin = c.getUserId();
                       
                 Menu menu = new Menu();
                frame.setVisible(false);
              
            } catch (IOException ex) { 
                JOptionPane.showMessageDialog(null, "wrong username or password");
            }

            
     

          
        }
    }
    
    public class ExitHandle implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
         System.exit(0);
        }
    }

 
}
