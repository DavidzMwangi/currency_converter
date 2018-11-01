package com.company.davidwmwangi.currencyconverter.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by David W. Mwangi on 31/10/2017.
 */

public class InternetController {
    private static final String BASE_URL="https://min-api.cryptocompare.com/";
    private static AsyncHttpClient client=new AsyncHttpClient();

    public InternetController(){
        //no headers as this is a simple get request

    }
    public  void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public  void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private  String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
