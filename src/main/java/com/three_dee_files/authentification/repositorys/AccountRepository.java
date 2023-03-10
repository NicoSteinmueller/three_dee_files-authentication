package com.three_dee_files.authentification.repositorys;

import com.three_dee_files.authentification.tables.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findUsersByEmail(String email);
    boolean existsAccountByEmailAndPasswordHash(String email, String passwordHash);
    Account getAccountByEmailAndPasswordHash (String email, String passwordHash);
}
