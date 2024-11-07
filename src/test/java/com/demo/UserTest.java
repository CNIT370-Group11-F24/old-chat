package com.demo;

import com.demo.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * This is a User Testcase class for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Xinying Ma
 * @version March 31, 2024
 */

public class UserTest {

    @Test
    public void testConstructorWithValidData() throws BadDataException {
        String dataContent = "Newuser1,password1,[\"Hello\",\"This is my second message\"],[\"Tim\",\"Friend\"],[\"Bob\",\"Blocked Friend\"]";
        User user = new User(dataContent);
        assertEquals("newUser1", user.getUsername());
        assertEquals("password1", user.getPassword());
        assertEquals(2, user.getMessages().size());
        assertEquals(2, user.getFriends().size());
        assertEquals(2, user.getBlockedFriends().size());
    }

    @Test(expected = BadDataException.class)
    public void testConstructorWithInvalidData() throws BadDataException {
        String dataContent = "Newuser1,password1,[\"Hello\",\"This is my second message\"],[\"Tim\",\"Friend\"]";
        new User(dataContent);
    }

    @Test
    public void testGettersAndSetters() {
        User newUser = new User(new BadDataException("Bad Data"));
        newUser.setUsername("newUser1");
        newUser.setPassword("password1");
        ArrayList<String> messages = new ArrayList<>(Arrays.asList("Hello", "World"));
        newUser.setMessages(messages);
        ArrayList<String> friends = new ArrayList<>(Arrays.asList("Tim", "Friend"));
        newUser.setFriends(friends);
        ArrayList<String> blockedFriends = new ArrayList<>(Arrays.asList("Bob", "Blocked Friend"));
        newUser.setBlockedFriends(blockedFriends);

        assertEquals("newUser1", newUser.getUsername());
        assertEquals("password1", newUser.getPassword());
        assertEquals(messages, newUser.getMessages());
        assertEquals(friends, newUser.getFriends());
        assertEquals(blockedFriends, newUser.getBlockedFriends());
    }

    @Test
    public void testToString() {
        User newUser = new User(new BadDataException("Bad Data"));
        newUser.setUsername("newUser1");
        newUser.setPassword("password1");
        ArrayList<String> messages = new ArrayList<>(Arrays.asList("Hello", "World"));
        newUser.setMessages(messages);
        ArrayList<String> friends = new ArrayList<>(Arrays.asList("Tim", "Friend"));
        newUser.setFriends(friends);
        ArrayList<String> blockedFriends = new ArrayList<>(Arrays.asList("Bob", "Blocked Friend"));
        newUser.setBlockedFriends(blockedFriends);

        String expected = "newUser1,password1,[Hello, World],[Tim, Friend],[Bob, Blocked Friend]";
        assertEquals(expected, newUser.toString());
    }
}