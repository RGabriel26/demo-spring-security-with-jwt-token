package com.example.demomakebymespringsecuritywithjwt.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    public User(String firstname, String lastname, String email, String password){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public User(User byEmail) {
        this.firstname = byEmail.getFirstname();
        this.lastname = byEmail.getLastname();
        this.email = byEmail.getEmail();
        this.password = byEmail.getPassword();
    }
}
