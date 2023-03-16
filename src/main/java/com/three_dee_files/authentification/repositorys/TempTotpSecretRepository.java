package com.three_dee_files.authentification.repositorys;

import com.three_dee_files.authentification.tables.Account;
import com.three_dee_files.authentification.tables.TempTotpSecret;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempTotpSecretRepository extends JpaRepository<TempTotpSecret, Integer> {
    boolean existsByAccount (Account account);
    void deleteByAccount (Account account);
    TempTotpSecret getTempTotpSecretByAccount (Account account);
}
