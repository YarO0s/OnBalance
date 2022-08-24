package com.denisov.onbalance.task;

public class TaskResponse {
    private long id;
    private String title;
    private String description;
    private boolean status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TaskResponse(long id, String title, String description, boolean status){
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }


}
