package com.prabindeka.selenium.runner;

import org.openqa.selenium.server.browserlaunchers.WindowsUtils;

public class FirefoxChromeLauncher implements BrowserLauncher {
    private static final String PROFILE_NAME = "webdriver";

    private String firefoxExe;
    private boolean closed;
    private com.prabindeka.selenium.runner.firefoxlauncher.FirefoxChromeLauncher firefoxChromeLauncher;
    private String[] cmdArray;

    public FirefoxChromeLauncher(String firefoxExe) {
        this.firefoxExe = firefoxExe;
    }

    public void launch(String url, boolean visible) {
        firefoxChromeLauncher = new com.prabindeka.selenium.runner.firefoxlauncher.FirefoxChromeLauncher(PROFILE_NAME, url, firefoxExe);
        cmdArray = firefoxChromeLauncher.getCmdArray();
        closed = false;
    }


    public void close() {
        if (closed) return;
        if (!firefoxChromeLauncher.isProcessPresent()) return;
        // try to kill with windows taskkill
        try {
            WindowsUtils.kill(cmdArray);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        closed = true;
    }


}