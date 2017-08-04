package no.ks.svarut.dekrypter;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandLineParserTest {

    @Test
    public void testParseKey() throws Exception {
        CommandLineParser commandLineParser = new CommandLineParser(new String[]{"-k", "key"});
        CommandLine parse = commandLineParser.parse();
        assertEquals("key", parse.getOptionValue("k"));
    }

    @Test
    public void testDelete() throws Exception {
        CommandLineParser commandLineParser = new CommandLineParser(new String[]{"-k", "key", "-d"});
        CommandLine parse = commandLineParser.parse();
        assertTrue(parse.hasOption("d"));

    }
}
