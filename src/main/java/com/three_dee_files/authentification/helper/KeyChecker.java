package com.three_dee_files.authentification.helper;

import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.util.Arrays;

@Controller
public class KeyChecker {

    private static byte[] BACKENDKEY;

    @Value("${app.backend.keyHash}")
    protected void setBACKENDKEY(String keyHash){
        Base32 base32 = new Base32();
        BACKENDKEY = base32.decode(keyHash);
    }
    public boolean isInvalidBackendKey(String key){
        byte[] keyHash = HashUtilities.hashSHA512(key);
        return !Arrays.equals(BACKENDKEY, keyHash);
    }
}
