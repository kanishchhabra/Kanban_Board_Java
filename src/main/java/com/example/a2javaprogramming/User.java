package com.example.a2javaprogramming;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String salt; //used to retrieve the password

    public User(String username, String firstName, String lastName, String password, String salt){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.salt = salt;
    }
}
