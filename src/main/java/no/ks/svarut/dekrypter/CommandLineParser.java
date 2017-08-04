package no.ks.svarut.dekrypter;

import org.apache.commons.cli.*;

public class CommandLineParser {

    private String[] args;

    public CommandLineParser(String[] args) {
        this.args = args;
    }

    public CommandLine parse() {

        Options options = definerKommandolinjeArgumenter();
        CommandLine commandLine = hentKommandolinjeArgumenter(options);

        if(commandLine.hasOption("?")){
            printHelp(options);
            System.exit(0);
        }
        if(!commandLine.hasOption("key")){
            System.out.println("Mangler private key fil");
            printHelp(options);
            System.exit(-1);
        }
        return commandLine;
    }

    public void printHelp(Options options) {

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar dekrypter.jar [OPTIONS]", options);
        System.exit(0);
    }

    private CommandLine hentKommandolinjeArgumenter(Options options) {
        org.apache.commons.cli.CommandLineParser cmdParser = new GnuParser();
        CommandLine commandLine = null;

        try {
            commandLine = cmdParser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Kunne ikke lese kommandolinjeargumenter.");
            System.out.println(e.getMessage());
            printHelp(options);
            System.exit(-1);
        }

        return commandLine;
    }

    private Options definerKommandolinjeArgumenter() {
        Options options = new Options();

        options.addOption(OptionBuilder
                .withDescription("Printer oversikt over mulige kommandolinjeargumenter.")
                .withLongOpt("help")
                .create("?"));

        options.addOption(OptionBuilder.withArgName("source")
                .hasArg()
                .withDescription("source fil eller mappe")
                .withLongOpt("source")
                .create("s"));
        options.addOption(OptionBuilder.withArgName("target")
                .hasArg()
                .withDescription("mappe som ukrypterte filer lagres i")
                .withLongOpt("target")
                .create("t"));

        options.addOption(OptionBuilder.withArgName("key")
                .hasArg()
                .withDescription("Private key for dekryptering av filene. Offentlig nøkkel som tilhører må være lagt inn i SvarUt.")
                .withLongOpt("key")
                .create("k"));
        options.addOption(OptionBuilder.withArgName("keypassword")
                .hasArg()
                .withDescription("Private key password.")
                .withLongOpt("keypassword")
                .create("kp"));
        options.addOption(OptionBuilder
                .withDescription("Delete source files after decrypt.")
                .withLongOpt("delete")
                .create("d"));
        return options;
    }
}
