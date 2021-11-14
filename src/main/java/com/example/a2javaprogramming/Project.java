package com.example.a2javaprogramming;

import java.util.LinkedList;

public class Project {
    private int projectID;
    private String projectName;
    private String username;
    LinkedList<Column> tasks = new LinkedList<Column>();

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

    public LinkedList<Column> getTasks() {
        return tasks;
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

    public void setTasks(LinkedList<Column> tasks) {
        this.tasks = tasks;
    }
}
