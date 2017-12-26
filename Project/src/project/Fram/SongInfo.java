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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Songs;


/**
 *
 * @author Lan
 */
public class SongInfo{

    private String API_URL = "https://1-dot-api-person.appspot.com/_api/v1/songs/get/";
    private String strId;
    private Songs songs;
    private Gson gson = new Gson();
    public JFrame frame;
    private JPanel panel;
    private JLabel lblId, lblName, lblSinger, lblSource, lblListenCount, lblImage;
    private JTextField txtId, txtName, txtSinger, txtSource, txtListenCount, txtImage;
    private JButton btnEdit, btnDelete;
    
    ListMember listMember;

    public SongInfo(String id) {
        super();
        strId = id;
        initComponent();

    }

    private void initComponent() {
        songs = new Songs();
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
        lblName = new JLabel("Song name");
        lblSinger = new JLabel("Singer");
        lblSource = new JLabel("Url source");
        lblListenCount = new JLabel("Listen Count");
        lblImage = new JLabel("Image");
     

        txtId = new JTextField();
        txtName = new JTextField();
        txtSinger = new JTextField();
        txtSource = new JTextField();
        txtListenCount = new JTextField();
        txtImage = new JTextField();
 

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        lblId.setBounds(50, 50, 80, 30);
        lblName.setBounds(50, 100, 80, 30);
        lblSinger.setBounds(50, 150, 80, 30);
        lblSource.setBounds(50, 200, 80, 30);
        lblListenCount.setBounds(50, 300, 80, 30);
        lblImage.setBounds(50, 250, 80, 30);
      

        txtId.setBounds(150, 50, 300, 30);
        txtName.setBounds(150, 100, 300, 30);
        txtSinger.setBounds(150, 150, 300, 30);
        txtSource.setBounds(150, 200, 300, 30);
        txtListenCount.setBounds(150, 300, 300, 30);
        txtImage.setBounds(150, 250, 300, 30);
      

        btnEdit.setBounds(200, 350, 80, 30);
        btnDelete.setBounds(300, 350, 80, 30);

        panel.add(lblId);
        panel.add(lblName);
        panel.add(lblSinger);
        panel.add(lblSource);
        panel.add(lblListenCount);
        panel.add(lblImage);
   

        panel.add(txtId);
        panel.add(txtName);
        panel.add(txtSinger);
        panel.add(txtSource);
        panel.add(txtListenCount);
        panel.add(txtImage);
      

        txtId.setEnabled(false);
        txtName.setEnabled(false);
        txtSinger.setEnabled(false);
        txtSource.setEnabled(false);
        txtListenCount.setEnabled(false);
        txtImage.setEnabled(false);
      

        btnEdit.addActionListener(new EditHandle());
        btnDelete.addActionListener(new DeleteHandle());

        panel.add(btnEdit);
        panel.add(btnDelete);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        loadSong(strId);
    }

    private void loadSong(String id) {
        String url = API_URL + id;
        try {
            String strResponse = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute().body();
            songs = gson.fromJson(strResponse, Songs.class);
            txtId.setText(songs.getId());
            txtName.setText(songs.getName());
            txtSinger.setText(songs.getSinger());
            txtSource.setText(songs.getUrlSource());
            txtImage.setText(songs.getImage());
            txtListenCount.setText(String.valueOf(songs.getListenCount()));
    
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public class EditHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (btnEdit.getActionCommand().equals("Edit")) {
                btnEdit.setText("Save");
                txtName.setEnabled(true);
                txtSinger.setEnabled(true);
                txtSource.setEnabled(true);
                txtListenCount.setEnabled(true);
                txtImage.setEnabled(true);

            }else if (btnEdit.getActionCommand().equals("Save")) {
                btnEdit.setText("Edit");
                txtName.setEnabled(false);
                txtSinger.setEnabled(false);
                txtSource.setEnabled(false);
                txtListenCount.setEnabled(false);
                txtImage.setEnabled(false);

                Songs song = new Songs();
                song.setId(txtId.getText());
                song.setName(txtName.getText());
                song.setListenCount(Integer.valueOf(txtListenCount.getText()));
                song.setUrlSource(txtSource.getText());
                song.setSinger(txtSinger.getText());
                song.setImage(txtImage.getText());
               
                 HashMap<String,String> errors = song.validate();
                
                Gson gson = new Gson();
                String songJson = gson.toJson(song);

                String url = "https://1-dot-api-person.appspot.com/_api/v1/songs/admin/" + txtId.getText();

                try {
                    String response = Jsoup.connect(url).ignoreContentType(true)
                            .header("AdminAuth", LoginFram.tokenKey)
                            .method(Connection.Method.PUT)
                            .requestBody(songJson)
                            .execute()
                            .body();
                    JOptionPane.showMessageDialog(null, "Edit success!");
              
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Eross!");
                }
            }
        }
    }
    
    
    public class DeleteHandle implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           if (JOptionPane.showConfirmDialog(null, "Are your sure?") == JOptionPane.OK_OPTION) {

                String url = "https://1-dot-api-person.appspot.com/_api/v1/songs/admin/" + txtId.getText();

                try {
                    String response = Jsoup.connect(url).ignoreContentType(true)
                            .header("AdminAuth", LoginFram.tokenKey)
                            .method(Connection.Method.DELETE)
                            .execute()
                            .body();

                    JOptionPane.showMessageDialog(null, "Delete success!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Eross!");
                }

            }
        }
    }
    
    

}
