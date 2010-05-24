package com.avoka.selenium.runner;

public interface AvokaBrowserLauncher {
    public void launch(String url, boolean visible);

    public void close();

}
