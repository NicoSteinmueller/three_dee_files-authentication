package com.three_dee_files.authentification.repositorys;

import com.three_dee_files.authentification.tables.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    boolean existsAccountByEmailAndPasswordHash(String email, byte[] passwordHash);
    Account getAccountByEmailAndPasswordHash (String email, byte[] passwordHash);
    boolean existsAccountByEmail (String email);
    void deleteAccountByEmail (String email);
}
