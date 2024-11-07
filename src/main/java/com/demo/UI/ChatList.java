package com.demo.UI;

import com.demo.Client;
import com.demo.Database;
import com.demo.constrants.Message;
import com.demo.utils.FileUploadUtil;
import com.demo.utils.MessageSendUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChatList extends JFrame {

    private static final String CHAT_LIST_MY_FRENDS = "Chat List";

    private JPanel chatListPanel;
    private JTextField inputField;

    private String myUserName;
    private ImageIcon myUserAvatar;
    private String herUserName;
    private ImageIcon herUserAvatar;

    Client client;

    private void sendTextFailedContent(){
        String message = inputField.getText();
        if(!message.isEmpty()) {
            addChatMessage("You", message, false);
            client.sendMessage(message, herUserName);
            inputField.setText("");
        }
    }

    public ChatList(String myUserName, String herUserName) {
        Database database = new Database();
        database.readUsers();
        this.myUserName = myUserName;
        this.herUserName = herUserName;
        this.myUserAvatar = FileUploadUtil.loadAvatar(
                database.getUser(myUserName).avatar
        );

        this.herUserAvatar = FileUploadUtil.loadAvatar(
                database.getUser(herUserName).avatar
        );

        setTitle("Chat List: " + herUserName);
        // 设置窗体的关闭操作为DISPOSE_ON_CLOSE
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        chatListPanel = new JPanel();
        chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(chatListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        inputField = new JTextField();
        inputField.addActionListener(e -> {
            sendTextFailedContent();
        });

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> {
            sendTextFailedContent();
        });

        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        add(inputPanel, BorderLayout.SOUTH);

        client = new Client(){
            @Override
            public void handleMessage(String message) {
                Message message1 = MessageSendUtil.readMessage(message);
                addChatMessage(herUserName, message1.getContent(), true);
            }
        };
        client.run();
        client.joinRoom(myUserName);
    }

    private void addChatMessage(String name, String message, boolean isReceived) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setPreferredSize(new Dimension(200, 80)); // 设置固定的宽度和高度
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // 设置最大高度为200像素
        if (isReceived) {
            JLabel avatarLabel = new JLabel(herUserAvatar);
            JLabel nameLabel = new JLabel(herUserName);
            JTextArea messageArea = new JTextArea(message);
            messageArea.setLineWrap(true);
            messageArea.setEditable(false);
            messagePanel.add(nameLabel, BorderLayout.NORTH);
            messagePanel.add(messageArea, BorderLayout.CENTER);
            messagePanel.add(avatarLabel, BorderLayout.WEST); // 添加头像
            messagePanel.setBackground(Color.white);
            messagePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        } else {
            JLabel avatarLabel = new JLabel(myUserAvatar);
            JLabel nameLabel = new JLabel(myUserName);
            JTextArea messageArea = new JTextArea(message);
            messageArea.setLineWrap(true);
            messageArea.setEditable(false);
            messagePanel.add(avatarLabel, BorderLayout.EAST); // 添加头像
            messagePanel.add(nameLabel, BorderLayout.NORTH);
            messagePanel.add(messageArea, BorderLayout.CENTER);
            messagePanel.setBackground(Color.WHITE);
            messagePanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // 设置右对齐
        }
        messagePanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> {
                    chatListPanel.remove(messagePanel);
                    chatListPanel.revalidate();
                    chatListPanel.repaint();
                });
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
        chatListPanel.add(messagePanel);
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatList chatList = new ChatList("12", "456");
            chatList.setVisible(true);
        });
    }
}
