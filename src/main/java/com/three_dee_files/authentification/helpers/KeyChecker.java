package com.three_dee_files.authentification.helpers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class KeyChecker {

    private static String BACKENDKEY;

    @Value("${app.backend.key}")
    protected void setBACKENDKEY(String key){
        BACKENDKEY = key;
    }
    public boolean isInvalidBackendKey(String key){
        return !BACKENDKEY.equals(key);
    }
}
