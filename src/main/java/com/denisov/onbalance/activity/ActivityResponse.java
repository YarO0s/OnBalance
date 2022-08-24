package com.denisov.onbalance.activity;

public class ActivityResponse {
    private String color;
    private String name;
    private String description;
    private int completionStatus;
    private long id;

    public ActivityResponse(long id, int completionStatus, String name, String color, String description){
        this.id = id;
        this.completionStatus = completionStatus;
        this.name = name;
        this.color = color;
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
