package com.prabindeka.selenium.runner.util;

import com.prabindeka.selenium.runner.BrowserLauncher;

import java.io.File;

public class SeleniumRunnerConfig
{

    public SeleniumRunnerConfig()
    {
    }

    public BrowserLauncher getBrowserLauncher()
    {
        return browserLauncher;
    }

    public void setBrowserLauncher(BrowserLauncher browserLauncher)
    {
        this.browserLauncher = browserLauncher;
    }

    public boolean isBrowserVisible()
    {
        return isBrowserVisible;
    }

    public void setBrowserVisible(boolean isBrowserVisible)
    {
        this.isBrowserVisible = isBrowserVisible;
    }

    public int getMaxPollAttempts()
    {
        return maxPollAttempts;
    }

    public void setMaxPollAttempts(int maxPollAttempts)
    {
        this.maxPollAttempts = maxPollAttempts;
    }

    public File getOutputFile()
    {
        return outputFile;
    }

    public void setOutputFile(File outputFile)
    {
        this.outputFile = outputFile;
    }

    public String getResultsURL()
    {
        return resultsURL;
    }

    public void setResultsURL(String resultsURL)
    {
        this.resultsURL = resultsURL;
    }

    public String getTestRunnerURL()
    {
        return testRunnerURL;
    }

    public void setTestRunnerURL(String testRunnerURL)
    {
        this.testRunnerURL = testRunnerURL;
    }

    public String getJacobDirectory()
    {
        return jacobDirectory;
    }

    public void setJacobDirectory(String jacobDirectory)
    {
        this.jacobDirectory = jacobDirectory;
    }

    private String resultsURL, testRunnerURL, jacobDirectory;
    private boolean isBrowserVisible;
    private File outputFile;
    private BrowserLauncher browserLauncher;
    private int maxPollAttempts;
}
