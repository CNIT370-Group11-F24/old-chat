package com.demo.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.BadDataException;
import com.demo.Database;
import com.demo.interf.UserInterface;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a User class for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan
 * @version March 30, 2024
 */
@Data
public class User extends SimpleUser implements UserInterface {
    // Creates variables for user

    private Boolean isFrend = false;
    private Boolean isBlocked = false;
    private Boolean isRequest = false;

    private ArrayList<String> messages = new ArrayList<String>(); // message array
    private ArrayList<String> friends = new ArrayList<String>(); // friend array
    private ArrayList<String> friendRequests = new ArrayList<String>(); // friend array
    private ArrayList<String> blockedFriends = new ArrayList<>(); // blocked (friend) array

    public User(String data) throws BadDataException {
        try {
            User user = parseArray(data);
            this.username = user.username;
            this.password = user.password;
            this.avatar = user.avatar;
            this.messages = user.messages;
            this.friends = user.friends;
            this.blockedFriends = user.blockedFriends;
            this.friendRequests = user.friendRequests;

        } catch (Exception e) {
            throw new BadDataException("Bad Data");
        }
    }
    public User(BadDataException e) { // Bad User Data
        this.username = String.valueOf(e);
        this.password = String.valueOf(e);
        // Don't get confused: New users also have all arrays set to null
        this.messages = null;
        this.friends = null;
        this.blockedFriends = null;
    }

    public User(String alice, String image) {
        this.username = alice;
        this.avatar = image;
    }

    public Boolean getIsFrend(String friend) {
        return this.friends.contains(friend);
    }

    public Boolean getIsBlocked(String friend) {
        return this.blockedFriends.contains(friend);
    }

    public Boolean getIsRequest(String friend) {
        Database database = new Database();
        User he = database.getUser(friend);
        if(he.friendRequests.contains(username)) {
            return true;
        } else {
            return false;
        }
    }

    public User(String avatarPath, String username, String password) {
        this.username = username;
        this.password = password;
        this.avatar = avatarPath;
    }

    public User parseArray(String array) {
        // 使用json存储
        return JSONObject.parseObject(array, User.class);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

//    public ArrayList<String> parseArray(String array) {
//        // make ArrayList strings when they are read in
//        String[] items = array.substring(1, array.length() - 1).split(",");
//        return new ArrayList<String>(Arrays.asList(items));
//    }

//    @Override
//    public String toString() {
//        return String.format("%s,%s,%s,%s,%s,%s,%s", this.username, this.password,
//                this.messages, this.friends, this.blockedFriends,this.friendRequests, this.avatar);
//    }

}