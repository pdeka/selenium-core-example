package com.avoka.selenium.runner;

import junit.framework.TestCase;

public class AvokaFirefoxChromeLauncherTest extends TestCase {

    public AvokaFirefoxChromeLauncherTest(String testCase) {
        super(testCase);
    }


    public void testLaunchesAndClosesProperly() throws InterruptedException {
        AvokaFirefoxChromeLauncher chromeLauncher = new AvokaFirefoxChromeLauncher("C:/Mozilla Firefox/firefox.exe");
        chromeLauncher.launch("http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html", true);
        Thread.sleep(10000);
        chromeLauncher.close();
    }
}
