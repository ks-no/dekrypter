package no.ks.svarut.dekrypter;


import no.ks.kryptering.CMSKrypteringImpl;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

import java.io.*;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public class Dekrypter {
    private final CMSKrypteringImpl cmsDataKryptering;
    private Config config;
    private PrivateKey privatekey;

    public Dekrypter(Config config) {
        this.config = config;
        try {
            privatekey = getPrivateKey(config.getPrivateKey(), config.getPrivateKeyPassword());
            cmsDataKryptering = new CMSKrypteringImpl();
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

    public void dekryptFiles() {
        File[] files = readFiles(config.getSourceDir());
        if(files.length == 0) {
            System.out.println("No files to decrypt exiting");
            System.exit(0);
        }
        for (File file : files) {
            File target = new File(config.getTargetDir().getAbsolutePath() + File.separator + file.getName().replace(".crypt", ""));
            System.out.println("Decrypting file " + file.getName());
            dekrypt(file, target);
            if(config.isDelete() && target.exists()) {
                System.out.println("Deleting file " + file);
                file.delete();
            }
        }
    }

    private void dekrypt(File source, File target) {
        try(FileInputStream is = new FileInputStream(source);
            FileOutputStream os = new FileOutputStream(target);
            InputStream dekryptedStream = cmsDataKryptering.dekrypterData(is,privatekey)
        ){
            IOUtils.copy(dekryptedStream, os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File[] readFiles(File sourceDir) {
        if (sourceDir.isDirectory()) return sourceDir.listFiles((dir, name) -> {
            return name.endsWith(".crypt");
        });
        else
            return new File[]{sourceDir};
    }
}
