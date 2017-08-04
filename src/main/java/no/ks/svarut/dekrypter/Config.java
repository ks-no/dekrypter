package no.ks.svarut.dekrypter;

import org.apache.commons.cli.CommandLine;

import java.io.File;

public class Config {
    private File sourceDir;
    private File targetDir;
    private File keyFile;
    private boolean delete;
    private String privateKeyPassword;

    public Config(CommandLine line) {
        setKeyFile(line.getOptionValue("k"));
        setSourceDir(line.getOptionValue("s"));
        setTargetDir(line.getOptionValue("t"));
        setDelete(line.hasOption("d"));
        privateKeyPassword = line.getOptionValue("keypassword");
    }

    public void setSourceDir(String sourceDir) {
        if(sourceDir != null && new File(sourceDir).exists())
            this.sourceDir = new File(sourceDir);
        else
            this.sourceDir = new File("");
    }

    public void setTargetDir(String targetDir) {
        if(targetDir != null && new File(targetDir).exists())
            this.targetDir = new File(targetDir);
        else
            this.targetDir = new File("");
    }

    public void setKeyFile(String keyFile) {
        if(keyFile != null && new File(keyFile).canRead())
            this.keyFile = new File(keyFile);
        else
            throw new RuntimeException("Can't read key file " + keyFile);
    }

    public String toString() {
        return "Running with key " + keyFile.getAbsolutePath() + " reading from " + sourceDir.getAbsolutePath() + " writing to " + targetDir.getAbsolutePath();
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public File getTargetDir() {
        return targetDir;
    }

    public File getPrivateKey() {
        return keyFile;
    }

    public String getPrivateKeyPassword() {
        return privateKeyPassword;
    }
}
