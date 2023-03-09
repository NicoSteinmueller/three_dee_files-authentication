package com.three_dee_files.authentification.tables;

import jakarta.persistence.*;

@Entity
@Table
public class BackendKey {

    @Id
    @Column(nullable = false, columnDefinition = "varchar(1024)")
    private String APIkey;

}
