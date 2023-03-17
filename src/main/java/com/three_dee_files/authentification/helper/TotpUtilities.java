package com.three_dee_files.authentification.helper;

import com.three_dee_files.authentification.repositorys.BackupCodeRepository;
import com.three_dee_files.authentification.tables.Account;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class TotpUtilities {
    @Autowired
    private BackupCodeRepository backupCodeRepository;

    static long STEPSIZE = 30000;
    public static String generateNewSecret(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[40];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeAsString(bytes);
    }

    @Transactional
    public boolean validate(Account account, String otp){
        if (validate(account.getTotpSecret(), otp))
            return true;
        if (backupCodeRepository.existsBackupCodeByAccountAndOtp(account, otp)){
            backupCodeRepository.deleteByAccountAndOtp(account, otp);
            return true;
        }
        return false;
    }

    public static boolean validate(String secret, String otp){
        long step = System.currentTimeMillis() / STEPSIZE;
        return getOTP(step, secret).equals(otp) || (step > 0 && getOTP(step-1, secret).equals(otp));
    }

    private static String getOTP(final long step, final String secret){
        StringBuilder steps = new StringBuilder(Long.toHexString(step).toUpperCase());
        while (steps.length() < 16)
            steps.insert(0, "0");

        byte[] bytesSteps = new byte[0];
        try {
            bytesSteps = Hex.decodeHex(String.valueOf(steps));
        } catch (DecoderException ignored) {}

        Base32 base32 = new Base32();
        byte[] bytesSecret = base32.decode(secret);

        byte[] bytesHash = hash(bytesSecret, bytesSteps);

        int offset = bytesHash[bytesHash.length - 1] & 0xf;
        int binary = ((bytesHash[offset] & 0x7f) << 24) | ((bytesHash[offset + 1] & 0xff) << 16) | ((bytesHash[offset + 2] & 0xff) << 8) | (bytesHash[offset + 3] & 0xff);
        int otp = binary % 1000000;

        StringBuilder result = new StringBuilder(Integer.toString(otp));
        while (result.length() < 6)
            result.insert(0, "0");

        return result.toString();
    }

    private static byte[] hash(byte[] bytesSecret, byte[] bytesSteps){
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec macSecret = new SecretKeySpec(bytesSecret, "RAW");
            mac.init(macSecret);
            return mac.doFinal(bytesSteps);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

}
