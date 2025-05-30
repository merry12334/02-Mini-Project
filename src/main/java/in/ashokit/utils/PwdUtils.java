package in.ashokit.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;

public class PwdUtils {

    public static String generateRandomPwd() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        SecureRandom secureRandom = new SecureRandom();

        return RandomStringUtils.random(
                6,                        // Length
                0,                        // Start index (inclusive)
                chars.length(),           // End index (exclusive)
                false,                    // Letters only? false
                false,                    // Numbers only? false
                chars.toCharArray(),      // Allowed characters
                secureRandom              // Secure random instance
        );
    }
}
