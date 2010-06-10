package com.prabindeka.selenium.runner;

public interface BrowserLauncher {
    public void launch(String url, boolean visible);

    public void close();

}
