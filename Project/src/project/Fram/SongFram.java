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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Category;
import project.Entity.CategoryListResponse;
import project.Entity.SongCate;
import project.Entity.Songs;
import project.Model.MultipartUtility;

/**
 *
 * @author Lan
 */
public class SongFram {

    File upFile;

    String uploadUrl = "";
    String getUploadUrl = "https://1-dot-api-person.appspot.com/image/geturl";

    private String API_URL = "https://1-dot-api-person.appspot.com/_api/v1/songs/admin/";
    private String currentId;

    private String currentSong;

    public JFrame frame;
    private JPanel panel;
    private JLabel lblName, lblSinger, lblSource, lblImage;
    private JTextField txtName, txtSinger, txtSource, txtImage;
    private JButton btnSubmit, btnList, btnImg;

    private List<JCheckBox> listCheckBox = new ArrayList<>();

    private int y = 0;

    private Gson gson = new Gson();

    public SongFram() {
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

        lblName = new JLabel("Song name");
        lblSinger = new JLabel("Singer");
        lblSource = new JLabel("Url source");
        lblImage = new JLabel("Avatar");

        txtName = new JTextField();
        txtSinger = new JTextField();
        txtSource = new JTextField();
        txtImage = new JTextField();
        btnSubmit = new JButton("Submit");
        btnList = new JButton("Exit");
        btnImg = new JButton("Img");
        try {
            String content = Jsoup.connect("https://1-dot-api-person.appspot.com/_api/v1/categories")
                    .ignoreContentType(true)
                    .header("AdminAuth", LoginFram.tokenKey)
                    .method(Connection.Method.GET).execute()
                    .body();
            CategoryListResponse response = gson.fromJson(content, CategoryListResponse.class);
            List<Category> listCate = response.getData();
   
            
            
            for (Category cate : listCate) {

                y += 35;
                JCheckBox checkBox = new JCheckBox();
                checkBox.setName(cate.getIDCate());
                checkBox.setText(cate.getCategory());
                checkBox.setOpaque(false);
                checkBox.setBounds(470, y, 100, 40);
                panel.add(checkBox);
                listCheckBox.add(checkBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    

        lblName.setBounds(50, 50, 80, 30);
        lblSinger.setBounds(50, 100, 80, 30);
        lblSource.setBounds(50, 150, 80, 30);
        lblImage.setBounds(50, 200, 80, 30);

        txtName.setBounds(150, 50, 300, 30);
        txtSinger.setBounds(150, 100, 300, 30);
        txtSource.setBounds(150, 150, 300, 30);
        txtImage.setBounds(150, 200, 200, 30);

        btnSubmit.setBounds(200, 250, 80, 30);
        btnList.setBounds(300, 250, 80, 30);
        btnImg.setBounds(370, 200, 80, 30);


        btnSubmit.addActionListener(new SubmitHandle());
        btnList.addActionListener(new ListHandle());
          btnImg.addActionListener(new ImgHandle());

        panel.add(lblName);
        panel.add(lblSinger);
        panel.add(lblSource);
        panel.add(lblImage);

        panel.add(txtName);
        panel.add(txtSinger);
        panel.add(txtSource);
        panel.add(txtImage);

        panel.add(btnSubmit);
        panel.add(btnList);
        panel.add(btnImg);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public class ListHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            frame.setVisible(false);
        }

    }
    public  class ImgHandle implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
       JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(fileChooser);
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();
            JOptionPane.showMessageDialog(null, "Chọn ảnh thành công!");
            upFile = new File(fileName);

            if (upFile != null) {
                String charset = "UTF-8";
                try {
                    MultipartUtility multiPart = new MultipartUtility(uploadUrl, charset);
                    multiPart.addHeaderField("User-Agent", "CodeJava");
                    multiPart.addHeaderField("Test-Header", "Header-Value");
                    multiPart.addFilePart("myFile", upFile);
                    List<String> response = multiPart.finish();
                    System.out.println("SERVER----");
                    String imgUrl = "";
                    for (String line : response) {
                        System.out.println(line);
                        if (line.length() > 0) {
                            imgUrl = line;
                        }
                        System.out.println(imgUrl);
                    }
                    //   showImg(imgUrl + "=s200");
                    txtImage.setText(imgUrl);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "eross!");
                    ex.printStackTrace();
                }
            }
        } 
    
    
    }

    public class SubmitHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            currentId = String.valueOf(System.currentTimeMillis());
            Songs song = new Songs();
            song.setId(currentId);
            song.setName(txtName.getText());
            song.setUrlSource(txtSource.getText());
            song.setSinger(txtSinger.getText());
            song.setImage(txtImage.getText());
            
            Gson gson = new Gson();
            HashMap<String, String> errors = song.validate();

            if (errors.size() > 0) {
                JOptionPane.showMessageDialog(null, "Enter full information");
                return;
            }
            String songJson = gson.toJson(song);
            try {
                System.out.println(LoginFram.tokenKey);

                String response = Jsoup.connect(API_URL).ignoreContentType(true)
                        .header("AdminAuth", LoginFram.tokenKey)
                        .method(Connection.Method.POST)
                        .requestBody(songJson)
                        .execute()
                        .body();
                JOptionPane.showMessageDialog(null, "Success!");
                reset();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error");
            }

            for (JCheckBox jCheckBox : listCheckBox) {
                if (jCheckBox.isSelected()) {
                    CreateSongCate(jCheckBox.getName(), currentId);
                   
                }

            }

        }
    }

    private void CreateSongCate(String idCate, String idSong) {
        SongCate songCate = new SongCate();
       
        songCate.setIdCate(idCate);
        songCate.setIdSong(idSong);
        Gson gson1 = new Gson();
        String songCateJson = gson1.toJson(songCate);

        try {
            String url = "https://1-dot-api-person.appspot.com/_api/v1/songcates/admin";
            String response = Jsoup.connect(url).ignoreContentType(true)
                    .header("AdminAuth", LoginFram.tokenKey)
                    .method(Connection.Method.POST)
                    .requestBody(songCateJson)
                    .execute()
                    .body();

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

    public void reset() {
        txtName.setText("");
        txtSinger.setText("");
        txtSource.setText("");

    }

}
