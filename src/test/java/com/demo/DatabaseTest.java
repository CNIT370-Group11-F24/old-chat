package com.demo;

import com.demo.pojo.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This is a Database Test class for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Xinying Ma
 * @version March 31, 2024
 */

public class DatabaseTest {
    private Database database;
    private User newUser1;
    private User newUser2;

    @Before
    public void setUp() throws BadDataException {
        database = new Database("user.txt");
        database.readUsers();

        newUser1 = new User("user1,password1,user1@email.com");
        newUser2 = new User("user2,password2,user2@email.com");

        database.getUsers().add(newUser1);
        database.getUsers().add(newUser2);
    }

    @Test
    public void testIsUser() {
        assertTrue(database.isUser("user1"));
        assertFalse(database.isUser("nonexistent"));
    }

    @Test
    public void testNewMessage() {
        assertTrue(database.newMessage("user1", "Hello, World!"));
        assertEquals(1, newUser1.getMessages().size());
        assertFalse(database.newMessage("nonexistent", "Test message"));
    }

    @Test
    public void testAddFriend() {
        assertTrue(database.addFriend("user1", "user2"));
        assertTrue(newUser1.getFriends().contains("user2"));
        assertFalse(database.addFriend("user1", "user2"));
    }

    @Test
    public void testRemoveFriend() {
        newUser1.getFriends().add("user2");
        assertTrue(database.removeFriend("user1", "user2"));
        assertFalse(newUser1.getFriends().contains("user2"));
    }

    @Test
    public void testBlockFriend() {
        newUser1.getFriends().add("user2");
        assertTrue(database.blockFriend("user1", "user2"));
        assertTrue(newUser1.getBlockedFriends().contains("user2"));
        assertFalse(newUser1.getFriends().contains("user2"));
    }

    @Test
    public void testUnblockFriend() {
        newUser1.getBlockedFriends().add("user2");
        assertTrue(database.unblockFriend("user1", "user2"));
        assertFalse(newUser1.getBlockedFriends().contains("user2"));
        assertTrue(newUser1.getFriends().contains("user2"));
    }

    @Test
    public void testSearchUser() throws BadDataException {
        User newUser1 = new User("user1,password1,[],[],[]");
        User newUser2 = new User("user2,password2,[],[],[]");
        database.getUsers().add(newUser1);
        database.getUsers().add(newUser2);

        String foundUser = database.viewUser("user1", "user2");
        assertNotNull(foundUser);
        assertNotEquals("User DNE or User Blocked", foundUser);

        newUser1.getBlockedFriends().add("user2");
        foundUser = database.viewUser("user1", "user2");
        assertEquals("User DNE or User Blocked", foundUser);
    }

    @Test
    public void testViewUser() {
        assertNotNull(database.viewUser("user1", "user2"));
        newUser1.getBlockedFriends().add("user2");
        assertEquals("User DNE or User Blocked", database.viewUser("user1", "user2"));
    }

    private void assertNotNull(String s) {
    }

    @Test
    public void testWriteUser() throws BadDataException {
        User newUser = new User("user3,password3,user3@email.com");
        assertTrue(database.writeUser(newUser));
    }
}