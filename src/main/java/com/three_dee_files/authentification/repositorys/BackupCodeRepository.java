package com.three_dee_files.authentification.repositorys;

import com.three_dee_files.authentification.tables.Account;
import com.three_dee_files.authentification.tables.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupCodeRepository extends JpaRepository<BackupCode, Integer> {
    boolean existsBackupCodeByAccountAndOtp(Account account, String otp);
    void deleteByAccountAndOtp(Account account, String otp);
    void deleteByAccount(Account account);
}
