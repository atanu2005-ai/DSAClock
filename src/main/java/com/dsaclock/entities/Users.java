package com.dsaclock.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

// -----------Users Entity------------
@Entity
@Table(name = "Users")
public class Users {

    //Users user_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; //used Long instead of long to get null instead of 0 as an empty user_id

    //username
    @Column(unique = true, nullable = false)
    private String username;

    //email
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    //password
    @Column(nullable = true)
    private String password;

    //revision count
    @Column(nullable = false)
    private int userRevisionCount = 0;

    //auth provider
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;
    public enum AuthProvider { // enum: google/local
        LOCAL,
        GOOGLE
    }

    //setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserRevisionCount(int userRevisionCount) {
        this.userRevisionCount = userRevisionCount;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //getters
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getUserRevisionCount() {
        return userRevisionCount;
    }

    public String getEmail() {
        return email;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public String getPassword() {
        return password;
    }

}
