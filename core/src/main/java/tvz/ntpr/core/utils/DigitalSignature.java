package tvz.ntpr.core.utils;

import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.Security;

public class DigitalSignature {
    private static final String KEYSTORE_PATH = "other/ntpr_keystore.p12";

    public static void sign(String input, String output) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            char[] keyPassword = "keypass".toCharArray();

            InputStream keystoreStream = DigitalSignature.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(keystoreStream, keyPassword);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("certkey", keyPassword);
            Certificate[] certChain = keyStore.getCertificateChain("certkey");

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

            IExternalSignature signature = new PrivateKeySignature(privateKey, "SHA-256", "BC");

            signer.signDetached(new BouncyCastleDigest(), signature, certChain, null, null, null, 0, null);
            reader.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
