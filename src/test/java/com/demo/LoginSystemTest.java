package com.demo;

import com.demo.pojo.User;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * This is a UserTest class for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Xinying Ma
 * @version March 31, 2024
 */

public class LoginSystemTest {
    private LoginSystem loginSystem;
    private Database database;

    @Before
    public void setUp() throws BadDataException {
        loginSystem = new LoginSystem() {
            @Override
            public void onLoginSuccess(String username) {

            }
        };

        // Add some test users to the database
        User newUser1 = new User("user1,password1,[],[],[]");
        User newUser2 = new User("user2,password2,[],[],[]");
        database.getUsers().add(newUser1);
        database.getUsers().add(newUser2);
    }

    @Test
    public void testSignUpSuccess() {
        String input = "newuser\npassword123!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(loginSystem.signUp());
        assertTrue(database.isUser("newuser"));
    }

    @Test
    public void testSignUpFailure() {
        String input = "user1\npassword";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(loginSystem.signUp());
    }

    @Test
    public void testLoginSuccess() {
        String input = "user1\npassword1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertTrue(loginSystem.logIn());
    }

    @Test
    public void testLoginFailure() {
        String input = "user1\nwrongpassword!!!";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        assertFalse(loginSystem.logIn());
    }

    @Test
    public void testIsProtectedPasswordValid() {
        assertTrue(loginSystem.isProtectedPassword("password123454!?"));
        assertTrue(loginSystem.isProtectedPassword("Abc123#$"));
    }

    @Test
    public void testIsProtectedPasswordInvalid() {
        assertFalse(loginSystem.isProtectedPassword("password"));
        assertFalse(loginSystem.isProtectedPassword("12345"));
    }
}