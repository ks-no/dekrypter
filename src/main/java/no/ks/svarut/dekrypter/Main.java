package no.ks.svarut.dekrypter;

import org.apache.commons.cli.CommandLine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {

    public static void main(String... args) {
        Security.addProvider(new BouncyCastleProvider());
        CommandLineParser commandLineParser = new CommandLineParser(args);
        CommandLine parse = commandLineParser.parse();
        Config config = new Config(parse);
        System.out.println(config.toString());

        Dekrypter dekrypter = new Dekrypter(config);
        dekrypter.dekryptFiles();
    }
}
