/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Fram;

import project.Model.ImageLabel;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Lan
 */
public class Menu {

    ListAdmin listAdmin = new ListAdmin();
    ListMember listMember = new ListMember();
    ListSong listSong = new ListSong();
    ListCategory listCategory = new ListCategory();
    private JFrame frame;
    private JPanel panel, mainPanel;
    private JLabel lblAdmin, lblMember, lblSong, lblCate, lblLogOut, lblExit;
    private JButton btnAdmin, btnMember, btnSong, btnLogOut, btnCate, btnExit;

    public Menu() {
        super();
        initComponent();
    }

    private void initComponent() {

        frame = new JFrame();
        frame.setTitle("User name: " + LoginFram.nameAdmin);
        frame.setSize(1250, 650);
        frame.setLocationRelativeTo(null);
        mainPanel = new JPanel();
        panel = new JPanel();
        panel = new ImagePanel(new ImageIcon("src\\project\\Img\\e6.png").getImage());

        mainPanel.setBackground(Color.decode("#001a22"));

        lblAdmin = new JLabel("ADMIN");
        lblMember = new JLabel("ADMIN");
        lblSong = new JLabel("ADMIN");
        lblCate = new JLabel("ADMIN");
        lblLogOut = new JLabel("ADMIN");
        lblExit = new JLabel("ADMIN");

        btnMember = new JButton("MEMBER MANAGEMENT");
        btnSong = new JButton("SONG MANAGEMENT");
        btnExit = new JButton("Exit");
        btnAdmin = new JButton("ADMIN MANAGEMENT");
        btnCate = new JButton("CATEGORY MANAGEMENT");
        btnLogOut = new JButton("LOG OUT");

        lblAdmin = new ImageLabel(new ImageIcon("src\\project\\Img\\q1.png").getImage());
        lblMember = new ImageLabel(new ImageIcon("src\\project\\Img\\q2.png").getImage());
        lblSong = new ImageLabel(new ImageIcon("src\\project\\Img\\q3.png").getImage());
        lblCate = new ImageLabel(new ImageIcon("src\\project\\Img\\q4.png").getImage());
        lblLogOut = new ImageLabel(new ImageIcon("src\\project\\Img\\q5.png").getImage());
        lblExit = new ImageLabel(new ImageIcon("src\\project\\Img\\q6.png").getImage());

        mainPanel.setBounds(1000, 0, 250, 650);
        panel.setBounds(0, 0, 1250, 650);

        lblAdmin.setBounds(20, 20, 200, 80);
        lblMember.setBounds(20, 120, 200, 80);
        lblSong.setBounds(20, 220, 200, 80);
        lblCate.setBounds(20, 320, 200, 80);
        lblLogOut.setBounds(20, 420, 200, 80);
        lblExit.setBounds(20, 520, 200, 80);

        lblAdmin.addMouseListener(new MouseAdmin());
        lblMember.addMouseListener(new MouseMember());
        lblSong.addMouseListener(new MouseSong());
        lblCate.addMouseListener(new MouseCate());
        lblLogOut.addMouseListener(new MouseLogOut());
        lblExit.addMouseListener(new MouseExit());

        mainPanel.add(lblAdmin);
        mainPanel.add(lblMember);
        mainPanel.add(lblSong);
        mainPanel.add(lblCate);
        mainPanel.add(lblLogOut);
        mainPanel.add(lblExit);

        frame.add(listAdmin.panel);
        frame.add(listMember);
        frame.add(listSong);
        frame.add(listCategory);

        frame.add(mainPanel);
        frame.add(panel);

        mainPanel.setLayout(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public class MouseAdmin implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            listCategory.setVisible(false);
            listSong.setVisible(false);
            listMember.setVisible(false);
            listAdmin.panel.setVisible(true);
            listAdmin.loadData();

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblAdmin.setBounds(15, 20, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblAdmin.setBounds(20, 20, 200, 80);
        }
    }

    public class MouseMember implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            listCategory.setVisible(false);
            listAdmin.panel.setVisible(false);
            listSong.setVisible(false);
            listMember.setVisible(true);
            listMember.loadData();

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblMember.setBounds(15, 120, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblMember.setBounds(20, 120, 200, 80);
        }
    }

    public class MouseSong implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            listAdmin.panel.setVisible(false);
            listCategory.setVisible(false);
            listMember.setVisible(false);
            listSong.setVisible(true);
            try {
                listSong.loadData();
            } catch (Exception ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblSong.setBounds(15, 220, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblSong.setBounds(20, 220, 200, 80);
        }
    }

    public class MouseCate implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            listAdmin.panel.setVisible(false);
            listMember.setVisible(false);
            listSong.setVisible(false);
            listCategory.setVisible(true);
            listCategory.loadData();
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblCate.setBounds(15, 320, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblCate.setBounds(20, 320, 200, 80);
        }
    }

    public class MouseLogOut implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            LoginFram loginFram = new LoginFram();
            frame.setVisible(false);

        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblLogOut.setBounds(15, 420, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblLogOut.setBounds(20, 420, 200, 80);
        }
    }

    public class MouseExit implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.exit(0);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            lblExit.setBounds(15, 520, 200, 80);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            lblExit.setBounds(20, 520, 200, 80);
        }
    }

}
