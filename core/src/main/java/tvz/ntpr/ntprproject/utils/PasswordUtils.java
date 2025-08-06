package tvz.ntpr.ntprproject.utils;

import tvz.ntpr.ntprproject.helper.SpicedPassword;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordUtils {
    private static final String ALGORITHM = "MD5";
    private static final String[] pepper = {
            "_B2sv:b8",
            "4utJctyv",
            "-sBXN)GL",
            "Gg:MM4GJ",
            "/@s?sv)/",
            "(ITBwy*7",
            "E.1PWWaQ",
            "Z9_ftZ-o"
    };

    public static SpicedPassword hashPassword(String password, String salt, String username) {
        if (salt == null) {
            salt = generateSalt();
        }

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            String spiced = spice(password, salt, username);
            byte[] hashBytes = md.digest(spiced.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return SpicedPassword.builder()
                    .passwordHash(stringBuilder.toString())
                    .salt(salt)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String spice(String password, String salt, String username) {
        StringBuilder stringBuilder = new StringBuilder();
        int pepperIndex = (int) Integer.toUnsignedLong(username.hashCode()) % pepper.length;

        stringBuilder.append(password);
        stringBuilder.append(salt);
        stringBuilder.append(pepper[pepperIndex]);

        return stringBuilder.toString();
    }

    private static String generateSalt() {
        Random rng = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomAscii = 32 + rng.nextInt(95);
            stringBuilder.append((char) randomAscii);
        }
        return stringBuilder.toString();
    }
}
