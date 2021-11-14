package com.example.a2javaprogramming;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String password;

    public User(String username, String firstName, String lastName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    //Setters
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    //Getters
    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString(){
        return String.format("Name: %s\nUsername: %s\n", this.firstName + " " + this.lastName, this.username);
    }
}
