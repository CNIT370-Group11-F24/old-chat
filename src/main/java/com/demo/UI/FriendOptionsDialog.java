package com.demo.UI;

import com.demo.Database;
import com.demo.pojo.User;

import javax.swing.JOptionPane;

public class FriendOptionsDialog {

    private String herUserName;
    private String myUserName;


    public FriendOptionsDialog(String herUserName, String myUserName) {
        this.herUserName = herUserName;
        this.myUserName = myUserName;
    }


    public void addFriendRequest() {
        Database database = new Database();
        database.addFriendRequest(myUserName, herUserName);
    }

    public void acceptRequest() {
        Database database = new Database();
        database.addFriend(myUserName, herUserName);
        database.removeFriendRequest(myUserName, herUserName);
    }

    public void removeRequest() {
        Database database = new Database();
        database.removeFriendRequest(myUserName, herUserName);
    }

    public void rejectRequest() {
        Database database = new Database();
        database.removeFriendRequest(myUserName, herUserName);
    }

    public void removeFriend() {
        Database database = new Database();
        database.removeFriend(myUserName, herUserName);
    }

    public void showDialog() {
        Database database = new Database();
        User user = database.getUser(myUserName);
        // 是好友
        if(user.getIsFrend(herUserName)) {
            // 添加好友，移除好友
            String[] options = {"Remove Friend"};
            int choice = JOptionPane.showOptionDialog(null, "Please select an action:", "Friend Options",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (choice) {
                case 0:
                    // 移除好友
                    removeFriend();
                    break;
            }
        }
        // 正在申请中
        else if(user.getIsRequest(herUserName)) {
            // 接受好友申请，拒绝好友申请
            String[] options = {"Accept Request", "Reject Request"};
            int choice = JOptionPane.showOptionDialog(null, "Please select an action:", "Friend Options",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (choice) {
                case 0:
                    // 接受好友申请
                    acceptRequest();
                    break;
                case 1:
                    // 拒绝好友申请
                    rejectRequest();
                    break;
            }
        }
        // 不是好友
        else {
            // 添加好友
            String[] options = {"Add Friend"};
            int choice = JOptionPane.showOptionDialog(null, "Please select an action:", "Friend Options",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            switch (choice) {
                case 0:
                    // 添加好友
                    addFriendRequest();
                    break;
            }
        }
//        String[] options = {"Add Friend", "Remove Friend", "Block Friend", "Accept Request", "Reject Request"};
//
//        int choice = JOptionPane.showOptionDialog(null, "Please select an action:", "Friend Options",
//                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
//
//        switch (choice) {
//            case 0:
//                // 添加好友
//                System.out.println("Adding friend...");
//                break;
//            case 1:
//                // 移除好友
//                System.out.println("Removing friend...");
//                break;
//            case 2:
//                // 拉黑好友
//                System.out.println("Blocking friend...");
//                break;
//            case 3:
//                // 同意申请
//                System.out.println("Accepting request...");
//                break;
//            case 4:
//                // 拒绝申请
//                System.out.println("Rejecting request...");
//                break;
//            default:
//                // 用户取消操作
//                System.out.println("User cancelled operation.");
//                break;
//        }
    }

    public static void main(String[] args) {
        FriendOptionsDialog dialog = new FriendOptionsDialog("123", "456");
        dialog.showDialog();
    }
}
