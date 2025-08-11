package tvz.ntpr.core.utils;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class DigitalSignature {
    private static final String KEYSTORE_PATH = "other/keystore.p12";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_PASSWORD = "keypass";
    private static final String KEY_ALIAS = "ntprkey";
    private static final String HASH_ALGORITHM = "SHA256withRSA";

    public static File createDetachedSignature(File input) throws Exception {
        Signature sig = Signature.getInstance(HASH_ALGORITHM);

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        char[] keyPassword = KEY_PASSWORD.toCharArray();
        try (InputStream keystoreStream = DigitalSignature.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH)) {
            keyStore.load(keystoreStream, keyPassword);
        }
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, keyPassword);

        sig.initSign(privateKey);

        try (InputStream in = new BufferedInputStream(new FileInputStream(input))) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                sig.update(buffer, 0, read);
            }
        }

        byte[] signatureBytes = sig.sign();

        File signatureFile = new File(input.getParentFile(), input.getName() + ".p7s");
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(signatureFile))) {
            out.write(signatureBytes);
        }

        return signatureFile;
    }
}
