package com.three_dee_files.authentification.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table
public class BackupCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(columnDefinition = "int")
    private Account account;

    @Column(nullable = false)
    private String otp;

    public BackupCode(Account account, String otp){
        this.account = account;
        this.otp = otp;
    }
}
