package com.demo.interf;

/**
 * This is a Login interface for Team Project.
 *
 * Purdue University -- CS18000 -- Spring 2024 -- Team Project
 *
 * @author Abhiru Palhan
 * @version March 29, 2024
 */
public interface LoginSystemInterface { // interface that stores all Login methods
    boolean signUp();
    boolean logIn();
    boolean isProtectedPassword(String password);

}
