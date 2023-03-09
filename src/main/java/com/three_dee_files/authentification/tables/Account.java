package com.three_dee_files.authentification.tables;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @Column(nullable = false, columnDefinition = "varchar(320)")
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(512)")
    private String passwordHash;

    public Account(String email, String passwordHash){
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
