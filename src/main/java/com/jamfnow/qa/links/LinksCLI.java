package com.jamfnow.qa.links;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class LinksCLI {

    public static void main(String[] args) {

        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        CommandLine cl = null;

        try {
            cl = parser.parse(options, args);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        if (cl != null && cl.hasOption("help")) {
            printHelp(options);
            System.exit(0);
        }
        else if (cl != null && cl.hasOption("version")) {
            printVersion();
            System.exit(0);
        }
        else if (cl != null && cl.hasOption("readme")) {
            printReadme();
            System.exit(0);
        }

        LinksConfig linksConfig = parseConfig(cl);
        LinksStats linksStats = new LinksStats();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println(linksStats)));

        LinksRunner linksRunner = new LinksRunner(linksConfig, linksStats);

        linksRunner.run();
    }

    private static LinksConfig parseConfig(final CommandLine cl) {
        if (cl == null) {
            return null;
        }

        if (cl.hasOption("f")) {
            File f = new File(cl.getOptionValue("cnf"));
            if (f.exists() && f.canRead()) {
                try {
                    return new ObjectMapper(new YAMLFactory()).readValue(new FileReader(f), LinksConfig.class);
                }
                catch (Exception e) {
                    System.err.println("Unable to create Links. Reason: " + e.getMessage());
                }
            }
        }
        else {
            LinksConfig linksConfig = new LinksConfig();
            linksConfig.setBrowser(Browser.valueOf(cl.getOptionValue("f")));
            //TODO: add rest of the options
        }
        return null;
    }

    public static Options createOptions() {
        Options options = new Options();

        options.addOption(Option.builder("f").longOpt("file").desc("Config file location. Using this one will override all other params.").build());

        options.addOption(Option.builder("b").longOpt("browser").desc("Browser to use for test, available: [Chrome, Firefox, PhantomJS].").build());
        options.addOption(Option.builder("t").longOpt("page-load-timeout").desc("How long to wait until determining that page did NOT load.").build());
        options.addOption(Option.builder("u").longOpt("url").desc("Main page url.").build());
        options.addOption(Option.builder("lu").longOpt("login-user").desc("Login page user.").build());
        options.addOption(Option.builder("lus").longOpt("login-user-selector").desc("Login page user input selector.").build());
        options.addOption(Option.builder("lp").longOpt("login-password").desc("Login page password.").build());
        options.addOption(Option.builder("lps").longOpt("login-password-selector").desc("Login page password input selector.").build());

        options.addOption(Option.builder("h").longOpt("help").desc("Print help.").build());
        options.addOption(Option.builder("v").longOpt("version").desc("Print version.").build());
        options.addOption(Option.builder("rdm").longOpt("readme").desc("Print a markdown readme.").build());

        return options;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("links", options);
    }

    private static void printVersion() {
        System.out.println(CLIProperties.getVersion());
    }

    private static void printReadme() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("README.md");
        try {
            System.out.println(IOUtils.toString(is, Charset.defaultCharset()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
