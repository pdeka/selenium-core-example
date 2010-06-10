package com.prabindeka.selenium.runner.util;

import org.apache.commons.httpclient.methods.GetMethod;

public interface ResponseEvaluator
{

    public abstract boolean evaluate(GetMethod getmethod);

    public static final ResponseEvaluator SUCCESSFUL_CONNECT = new ResponseEvaluator() {

        public boolean evaluate(GetMethod get)
        {
            return get.getStatusCode() == 200;
        }

    }
;

}

