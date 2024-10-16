package com.krzysiek.recruiting.model;

import com.krzysiek.recruiting.enums.Role;
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

    @Column(nullable = false)
    private String password;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed  = false;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<File> files = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecruitmentProcess> recruitmentProcesses = new HashSet<>();

    // JPA required
    public User(){}

    public User(String email, String password, String confirmationToken){
        this.email = email;
        this.password = password;
        this.isConfirmed = false;
        this.confirmationToken = confirmationToken;
    }

}
