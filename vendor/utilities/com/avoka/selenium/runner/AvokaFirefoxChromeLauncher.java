package com.avoka.selenium.runner;

import com.avoka.selenium.runner.firefoxlauncher.FirefoxChromeLauncher;
import org.openqa.selenium.server.browserlaunchers.WindowsUtils;

public class AvokaFirefoxChromeLauncher implements AvokaBrowserLauncher {
    private static final String PROFILE_NAME = "webdriver";

    private String firefoxExe;
    private boolean closed;
    private FirefoxChromeLauncher firefoxChromeLauncher;
    private String[] cmdArray;

    public AvokaFirefoxChromeLauncher(String firefoxExe) {
        this.firefoxExe = firefoxExe;
    }

    public void launch(String url, boolean visible) {
        firefoxChromeLauncher = new FirefoxChromeLauncher(PROFILE_NAME, url, firefoxExe);
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