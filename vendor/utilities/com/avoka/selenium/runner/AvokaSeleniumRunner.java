package com.avoka.selenium.runner;

import com.avoka.selenium.runner.util.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class AvokaSeleniumRunner {

    public AvokaSeleniumRunner(SeleniumRunnerConfig initalConfig) {
        config = initalConfig;
    }

    public static void main(String argv[])
            throws Exception {
        Properties properties = System.getProperties();
        SeleniumRunnerConfig config = new SeleniumRunnerConfig();
        config.setResultsURL(properties.getProperty("test.selenium.results"));
        config.setTestRunnerURL(properties.getProperty("test.selenium.runner"));
        config.setBrowserVisible(visible(properties.getProperty("test.selenium.browser.visible")));
        String logDirectoryPath = properties.getProperty("test.selenium.log.dir");
        config.setOutputFile(createLogFile(logDirectoryPath));
        config.setMaxPollAttempts(Integer.parseInt(properties.getProperty("test.selenium.maximum.poll.attempts")));


        String browserType = properties.getProperty("test.selenium.browser");

        if ("firefox-chrome".equalsIgnoreCase(browserType)) {
            String firefoxExe = properties.getProperty("test.firefox.exe.location");
            config.setBrowserLauncher(new AvokaFirefoxChromeLauncher(firefoxExe));
        } else if ("ie".equalsIgnoreCase(browserType)) {
            config.setBrowserLauncher(new AvokaWindowsIEBrowserLauncher());
        } else {
            throw new RuntimeException("Not a valid browser. Please set test.selenium.browser= ie or firefox");
        }

        (new AvokaSeleniumRunner(config)).run();
    }

    public void run() throws IOException, InterruptedException {
        String resultsURL = config.getResultsURL();
        System.out.println("Results URL - " + resultsURL);
        boolean isBrowserVisible = config.isBrowserVisible();
        AvokaBrowserLauncher browserLauncher = config.getBrowserLauncher();
        (new SeleniumInitializer(resultsURL)).initialize();
        System.out.println("config.getTestRunnerURL() - " + config.getTestRunnerURL());
        System.out.println("isBrowserVisible - " + isBrowserVisible);
        browserLauncher.launch(config.getTestRunnerURL(), isBrowserVisible);
        boolean succeeded = (new SeleniumPoller(resultsURL, config.getMaxPollAttempts(), config.getOutputFile())).poll();

        browserLauncher.close();

        if (!succeeded)
            throw new RuntimeException("The Selenium tests have failed! Please have a look at selenium results to see failures.");
//        if (succeeded || !isBrowserVisible)
//            browserLauncher.close();
    }

    private static boolean visible(String visibleString) {
        boolean isPropertySet = visibleString != null;
        String normalizedVisibleString = isPropertySet ? visibleString.toLowerCase().trim() : "";
        return !"false".equals(normalizedVisibleString);
    }

    private static File createLogFile(String logDirectoryPath) {
        File logDirectory = new File(logDirectoryPath);
        if (!logDirectory.exists() || !logDirectory.isDirectory()) {
            throw new RuntimeException((new StringBuilder()).append("cannot write to log directory '").append(logDirectory).append("'").toString());
        } else {
            File outputFile = new File(logDirectoryPath, "selenium-results.html");
            return outputFile;
        }
    }

    private final SeleniumRunnerConfig config;
}
