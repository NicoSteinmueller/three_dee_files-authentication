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

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] otp;

    public BackupCode(Account account, byte[] otp){
        this.account = account;
        this.otp = otp;
    }
}
