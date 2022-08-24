package com.denisov.onbalance.activity;

import com.denisov.onbalance.auth.user.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "activity")
public class ActivityEntity {

    @Id
    @SequenceGenerator(
            name = "activitySequence",
            sequenceName = "activitySequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "activitySequence")
    private long id;

    @Column(name = "name", unique = false, nullable = false)
    private String name;

    @Column(name = "color", unique = false, nullable = false)
    private String color;

    @Column(name = "description", unique = false, nullable = false)
    private String description;

    @Column(name = "completion_Status", unique = false, nullable = false)
    private int completionStatus;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private UserEntity userId;

    public ActivityEntity(){

    }

    public ActivityEntity(String name, String color, String description,
                          int completionStatus, UserEntity userId){
        this.name = name;
        this.color = color;
        this.description = description;
        this.completionStatus = completionStatus;
        this.userId = userId;
    }

    public ActivityEntity(long id, String name, String color, String description,
                          int completionStatus, UserEntity userId){
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.completionStatus = completionStatus;
        this.userId = userId;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object activity){
        if(!(activity instanceof ActivityEntity)){
            return false;
        }
        ActivityEntity activityToCompare = (ActivityEntity) activity;

        if(activityToCompare.getUserId() != this.userId){
            return false;
        }

        if(activityToCompare.getId() != this.id){
            return false;
        }

        if(activityToCompare.getColor() != this.color){
            return false;
        }

        if(activityToCompare.getCompletionStatus() != this.completionStatus){
            return false;
        }

        if(activityToCompare.getName() != this.name){
            return false;
        }

        if(activityToCompare.getDescription() != this.description){
            return false;
        }
        return true;
    }
}
