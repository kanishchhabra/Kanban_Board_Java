package com.example.a2javaprogramming;

import java.util.LinkedList;

public class Column {
    private int columnId;
    private String columnName;
    private int projectId;

    public Column(int columnId, String columnName, int projectId){
        this.columnId = columnId;
        this.columnName = columnName;
        this.projectId = projectId;
    }

    //Setters
    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    //Getters
    public int getColumnId() {
        return columnId;
    }
    public String getColumnName() {
        return columnName;
    }
    public int getProjectId() {
        return projectId;
    }
}
