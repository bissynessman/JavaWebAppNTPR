package tvz.ntpr.core.utils;

import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import tvz.ntpr.core.rest.TimeApi;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.Security;
import java.time.ZoneOffset;

public class DigitalSignature {
    private static final String KEYSTORE_PATH = "other/ntpr_keystore.p12";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_PASSWORD = "keypass";
    private static final String KEY_ALIAS = "certkey";
    private static final String HASH_ALGORITHM = "SHA256";
    private static final String PROVIDER = "BC";

    private static final TimeApi timeApi = new TimeApi();

    public static File sign(File input) {
        File output = new File(input
                .getPath()
                .replaceFirst(
                        "\\.tmp",
                        "(" + timeApi.getCurrentTime().toEpochSecond(ZoneOffset.UTC) + ")"));

        try {
            Security.addProvider(new BouncyCastleProvider());

            InputStream keystoreStream = DigitalSignature.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            char[] keyPassword = KEY_PASSWORD.toCharArray();

            keyStore.load(keystoreStream, keyPassword);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, keyPassword);
            Certificate[] certChain = keyStore.getCertificateChain(KEY_ALIAS);

            PdfReader reader = new PdfReader(input);
            PdfWriter writer = new PdfWriter(output);

            StampingProperties stampingProperties = new StampingProperties().useAppendMode();

            PdfSigner signer = new PdfSigner(reader, writer, stampingProperties);

            SignerProperties signerProperties = new SignerProperties()
                    .setFieldName("signature")
                    .setReason("NTPR Project")
                    .setSignatureCreator("(0246108773)Tim Pavić")
                    .setLocation("Zagreb, HR")
                    .setContact("tpavic2@tvz.hr");
            signer.setSignerProperties(signerProperties);

            IExternalSignature signature = new PrivateKeySignature(privateKey, HASH_ALGORITHM, PROVIDER);

            signer.signDetached(new BouncyCastleDigest(), signature, certChain, null, null, null, 0, null);
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }
}
