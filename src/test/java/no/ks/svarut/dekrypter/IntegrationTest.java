package no.ks.svarut.dekrypter;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    @Rule
    public TemporaryFolder target= new TemporaryFolder();

    @After
    public void tearDown() throws Exception {
        target.delete();
    }

    @Test
    public void testDekrypt() throws Exception {
        String source = "src" + File.separator + "test" + File.separator + "resources";
        if(!new File(source).exists()) source ="dekrypter" + File.separator + "src" + File.separator + "test" + File.separator + "resources";
        Main.main(new String[]{"-k",source + File.separator + "privatekey.pem", "-t", target.getRoot().getAbsolutePath(), "-s", source});
        assertTrue(new File(target.getRoot().getAbsolutePath() + File.separator + "kryptert.pdf").exists());
    }
}
