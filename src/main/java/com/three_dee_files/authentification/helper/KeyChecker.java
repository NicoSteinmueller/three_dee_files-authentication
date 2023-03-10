package com.three_dee_files.authentification.helper;

import com.three_dee_files.authentification.repositorys.BackendKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class KeyChecker {
    @Autowired
    private BackendKeyRepository backendKeyRepository;

    public boolean isValidBackendKey(String key){
        return backendKeyRepository.existsBackendKeyByAPIkey(key);
    }
}
