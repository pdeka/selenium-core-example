package com.avoka.selenium.runner.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

// Referenced classes of package com.thoughtworks.selenium.runner:
//            ResponseEvaluator

public class UrlPoller
{

    public UrlPoller(String applicationURL, int pollingLimit)
    {
        this.applicationURL = applicationURL;
        this.pollingLimit = pollingLimit;
    }

    public boolean poll(ResponseEvaluator evaluator)
    {
        for(int count = 0; count < pollingLimit; count++)
        {
            System.out.println((new StringBuilder()).append("Attempting to connect to ").append(applicationURL).append(". Try ").append(count).append(" ...").toString());
            boolean succeeded = evaluateConnectionAttempt(evaluator);
            if(succeeded)
                return true;
            sleepOneSecond();
        }

        return false;
    }

    private boolean evaluateConnectionAttempt(ResponseEvaluator evaluator)
    {
        try
        {
            GetMethod get = new GetMethod(applicationURL);
            (new HttpClient()).executeMethod(get);
            return evaluator.evaluate(get);
        }
        catch(HttpException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    private void sleepOneSecond()
    {
        try
        {
            Thread.sleep(1000L);
        }
        catch(InterruptedException e1)
        {
            e1.printStackTrace();
        }
    }

    private final String applicationURL;
    private int pollingLimit;
}
