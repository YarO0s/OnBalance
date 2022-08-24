package com.denisov.onbalance.task;

import com.denisov.onbalance.activity.ActivityEntity;

import javax.persistence.*;

@Entity
@Table(name="task")
public class TaskEntity {

    @Id
    @SequenceGenerator(
            name = "taskSequence",
            sequenceName = "taskSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "taskSequence")
    private long id;

    @Column(name = "title", nullable = false, unique = false)
    private String title;

    @Column(name = "description", nullable = false, unique = false)
    private String description;

    @Column(name = "completion_status", nullable = false, unique = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "activityId", referencedColumnName = "id", nullable = false)
    ActivityEntity activityId;

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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ActivityEntity getActivityId(){
        return activityId;
    }

    public TaskEntity(String title, String description, boolean status, ActivityEntity activityId){
        this.title = title;
        this.description = description;
        this.status = status;
        this.activityId = activityId;
    }

    public TaskEntity(){

    }
}
