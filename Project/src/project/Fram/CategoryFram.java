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
import javax.swing.JTextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Category;

/**
 *
 * @author Lan
 */
public class CategoryFram {
    
    ListCategory listCategory;
    
    public JFrame frame;
    private JPanel panel;
    private JLabel lblId, lblCategory, lblDescribe;
    private JTextField txtId,txtCategory, txtDescribe;

    private JButton btnSubmit, btnReset;

    public CategoryFram() {
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
        lblId = new JLabel("Id");
        lblCategory = new JLabel("Category");
        lblDescribe = new JLabel("Describe");
     

      

        txtId = new JTextField();
        txtCategory = new JTextField();
        txtDescribe = new JTextField();
    

    

        btnSubmit = new JButton("Register");
        btnReset = new JButton("Exit");

        lblId.setBounds(50, 50, 80, 30);
        lblCategory.setBounds(50, 100, 80, 30);
        lblDescribe.setBounds(50, 150, 80, 30);
 

      

        txtId.setBounds(150, 50, 300, 30);
        txtCategory.setBounds(150, 100, 300, 30);
        txtDescribe.setBounds(150, 150, 300, 30);
    



        
        
       
        btnSubmit.setBounds(200, 350, 80, 30);
        btnReset.setBounds(300, 350, 80, 30);

        btnSubmit.addActionListener(new SubmitHandle());
        btnReset.addActionListener(new ResetHandle());

        panel.add(lblId);
        panel.add(lblCategory);
        panel.add(lblDescribe);
 

        panel.add(txtId);
        panel.add(txtCategory);
        panel.add(txtDescribe);
  

        panel.add(btnSubmit);
        panel.add(btnReset);
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public class SubmitHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Category category = new Category();

            category.setIDCate(txtId.getText());
            category.setCategory(txtCategory.getText());
            category.setDescribe(txtDescribe.getText());
      

         
     
            Gson gson = new Gson();
            HashMap<String,String> errors = category.validate();
            
            if(errors.size()>0 ){
             JOptionPane.showMessageDialog(null, "Enter full information");
                return;
            }
            String memberJson = gson.toJson(category);

            try {
                String url = "https://1-dot-api-person.appspot.com/_api/v1/categories";
                String response = Jsoup.connect(url).ignoreContentType(true)
                        .header("AdminAuth", LoginFram.tokenKey)
                        .method(Connection.Method.POST)
                        .requestBody(memberJson)
                        .execute()
                        .body();
                JOptionPane.showMessageDialog(null, "Success!");
                reset();
          
            } catch (IOException ex) {
                
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

public void reset(){
    txtId.setText("");
    txtCategory.setText("");
 
}

}
