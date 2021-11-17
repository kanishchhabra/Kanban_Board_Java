package com.example.a2javaprogramming;

import java.sql.Date;

public class Task {
    private int taskId;
    private String taskName;
    private String taskStatus;
    private int columnId;
    private String taskDescription;
    private Date dueDate;

    public Task(int taskId, String taskName, String taskStatus, int columnId, Date dueDate, String taskDescription){
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.columnId = columnId;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
    }

    //Setters
    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    //Getters
    public int getColumnId() {
        return columnId;
    }
    public int getTaskId() {
        return taskId;
    }
    public String getTaskName() {
        return taskName;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public String getTaskDescription() {
        return taskDescription;
    }
}
