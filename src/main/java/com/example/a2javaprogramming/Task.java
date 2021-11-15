package com.example.a2javaprogramming;

public class Task {
    private int taskId;
    private String taskName;
    private String taskStatus;
    private int columnId;

    public Task(int taskId, String taskName, String taskStatus, int columnId){
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskStatus = taskStatus;
        this.columnId = columnId;
    }
}
