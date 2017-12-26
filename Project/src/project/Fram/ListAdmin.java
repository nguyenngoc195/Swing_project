/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.Fram;

import com.google.gson.Gson;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import project.Entity.Admin;
import project.Entity.AdminListResponse;
import project.Entity.PageInfor;

/**
 *
 * @author Lan
 */
public class ListAdmin {

    public JPanel panel;
    private String API_URL = "https://1-dot-api-person.appspot.com/_api/v1/admins/key";
    private PageInfor pageInfor;

    private JLabel lblTotalPate;

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JButton btnFirst, btnPrevious, btnGo, btnNext, btnLast, btnAddSong, btnLoad, btnExit;
    private JTextField txtCurrentPage;
    AdminFram adminFram = null;

    public ListAdmin() {
        super();
        initComponent();
    }

    private void initComponent() {
        pageInfor = new PageInfor();
        panel = new JPanel();

        panel.setBounds(0, 0, 1000, 650);
        panel.setLayout(null);
        //  panel = new ImagePanel(new ImageIcon("C:\\Users\\Lan\\Desktop\\project\\img\\e6.png").getImage());
           panel.setBackground(Color.decode("#001a22"));
        String[] columnNames = {"User Name", "Name","Email","Phone"};
        Object[][] data = {};
        tableModel = new DefaultTableModel(data, columnNames){
         @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                
                int row = table.getSelectedRow();
                String id = tableModel.getValueAt(row, 0).toString();

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        scrollPane = new JScrollPane(table); // Đưa bảng vào khung.
        scrollPane.setBounds(20, 50, 950, 475); // Set vị trí và kích thước cho khung.
        panel.add(scrollPane); // Đưa khung vào frame.

        btnFirst = new JButton("<<");
        btnPrevious = new JButton("<");
        btnGo = new JButton("GO");
        btnNext = new JButton(">");
        btnLast = new JButton(">>");
    
//        btnFirst.setBackground(Color.decode("#313131"));
//        btnPrevious.setBackground(Color.decode("#313131"));
//        btnGo.setBackground(Color.decode("#313131"));
//        btnNext.setBackground(Color.decode("#313131"));
//        btnLast.setBackground(Color.decode("#313131"));
        
        
        

        btnAddSong = new JButton("Add");
        btnLoad = new JButton("Load");

        btnExit = new JButton("Exit");

        txtCurrentPage = new JTextField();

        btnFirst.setBounds(100, 550, 50, 30);
        btnPrevious.setBounds(170, 550, 50, 30);
        txtCurrentPage.setBounds(240, 550, 50, 30);
        btnGo.setBounds(300, 550, 60, 30);
        btnNext.setBounds(380, 550, 50, 30);
        btnLast.setBounds(450, 550, 50, 30);
        btnAddSong.setBounds(650, 550, 80, 30);
        btnLoad.setBounds(750, 550, 80, 30);

        btnExit.setBounds(850, 550, 80, 30);

        table.setRowHeight(45);

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        btnGo.addActionListener(new PaginationHandle());
        btnFirst.addActionListener(new PaginationHandle());
        btnPrevious.addActionListener(new PaginationHandle());
        btnNext.addActionListener(new PaginationHandle());
        btnLast.addActionListener(new PaginationHandle());
        btnAddSong.addActionListener(new AddHandle());
        btnLoad.addActionListener(new LoadHandle());

        btnExit.addActionListener(new ExitHandle());

        panel.add(btnFirst);
        panel.add(btnPrevious);
        panel.add(txtCurrentPage);
        panel.add(btnGo);
        panel.add(btnNext);
        panel.add(btnLast);
        panel.add(btnAddSong);
        panel.add(btnLoad);

        panel.add(btnExit);

        panel.setVisible(false);

        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);

        try {

            Gson gson = new Gson();
            String url = API_URL + "?page=" + pageInfor.getPage() + "&limit=" + pageInfor.getLimit();

            String responseStr = Jsoup.connect(url).ignoreContentType(true).header("AdminAuth", LoginFram.tokenKey).method(Connection.Method.GET).execute().body();

            AdminListResponse responseData = gson.fromJson(responseStr, AdminListResponse.class);

            pageInfor = new PageInfor();
            pageInfor.setLimit(responseData.getLimit());
            pageInfor.setPage(responseData.getPage());
            pageInfor.setTotalItem(responseData.getTotalItem());
            pageInfor.setTotalPage(responseData.getTotalPage());

            txtCurrentPage.setText(String.valueOf(pageInfor.getPage()));

            for (Admin admin : responseData.getData()) {

                Object[] row = {admin.getUserName(), admin.getName(),admin.getEmail(),admin.getPhone()};
                tableModel.addRow(row);
            }
            handlePaginateButton();
        } catch (IOException ex) {
            Logger.getLogger(ListSong.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class LoadHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            loadData();
        }

    }

    private void handlePaginateButton() {
        if (pageInfor.getPage() <= 1) {
            btnFirst.setEnabled(false);
            btnPrevious.setEnabled(false);
        } else {
            btnFirst.setEnabled(true);
            btnPrevious.setEnabled(true);
        }
        if (pageInfor.getPage() == pageInfor.getTotalPage()) {
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
        }
    }

    class PaginationHandle implements ActionListener {

        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnGo) {
                try {
                    int inputPage = Integer.parseInt(txtCurrentPage.getText());
                    if (inputPage == pageInfor.getPage()) {
                        return;
                    }
                    if (inputPage > pageInfor.getTotalPage() || inputPage <= 0) {
                        JOptionPane.showMessageDialog(null, "????", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    pageInfor.setPage(inputPage);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (e.getSource() == btnFirst) {
                pageInfor.setPage(1);
            } else if (e.getSource() == btnPrevious) {
                if (pageInfor.getPage() > 1) {
                    pageInfor.setPage(pageInfor.getPage() - 1);
                }
            } else if (e.getSource() == btnNext) {
                if (pageInfor.getPage() < pageInfor.getTotalPage()) {
                    pageInfor.setPage(pageInfor.getPage() + 1);
                }
            } else if (e.getSource() == btnLast) {
                pageInfor.setPage(pageInfor.getTotalPage());
            }
            txtCurrentPage.setText(String.valueOf(pageInfor.getPage()));
            loadData();
        }
    }

    public class ExitHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.setVisible(false);
        }

    }

    public class AddHandle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (adminFram == null) {
                adminFram = new AdminFram();
            } else {
                adminFram.frame.setVisible(false);
                adminFram = new AdminFram();
            }
        }
    }

}
