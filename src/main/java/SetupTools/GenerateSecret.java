package SetupTools;

import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

public class GenerateSecret {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[120];
        random.nextBytes(bytes);

        String secureString = Base64.encodeBase64String(bytes);
        System.out.println("Secret: "+secureString);
    }
}
