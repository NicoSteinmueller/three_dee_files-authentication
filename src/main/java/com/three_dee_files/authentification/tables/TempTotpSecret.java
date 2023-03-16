package com.three_dee_files.authentification.tables;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table
public class TempTotpSecret {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int ID;

    @OneToOne(cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(columnDefinition = "int")
    private Account account;

    @Column(nullable = false, columnDefinition = "varchar(64)")
    private String totpSecret;

    public TempTotpSecret(Account account, String totpSecret){
        this.account = account;
        this.totpSecret = totpSecret;
    }
}
