package SetupTools;

import com.three_dee_files.authentification.helper.HashUtilities;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;

public class GenerateBackendKeyAndKeyHash {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[80];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();


        String secureString = base32.encodeAsString(bytes);
        System.out.println("KEY: "+secureString);
        String temp32 = base32.encodeAsString(HashUtilities.hashSHA512(secureString));
        System.out.println("HASH: "+ temp32);
    }
}
