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

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] passwordHash;

    public Account(String email, byte[] passwordHash){
        this.email = email;
        this.passwordHash = passwordHash;
    }

}
