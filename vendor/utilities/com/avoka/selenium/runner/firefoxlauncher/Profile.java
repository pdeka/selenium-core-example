package com.avoka.selenium.runner.firefoxlauncher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.text.MessageFormat;

public class Profile {

    private final static String TMP_DIR = System.getProperty("java.io.tmpdir");
    private UserPref userPref;
    private File firefox;

    public Profile(File firefox) {
        assertNotNull(firefox);
        this.firefox = firefox;
        userPref = new UserPref();
    }

    private void assertNotNull(File firefox) {
        if(firefox == null) {
            throw new IllegalArgumentException("Firefox exe cannot be null");
        }
    }

    public void deleteWebDriverProfile(String profileName) {
        try {
            File profileDir = locateWebDriverProfile(profileName);
            if (profileDir.exists()) {
                FileUtils.deleteDirectory(profileDir);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public void createBaseWebDriverProfile(String profileName) {

        System.out.println(MessageFormat.format("Creating {0}", profileName));
        Process process;
        try {
            process = new ProcessBuilder(firefox.getAbsolutePath(), "-CreateProfile", profileName).redirectErrorStream(true).start();

            // Wait for a second for the process to actually finish
            wait(1);

            System.out.println(getNextLineOfOutputFrom(process));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File profileDir = locateWebDriverProfile(profileName);

        //System.out.println("Attempting to install the WebDriver extension");
        //installExtensionInto(extensionsDir);

        System.out.println("Updating user preferences with common, useful settings");
        userPref.updateUserPrefsFor(profileDir);

        System.out.println("Deleting existing extensions cache (if it already exists)");
        deleteExtensionsCacheIfItExists(profileDir);

        System.out.println(MessageFormat.format("Firefox should now start.\n\n" +
                "Please go to the \"Tools->Add-ons\" menu and confirm that an add-on called \"Firefox WebDriver\" has been " +
                "successfully installed.\n\nIf this is not present, please quit all open instances of Firefox.\n",
                firefox.getAbsolutePath(), profileName));

    }

    public File createCopyOfDefaultProfile(String profileName) {
        // Find the "normal" WebDriver profile, and make a copy of it
        File from = locateWebDriverProfile(profileName);
        if (!from.exists())
            throw new RuntimeException(MessageFormat.format("Found the {0} profile directory, but it does not exist: {1}",
                    profileName, from.getAbsolutePath()));

        File to = getTempWebdriverProfileDir();
        to.mkdirs();

        copy(from, to);

        return to;
    }

    public void cleanTempWebdriverProfileDir() {
        try {
            File profileDir = getTempWebdriverProfileDir();
            if (profileDir.exists()) {
                FileUtils.deleteDirectory(profileDir);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Cannot clean the profiles from the temp dir probably because there is a lock on file because another browser is running with the same profile", ioe);
        }
    }

    private File getTempWebdriverProfileDir() {
        //File to = new File(tmpDir, "webdriver-" + System.currentTimeMillis());
        File to = new File(TMP_DIR, "webdriver-profile");
        return to;
    }

    private void copy(File from, File to) {
        String[] contents = from.list();
        for (String child : contents) {
            File toCopy = new File(from, child);
            File target = new File(to, child);

            if (toCopy.isDirectory()) {
                target.mkdir();
                copy(toCopy, target);
            } else if (!".parentlock".equals(child)) {
                copyFile(toCopy, target);
            }
        }
    }

    private void copyFile(File from, File to) {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(to));
            in = new BufferedInputStream(new FileInputStream(from));

            int read = in.read();
            while (read != -1) {
                out.write(read);
                read = in.read();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public File locateWebDriverProfile(String profileName) {
        String profileNameLine = "Name=" + profileName;
        String osName = System.getProperty("os.name").toLowerCase();
        File appData = locateUserDataDirectory(osName);

        File profilesIni = new File(appData, "profiles.ini");
        if (!profilesIni.exists()) {
            throw new RuntimeException("Unable to locate the profiles.ini file, which contains information about where to locate the profiles");
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(profilesIni));
            boolean isRelative = true;
            String line = reader.readLine();
            boolean inProfile = false;
            while (line != null) {
                if (inProfile && line.startsWith("IsRelative="))
                    isRelative = line.endsWith("1");
                if (inProfile && line.startsWith("Name")) {
                    // We've left the webdriver profile and should have returned. Run away! Run away!
                    throw new RuntimeException("Found the " + profileName + " profile declaration, but cannot locate the path. Exiting");
                }
                if (inProfile && line.startsWith("Path=")) {
                    String path = line.substring("Path=".length());
                    if (isRelative)
                        return new File(appData, path);
                    return new File(path);
                }
                if (profileNameLine.equals(line.trim()))
                    inProfile = true;

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                // Nothing that can be done sensibly. Swallowing.
            }
        }

        return new File("non existent file");
//        throw new RuntimeException("Unable to locate the " + profileName + " profile. Exiting");
    }

    private File locateUserDataDirectory(String osName) {
        File appData;
        if (osName.contains("windows")) {
            appData = new File(MessageFormat.format("{0}\\Mozilla\\Firefox", System.getenv("APPDATA")));
        } else if (osName.contains("mac")) {
            appData = new File(MessageFormat.format("{0}/Library/Application Support/Firefox", System.getenv("HOME")));
        } else {
            appData = new File(MessageFormat.format("{0}/.mozilla/firefox", System.getenv("HOME")));
        }

        if (!appData.exists()) {
            throw new RuntimeException("Unable to locate directory which should contain the information about Firefox profiles.\n" +
                    "Tried looking in: " + appData.getAbsolutePath());
        }

        if (!appData.isDirectory()) {
            throw new RuntimeException("The discovered user firefox data directory " +
                    "(which normally contains the profiles) isn't a directory: " + appData.getAbsolutePath());
        }

        return appData;
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

    private void wait(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // Nothing to do. Swallow it
        }
    }

    private void deleteExtensionsCacheIfItExists(File extensionsDir) {
        File cacheFile = new File(extensionsDir, "extensions.cache");
        if (cacheFile.exists())
            cacheFile.delete();
    }

    

}