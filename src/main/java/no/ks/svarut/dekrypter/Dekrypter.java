package no.ks.svarut.dekrypter;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Dekrypter {
    private final CMSDataKryptering cmsDataKryptering;
    private Config config;

    public Dekrypter(Config config) {
        this.config = config;
        try {
            cmsDataKryptering = new CMSDataKryptering(config.getPrivateKey(), config.getPrivateKeyPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            InputStream dekryptedStream = cmsDataKryptering.dekrypterData(is);
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
