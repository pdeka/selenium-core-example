package com.prabindeka.selenium.runner.util;


import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class ResultWriter
{

    public ResultWriter(File resultFile)
    {
        this.resultFile = resultFile;
    }

    public void write(String results)
    {
        if(results == null)
            results = "results never provided by postServlet";
        try
        {
            FileUtils.writeStringToFile(resultFile, results, System.getProperty("file.encoding"));
        }
        catch(IOException e)
        {
            throw new RuntimeException((new StringBuilder()).append("could not write results to ").append(resultFile).toString());
        }
    }

    private final File resultFile;
}
