package com.example.a2javaprogramming;

import java.util.LinkedList;

public class Project {
    private int projectID;
    private String projectName;
    private String username;

    public Project(int projectID, String projectName, String username){
        this.projectID = projectID;
        this.projectName = projectName;
        this.username = username;
    }

    //getters
    public String getUsername() {
        return username;
    }
    public int getProjectID() {
        return projectID;
    }
    public String getProjectName() {
        return projectName;
    }


    //setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
