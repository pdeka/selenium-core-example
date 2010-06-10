package com.prabindeka.selenium.runner.util;

import java.io.*;

import org.apache.commons.httpclient.methods.GetMethod;

public class SeleniumPoller {

    private final int _maxPollAttempts;
    private final String _applicationURL;
    private PollResult _lastPollResult;
    private final File _outputFile;


    public SeleniumPoller(String applicationURL, int maxPollAttempts, File outputFile) {
        _applicationURL = applicationURL;
        _maxPollAttempts = maxPollAttempts;
        _outputFile = outputFile;
    }

    public boolean poll() {
        pollForResults();
        return _lastPollResult.isTestsPassed();
    }

    private void pollForResults() {
        (new UrlPoller(_applicationURL, _maxPollAttempts)).poll(new ResponseEvaluator() {

            public boolean evaluate(GetMethod get) {
                if (get.getStatusCode() != 200)
                    return false;
                try {
                    _lastPollResult = new PollResult(get.getResponseBodyAsString());
                }
                catch (IOException e) {
                    System.err.println(e);
                }
                return _lastPollResult.isTestResultPosted();
            }

            final SeleniumPoller this$0;
            {
                this$0 = SeleniumPoller.this;
//                super();
            }
        }
        );
    }

    private class PollResult {

        public PollResult(String result) {
            super();
            this$0 = SeleniumPoller.this;
            _result = result;
            if (isTestResultPosted())
                (new ResultWriter(_outputFile)).write(_result);
        }

        public boolean isTestResultPosted() {
            return _result.indexOf("<body>") != -1;
        }

        public boolean isTestsPassed() {
            return _result.indexOf("passed") != -1;
        }

        private final String _result;
        final SeleniumPoller this$0;

    }



}
