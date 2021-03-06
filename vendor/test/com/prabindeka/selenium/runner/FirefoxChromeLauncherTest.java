package com.prabindeka.selenium.runner;

import junit.framework.TestCase;

public class FirefoxChromeLauncherTest extends TestCase {

    public FirefoxChromeLauncherTest(String testCase) {
        super(testCase);
    }


    public void testLaunchesAndClosesProperly() throws InterruptedException {
        FirefoxChromeLauncher chromeLauncher = new FirefoxChromeLauncher("C:/Mozilla Firefox/firefox.exe");
        chromeLauncher.launch("http://localhost:8080/selenium/TestRunner.html?test=/selenium-tests/TestSuite.html", true);
        Thread.sleep(10000);
        chromeLauncher.close();
    }
}
