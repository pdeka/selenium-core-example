package com.prabindeka.selenium.runner.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class SeleniumInitializer
{

    public SeleniumInitializer(String applicationURL)
    {
        this.applicationURL = applicationURL;
    }

    public void initialize()
        throws HttpException, IOException
    {
        resetResultsServlet();
    }

    private void resetResultsServlet()
        throws HttpException, IOException
    {
        HttpClient client = new HttpClient();
        System.out.println((new StringBuilder()).append("Clearing:").append(applicationURL).toString());
        GetMethod get = new GetMethod((new StringBuilder()).append(applicationURL).append("?clear").toString());
        client.executeMethod(get);
        if(get.getStatusCode() != 200 || !isSeleniumResultsCleared(get))
        {
            throw new RuntimeException("failed to reset postServlet");
        } else
        {
            System.out.println("selenium results cleared");
            return;
        }
    }

    private static boolean isSeleniumResultsCleared(GetMethod get)
        throws IOException
    {
        return get.getResponseBodyAsString().indexOf("selenium results cleared") != -1;
    }

    private final String applicationURL;
}
