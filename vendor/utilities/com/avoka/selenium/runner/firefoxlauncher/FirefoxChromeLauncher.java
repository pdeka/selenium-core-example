package com.avoka.selenium.runner.firefoxlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class FirefoxChromeLauncher {

    private String url;
    private File firefox;
    private Process process;
    private String[] cmdArray;
    private Profile profile;

    public FirefoxChromeLauncher(String profileName, String url, String firefoxExe) {
        assertNotBlank(profileName, "Profile name");
        assertNotBlank(url, "URL");
        this.url = url;
        this.firefox = locateFirefoxBinary(new File(firefoxExe));
        profile = new Profile(firefox);

        File profileDir = profile.locateWebDriverProfile(profileName);

        if (profileDir.exists()) {
            profile.cleanTempWebdriverProfileDir();
            startFirefox(profileName);
        } else {
            profile.createBaseWebDriverProfile(profileName);
            cmdArray = new String[]{firefox.getAbsolutePath(), "-P", profileName, this.url};
            startFirefox(profileName, new ProcessBuilder(firefox.getAbsolutePath(), "-P", profileName, this.url).redirectErrorStream(true));
        }
    }

    public boolean isProcessPresent() {
        return process != null;
    }

    private void assertNotBlank(String string, String label) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(label + " name cannot be blank");
        }
    }

    public String[] getCmdArray() {
        return cmdArray;
    }

    private void startFirefox(String profileName, ProcessBuilder builder) {
        try {
            builder.environment().put("MOZ_NO_REMOTE", "1");
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Cannot load firefox: " + profileName, e);
        }

    }

    public void startFirefox(String profileName) {
        File profileDir = profile.createCopyOfDefaultProfile(profileName);
        cmdArray = new String[]{firefox.getAbsolutePath(), "-profile", profileDir.getAbsolutePath(), url};

        ProcessBuilder builder = new ProcessBuilder(firefox.getAbsolutePath(), "-profile", profileDir.getAbsolutePath(), url).redirectErrorStream(true);
        startFirefox(profileName, builder);
    }


    private File locateFirefoxBinary(File suggestedLocation) {
        if (suggestedLocation != null) {
            if (suggestedLocation.exists() && suggestedLocation.isFile())
                return suggestedLocation;
            else
                throw new RuntimeException("Given firefox binary location does not exist or is not a real file: " + suggestedLocation);
        }

        File binary = locateFirefoxBinaryFromSystemProperty();
        if (binary != null)
            return binary;

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            String programFiles = System.getenv("PROGRAMFILES");
            if (programFiles == null)
                programFiles = "\\Program Files";
            binary = new File(
                    programFiles + "\\Mozilla Firefox\\firefox.exe");
        } else if (osName.startsWith("mac")) {
            binary = new File(
                    "/Applications/Firefox.app/Contents/MacOS/firefox");
        } else {
            binary = shellOutAndFindPathOfFirefox("firefox");
        }

        if (binary.exists())
            return binary;

        throw new RuntimeException("Unable to locate firefox binary. Please check that it is installed in the default location, " +
                "or the path given points to the firefox binary. I would have used: " + binary.getPath());
    }

    private File locateFirefoxBinaryFromSystemProperty() {
        String binaryName = System.getProperty("webdriver.firefox.bin");
        if (binaryName == null)
            return null;

        File binary = new File(binaryName);
        if (binary.exists())
            return binary;

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows"))
            return null;  // Who knows how to handle this

        if (osName.contains("mac")) {
            if (!binaryName.endsWith(".app"))
                binaryName += ".app";
            binaryName += "/Contents/MacOS/firefox";
            return new File(binaryName);
        }

        // Assume that we're on a UNIX variant
        return shellOutAndFindPathOfFirefox(binaryName);

    }

    private File shellOutAndFindPathOfFirefox(String binaryName) {
        // Assume that we're on a unix of some kind. We're going to cheat
        try {
            Process which = Runtime.getRuntime().exec(new String[]{"which", binaryName});
            String result = getNextLineOfOutputFrom(which);
            return new File(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Assumes that the process has exited
    private String getNextLineOfOutputFrom(Process process) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }


}
