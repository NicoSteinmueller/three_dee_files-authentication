package com.three_dee_files.authentification.repositorys;

import com.three_dee_files.authentification.tables.BackendKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackendKeyRepository extends JpaRepository<BackendKey, String> {
}
