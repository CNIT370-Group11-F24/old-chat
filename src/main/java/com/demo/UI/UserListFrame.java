package com.demo.UI;

import com.demo.Database;
import com.demo.pojo.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.function.Predicate;

public class UserListFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private String myUserName = null;
    private User myUser = null;

    public void initView(){
        setTitle("User List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initModel();
        table = new JTable(tableModel);
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(100);
        column.setCellEditor(new DefaultCellEditor(new JTextField()){
            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return false;
            }
        });
        column.setMinWidth(100);
        column.setMaxWidth(100);
        column.setCellRenderer(new ImageRenderer());

        table.setRowHeight(100); // Set row height to adjust the size of the avatar
        table.setPreferredScrollableViewportSize(new Dimension(600, 400)); // Set preferred size of the table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                String username = (String) tableModel.getValueAt(row, 1);
                if (e.getClickCount() == 2) {
                    ChatList chatList = new ChatList(myUserName, username);
                    chatList.setVisible(true);
                }
            }
        });

        // 添加搜索功能
        // 添加一个水平容器
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();

            Database database = new Database();
            ArrayList<User> userList = database.getUsers();
            if (searchText.isEmpty()) {
//                if(myUserName != null) {
//                    initFrame(database.getUsers(myUserName));
//                } else {
//
//                }
                userList.removeIf(a -> {
                    return a.getUsername().equals(myUserName);
                });
                initFrame(userList);
                return;
            }
            ArrayList<User> searchList = new ArrayList<>();
            for (User user : userList) {
                if (user.getUsername().toLowerCase().contains(searchText.toLowerCase())  && !user.getUsername().equals(myUserName)) {
                   searchList.add(user);
                }
            }
            initFrame(searchList);
        });

        // 查看全部用户
        if(myUserName != null) {
            JButton allButton = new JButton("All");
            allButton.addActionListener(e -> {
                Database database = new Database();
                ArrayList<User> users = database.getUsers();
                users.removeIf(new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        if(user.getUsername().equals(myUserName)) {
                            return true;
                        }
                        return false;
                    }
                });
                initFrame(users);
            });
            searchPanel.add(allButton);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initModel(){
        tableModel = new DefaultTableModel();
//        tableModel.addColumn("ID");
        tableModel.addColumn("Avatar");
        tableModel.addColumn("Name");
//        tableModel.addColumn("msg");
//        tableModel.addColumn("add");
//        tableModel.addColumn("reject");
//        tableModel.addColumn("move");
//        tableModel.addColumn("block");
//        tableModel.addColumn("unblock");
    }

    private void initButtons(){

    }

    public UserListFrame(String myUserName) {
        this.myUserName = myUserName;
        initView();
        initButtons();
        setVisible(true);
    }
    private void initFrame(ArrayList<User> userList){
        tableModel.setRowCount(0);
        for (User user : userList) {
            Object[] rowData = {
                    new ImageIcon(user.getAvatar()),
                    user.getUsername()
            };
            tableModel.addRow(rowData);
        }
        table.addNotify();
    }

    private static void updateList(){
        new UserListFrame("12");
    }

    public static void main(String[] args) {
        updateList();
    }

    class ButtonRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            User user;
            if(value instanceof User) {
                user = (User) value;
            } else {
                return null;
            }
            // 水平布局
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            if(user.getIsFrend()) {
                JButton button = new JButton("msg");
                button.setPreferredSize(new Dimension(100, 30));
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                panel.add(button);
                button.addActionListener(e -> {
                    ChatList chatList = new ChatList(myUserName, user.getUsername());
                    chatList.setVisible(true);
                });

                // remove
                JButton button1 = new JButton("remove");
                button1.setPreferredSize(new Dimension(100, 30));
                button1.setBackground(Color.ORANGE);
                button1.setForeground(Color.RED);
                button1.setBorderPainted(false);
                button1.setFocusPainted(false);
                panel.add(button1);
            }
            else if(user.getIsRequest())  {
                JButton button = new JButton("accept");
                button.setPreferredSize(new Dimension(100, 30));
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                panel.add(button);

                // reject
                JButton button1 = new JButton("reject");
                button1.setPreferredSize(new Dimension(100, 30));
                button1.setBackground(Color.GREEN);
                button1.setForeground(Color.WHITE);
                button1.setBorderPainted(false);
                button1.setFocusPainted(false);
                panel.add(button1);
            }
            else if(user.getIsBlocked()) {
                JButton button = new JButton("unblock");
                button.setPreferredSize(new Dimension(100, 30));
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                panel.add(button);
            }
            else {
                JButton button = new JButton("add");
                button.setPreferredSize(new Dimension(100, 30));
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
                panel.setComponentZOrder(button, 0);
                button.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Database database = new Database();
                        boolean b = database.addFriendRequest(myUserName, user.getUsername());
                        if(b) {
                            JOptionPane.showMessageDialog(null, "add friend request success");
                        } else {
                            JOptionPane.showMessageDialog(null, "add friend request fail");
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

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                panel.add(button);
            }
            return panel;
        }
    }

}

class ImageRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value != null && value instanceof ImageIcon) {
            ImageIcon icon = (ImageIcon) value;
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Resize the image
            label.setIcon(new ImageIcon(newImg));
        }
        return label;
    }
}

abstract class TableActionButton extends DefaultTableCellRenderer {
    abstract void actionPerformed();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null && value instanceof String) {
            String text = (String) value;
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(100, 30));
            button.setBackground(Color.GREEN);
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    actionPerformed();
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
        }
        return new JLabel();
    }
}