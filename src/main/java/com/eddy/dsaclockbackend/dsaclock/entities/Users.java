package com.eddy.dsaclockbackend.dsaclock.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

// -----------User Entity------------
@Entity
public class Users {

    //User id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //used Long instead of long to get null instead of 0 as an empty id

    //username
    @Column(unique = true, nullable = false)
    private String username;

    //email
    @Email
    @Column(unique = true)
    private String email;
}
