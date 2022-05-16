package com.denisov.onbalance.auth.confirmation;

import com.denisov.onbalance.auth.user.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="confirmation_token")
public class ConfirmationTokenEntity {

    @SequenceGenerator(
            name = "confirmationTokenSequence",
            sequenceName = "confirmationTokenSequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "confirmationTokenSequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(nullable = true)
    private LocalDateTime confirmationTime;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false)
    private UserEntity appUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getConfirmationTime() {
        return confirmationTime;
    }

    public void setConfirmationTime(LocalDateTime confirmationTime) {
        this.confirmationTime = confirmationTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public ConfirmationTokenEntity(){

    }

    //TODO: move user declaration to associated dataMapper
    public UserEntity getUser(){
        return appUserId;
    }

    public void setUser(UserEntity appUserId){
        this.appUserId = appUserId;
    }

    public ConfirmationTokenEntity(String token, LocalDateTime creationTime, LocalDateTime confirmationTime,
                                   LocalDateTime expirationTime, UserEntity appUserId){
        this.token = token;
        this.creationTime = creationTime;
        this.confirmationTime = confirmationTime;
        this.expirationTime = expirationTime;
        this.appUserId = appUserId;
    }

    public String toString(){
        return "Confirmation token entity: " + id + " " + token + " " + creationTime + " " + confirmationTime + " " + expirationTime;
    }

    //TODO: override hashCode and equals
}
