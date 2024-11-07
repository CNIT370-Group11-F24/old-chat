package com.demo;

import com.demo.UI.UserListFrame;
import com.demo.constrants.Config;
import com.demo.interf.LoginSystemInterface;
import com.demo.pojo.User;
import com.demo.utils.FileUploadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * This is a Login class for Team Project.
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan, Miguel
 * @version April 9, 2024
 */
public abstract class LoginSystem extends JFrame implements LoginSystemInterface {
    private Database database;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String avatarPath;

    private int maxTry = Config.MAX_TRY;

    public LoginSystem() {
        this.database = new Database();
        setTitle("Login System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 300);

        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // 头像
        JLabel avatarLabel = new JLabel();
        avatarLabel.setText("avatar:");
        loginPanel.add(avatarLabel);

        // 用户点击
        JLabel avatar = new JLabel();
        avatar.setText("choose");
        // 点击后可选择文件图片
        avatar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().toLowerCase().endsWith(".jpg") || f.isDirectory();
                    }

                    @Override
                    public String getDescription() {
                        return "JPG Files";
                    }
                });
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getAbsolutePath().endsWith(".jpg")){
                        JOptionPane.showMessageDialog(null, "Please select a .jpg file.");
                        return;
                    }
                    // 处理上传逻辑，可以在这里将selectedFile上传到服务器或者进行其他操作
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    String s = FileUploadUtil.uploadFileToLocal(selectedFile, "upload/");

                    avatarPath = s;
                    ImageIcon icon = FileUploadUtil.loadImage(s);
                    Image img = icon.getImage();
                    Image newImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Resize the image
                    avatar.setIcon(new ImageIcon(newImg));
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
        loginPanel.add(avatar);
      
        JLabel usernameLabel = new JLabel("Username:");
        loginPanel.add(usernameLabel);

        usernameField = new JTextField(20);
        loginPanel.add(usernameField);

        
        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        
        loginPanel.add(passwordField);

        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> logIn());
        loginPanel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> signUp());
    
        loginPanel.add(signUpButton);

        add(loginPanel, BorderLayout.CENTER);
    }

    @Override
    public boolean signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(null == avatarPath || avatarPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invalid avatar.");
            return false;
        }

        if (!isProtectedPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password.");
            return false;
        }

        database.readUsers();

        if (database.isUser(username)) {
            JOptionPane.showMessageDialog(this, "User exists already.");
            return false;
        } else {
            User user = new User(avatarPath, username, password);
            String data = user.toString();
            try {
                database.writeUser(new User(data));
                JOptionPane.showMessageDialog(this, "User successfully created.");
                return true;
            } catch (BadDataException e) {
                JOptionPane.showMessageDialog(this, "Error: Bad user data format.");
                e.printStackTrace();
                return false;
            }
        }
    }

    public abstract void onLoginSuccess(String username);

    @Override
    public boolean logIn() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        maxTry--;
        if (maxTry == 0) {
            JOptionPane.showMessageDialog(this, "You have no more times to try.");
            return false;
        }
        if (!isProtectedPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password.You only has" + maxTry + " times now.");
            return false;
        }
    
        database.readUsers();
    
        for (User user : database.getUsers()) {
            if (user.getUsername().equals(username)) {
                if (user.getPassword().equals(password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    this.setVisible(false);
                    onLoginSuccess(username);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong password.You only has" + maxTry + " times now.");
                    return false;
                }
            }
        }
    
        JOptionPane.showMessageDialog(this, "User does not exist.You only has" + maxTry + " times now.");
        return false;
    }

    @Override
    public boolean isProtectedPassword(String password) {
        char[] spec = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '-'};

        if (password.length() <= 5) {
            return false;
        }

        for (char special : spec) {
            if (password.indexOf(special) != -1) {
                return true;
            }
        }

        return false;
    }

    public int getUserCount() {
        database.readUsers();
        return database.getUsers().size();
    }

    public boolean promptSignUp() {
        if (getUserCount() < 2) {
            String password = new String(passwordField.getPassword());
    
            if (password.equals("")) {
                JOptionPane.showMessageDialog(this, "Please Sign Up");
                return false;
            }
            return signUp();
        }

        return false;
    }

    public static void main(String[] args) {
        LoginSystem gui = new LoginSystem(){
            @Override
            public void onLoginSuccess(String username) {
                UserListFrame userListFrame = new UserListFrame(username);
                userListFrame.setVisible(true);
            }
        };
//        if(gui.promptSignUp()) {
//
//        }
        gui.setVisible(true);
    }
}