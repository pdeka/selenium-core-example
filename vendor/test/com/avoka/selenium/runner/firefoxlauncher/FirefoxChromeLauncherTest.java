package com.avoka.selenium.runner.firefoxlauncher;

import junit.framework.TestCase;

public class FirefoxChromeLauncherTest extends TestCase {

    public void testFirefoxChromeLaunch() {
        FirefoxChromeLauncher launcher = new FirefoxChromeLauncher("caesar1", "http://www.google.com", "C:/Mozilla Firefox/firefox.exe");
    }


}