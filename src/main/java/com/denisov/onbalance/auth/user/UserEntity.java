package com.denisov.onbalance.auth.user;

import javax.persistence.*;

@Entity
@Table(name="app_user")
public class UserEntity {
    @Id
    @SequenceGenerator(
            name = "appUserSequence",
            sequenceName = "appUserSequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "appUserSequence")
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    private String password;

    public UserEntity(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UserEntity(){

    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String toString(){
        return "UserEntity: " + id + " "  + name + " "  + email + " "  + password + " ";
    }
}
