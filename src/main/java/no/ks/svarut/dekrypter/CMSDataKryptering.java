package no.ks.svarut.dekrypter;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.cms.CMSEnvelopedDataParser;
import org.bouncycastle.cms.CMSTypedStream;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class CMSDataKryptering {



    private final PrivateKey privateKey;

    public CMSDataKryptering(File privateKeyFile, String password) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        this.privateKey = getPrivateKey(privateKeyFile, password);
    }

    public InputStream dekrypterData(InputStream encryptedStream) {
        try {
            // Initialise parser
            CMSEnvelopedDataParser envDataParser = new CMSEnvelopedDataParser(encryptedStream);
            RecipientInformationStore recipients = envDataParser.getRecipientInfos();

            RecipientInformation recipient = (RecipientInformation) recipients.getRecipients().iterator().next();

            CMSTypedStream envelopedData = recipient.getContentStream(new JceKeyTransEnvelopedRecipient(privateKey));
            return envelopedData.getContentStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PrivateKey getPrivateKey(File privateKeyFile, String password) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println();
        System.out.println("reading key " + privateKeyFile.getAbsolutePath());
        if(password == null) password = "";
        PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile));
        Object object = pemParser.readObject();
        PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair kp;
        if (object instanceof PEMEncryptedKeyPair) {
            System.out.println("Encrypted key - we will use provided password");
            kp = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
            return kp.getPrivate();
        }else if (object instanceof PrivateKeyInfo){
            PrivateKeyInfo key = (PrivateKeyInfo) object;
            return converter.getPrivateKey(key);
        } else {
            System.out.println("Unencrypted key - no password needed");
            kp = converter.getKeyPair((PEMKeyPair) object);
            return kp.getPrivate();
        }
    }

}
