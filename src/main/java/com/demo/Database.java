package com.demo;

import com.demo.constrants.Config;
import com.demo.interf.DatabaseInterface;
import com.demo.pojo.User;

import java.io.*;
import java.util.ArrayList;

/**
 * This is a Database class for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan, Savja Santana
 * @version March 29, 2024
 */

public class Database implements DatabaseInterface {
    // Database that stores all users from userFile and does
    // User search
    // User viewer
    // Add, block, and remove friend features

    private ArrayList<User> users;
    private final String usersIn;

    public Database(String usersIn) {
        // initializes array of users and user file
        this.users = new ArrayList<>();
        this.usersIn = usersIn;
    }

    public Database(){
        File file = new File(Config.USER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        this.usersIn = file.getAbsolutePath();
        this.users = new ArrayList<>();
        readUsers();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<User> getUsers(String username) {
        User user = null;
        for (int i = 0; i < users.size(); i++) {
            User tempUser = users.get(i);
            if (tempUser.getUsername().equals(username)) {
                user = tempUser;
                break;
            }
        }
        ArrayList<User> returnUsers = new ArrayList<>();
        if (user != null) {
            ArrayList<String> friendRequests = user.getFriendRequests();
            ArrayList<String> blockedFriends = user.getBlockedFriends();
            // 先显示好友申请
            if(friendRequests != null) {
                for (String friendRequest : friendRequests) {
                    User user1 = getUser(friendRequest);
                    if(user1 == null) continue;
                    user1.setIsRequest(true);
                    if(blockedFriends != null && !blockedFriends.contains(friendRequest)) {
                        returnUsers.add(user1);
                    }
                }
            }
            // 显示好友
            if(user.getFriends() != null) {
                for (String friend : user.getFriends()) {
                    User user1 = getUser(friend);
                    if(user1 == null) continue;
                    user1.setIsFrend(true);
                    returnUsers.add(user1);
                }
            }
        }

        return returnUsers;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public boolean readUsers() {
        users = new ArrayList<>();
        // reads all users in and adds them to users array
        try (BufferedReader br = new BufferedReader(new FileReader(this.usersIn))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    users.add(new User(line));
                } catch (BadDataException e) {
                    users.add(new User(e));
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                // 写入文件

                break;
            }
        }
    }

    public void updateUsers() {
        // 删除文件
        try {
            File file = new File(this.usersIn);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.usersIn, false))) {
            for (User user : users) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUser(String username) {
        // checks to see if user exists
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean newMessage(String username, String message) {
        // adds new messages to user profile
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.getMessages().add(message);
                System.out.println("Message is added.");
                return true;
            }
        }
        return false;
    }

    public boolean addFriendRequest(String username, String friend) {
        // adds friend request to friend request array
        this.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(friend)) {
                if (!user.getBlockedFriends().contains(username)) {
                    user.getFriendRequests().add(username);
                    this.updateUsers();
                    System.out.println("Friend request is added.");
                    return true;
                } else {
                    System.out.println("Friend is blocked.");
                    return false;
                }

            }
        }
        return false;
    }

    public boolean removeFriendRequest(String username, String friend) {
        // removes friend request from friend request array
        this.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(friend)) {
                user.getFriendRequests().remove(username);
                this.updateUsers();
                return true;
            }
        }
        return false;
    }


    public boolean addFriend(String username, String friend) {
        // adds friend to friend array if they aren't blocked
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (!user.getBlockedFriends().contains(friend)) {
                    user.getFriends().add(friend);
                    getUser(friend).getFriends().add(username);
                    updateUsers();
                    System.out.println("Friend is added.");
                    return true;
                } else {
                    System.out.println("Friend is blocked.");
                    return false;
                }
            }
        }
        return false;
    }

    public boolean removeFriend(String username, String friend) {
        // remove friend from friend array
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.getFriends().remove(friend);
                getUser(friend).getFriends().remove(username);
                updateUsers();
                System.out.println("Friend is removed.");
                return true;
            }
        }
        return false;
    }

    public boolean blockFriend(String username, String friend) {
        // add friend to blocked array and remove them from friend array
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.getFriends().remove(friend);
                user.getBlockedFriends().add(friend);
                updateUsers();
                System.out.println("Friend is blocked.");
                return true;
            }
        }
        return false;
    }
    public boolean unblockFriend(String username, String friend) {
        // remove friend from block array and add them to friend array
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.getBlockedFriends().remove(friend);
                System.out.println("Friend is unblocked.");
                return true;
            }
        }
        return false;
    }
    public User searchUser(String username, String searchUser) {
        // Searches from user and returns the user if they aren't blocked
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                if (!user.getBlockedFriends().contains(searchUser)) {
                    return user;
                } else {
                    System.out.println("User DNE or User Blocked");
                    return null;
                }
            }
        }
        return null;
    }

    public String viewUser(String username, String viewUser) {
        // calls search method and returns everything about the user in string format
        User found = searchUser(username, viewUser);

        if (found == null) {
            return "User DNE or User Blocked";
        }
        return found.toString();
    }
    public boolean writeUser(User user) {
        // writes user to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Config.USER_FILE_PATH, true))) {

            bw.write(user.toString() + "\n");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

