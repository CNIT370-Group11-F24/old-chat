package com.demo.interf;

import com.demo.pojo.User;

import java.util.ArrayList;
/**
 * This is a DataBase interface for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan
 * @version March 30, 2024
 */
public interface DatabaseInterface { // interface that stores all database methods
    ArrayList<User> getUsers();
    void setUsers(ArrayList<User> users);
    boolean readUsers();
    boolean isUser(String username);
    boolean newMessage(String username, String message);
    boolean addFriend(String username, String friend);
    boolean removeFriend(String username, String friend);
    boolean blockFriend(String username, String friend);
    boolean unblockFriend(String username, String friend);
    User searchUser(String username, String searchUser);
    String viewUser(String username, String viewUser);
    boolean writeUser(User user);
}
