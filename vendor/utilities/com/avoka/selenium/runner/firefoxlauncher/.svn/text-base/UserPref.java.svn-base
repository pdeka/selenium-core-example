package com.avoka.selenium.runner.firefoxlauncher;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class UserPref {

    public UserPref() {
    }

    public void updateUserPrefsFor(File extensionsDir) {
        File userPrefs = new File(extensionsDir, "prefs.js");

        Map<String, String> prefs = new HashMap<String, String>();
        if (userPrefs.exists()) {
            prefs = readExistingPrefs(userPrefs);
        }

        prefs.put("app.update.enabled", "false");
        prefs.put("browser.download.manager.showWhenStarting", "false");
        prefs.put("browser.link.open_external", "2");
        prefs.put("browser.link.open_newwindow", "2");
        prefs.put("browser.search.update", "false");
        prefs.put("browser.shell.checkDefaultBrowser", "false");
        prefs.put("browser.startup.page", "0");
        prefs.put("browser.tabs.warnOnOpen", "false");
        prefs.put("dom.disable_open_during_load", "false");
        prefs.put("extensions.update.enabled", "false");
        prefs.put("extensions.update.notifyUser", "false");
        prefs.put("security.warn_entering_secure", "false");;
        prefs.put("security.warn_submit_insecure", "false");;
        prefs.put("security.warn_entering_secure.show_once", "false");
        prefs.put("security.warn_entering_weak", "false");
        prefs.put("security.warn_entering_weak.show_once", "false");
        prefs.put("security.warn_leaving_secure", "false");
        prefs.put("security.warn_leaving_secure.show_once", "false");
        prefs.put("security.warn_submit_insecure", "false");
        prefs.put("security.warn_viewing_mixed", "false");
        prefs.put("security.warn_viewing_mixed.show_once", "false");
        prefs.put("signon.rememberSignons", "false");
        prefs.put("signed.applets.codebase_principal_support", "true");

        userPrefs.delete();

        writeNewPrefs(userPrefs, prefs);
    }

    private void writeNewPrefs(File userPrefs, Map<String, String> prefs) {
        Writer writer = null;
        try {
            writer = new FileWriter(userPrefs);
            for (Map.Entry<String, String> entry : prefs.entrySet()) {
                writer.append("user_pref(\"").append(entry.getKey()).append("\", ").append(entry.getValue()).append(");\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeCleanly(writer);
        }
    }

    // Assumes that we only really care about the preferences, not the comments


    private Map<String, String> readExistingPrefs(File userPrefs) {
        Map<String, String> prefs = new HashMap<String, String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(userPrefs));
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("user_pref(\"")) {
                    line = reader.readLine();
                    continue;
                }
                line = line.substring("user_pref(\"".length());
                line = line.substring(0, line.length() - ");".length());
                String[] parts = line.split(",");
                prefs.put(parts[0].trim(), parts[1].trim());

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeCleanly(reader);
        }

        return prefs;
    }

    private void closeCleanly(Writer writer) {
        IOUtils.closeQuietly(writer);
    }


    private void closeCleanly(Reader reader) {
        IOUtils.closeQuietly(reader);
    }

}
