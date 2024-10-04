package com.krzysiek.recruiting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "confirmation_link")
    private String confirmationLink;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecruitmentProcess> recruitmentProcesses = new HashSet<>();

    // JPA required
    public User(){}

    public User(String email, String password){
        this.email = email;
        this.password = password;
        this.isConfirmed = false;
        this.confirmationLink = null;
    }

}
