package com.eddy.dsaclockbackend.dsaclock;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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
