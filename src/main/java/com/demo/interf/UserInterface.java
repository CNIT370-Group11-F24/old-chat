package com.demo.interf;

import com.demo.pojo.User;

import java.util.ArrayList;
/**
 * This is a User interface for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan
 * @version March 29, 2024
 */
public interface UserInterface { // interface for user
    User parseArray(String array);
    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    ArrayList<String> getBlockedFriends();
    void setBlockedFriends(ArrayList<String> blockedFriends);
    ArrayList<String> getFriends();
    void setFriends(ArrayList<String> friends);
    ArrayList<String> getMessages();
    void setMessages(ArrayList<String> messages);

    String toString();

}
