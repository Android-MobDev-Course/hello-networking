package com.mobdev.hellonetworking.network;

import androidx.lifecycle.MutableLiveData;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Marco Picone picone.m@gmail.com on 03,May,2020
 * Mobile System Development - University Course
 */
public class HttpRequestManager {

    private static final String TARGET_HTTP_URL = "https://httpbin.org/get";

    public static final String HTTP_LIBRARY_NATIVE_JAVA = "JavaNative";
    public static final String HTTP_LIBRARY_OK_HTTP = "OkHttp";

    public static void scheduleHttpDummyRequest(String httpLibrary, MutableLiveData<String> httpResponseLiveData){
        if(httpLibrary.equals(HTTP_LIBRARY_NATIVE_JAVA))
            sendJavaNativeHttpRequest(httpResponseLiveData);
        else if(httpLibrary.equals(HTTP_LIBRARY_OK_HTTP))
            sendOkHttpHttpRequest(httpResponseLiveData);
    }

    private static void sendJavaNativeHttpRequest(final MutableLiveData<String> httpResponseLiveData){

        if(httpResponseLiveData != null){

            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        URL urlObj = new URL(TARGET_HTTP_URL);

                        HttpURLConnection connection = (HttpURLConnection)urlObj.openConnection();

                        connection.connect();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        String line = null;

                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line + "\n");
                        }

                        //Calling postValue since we are on a background thread
                        httpResponseLiveData.postValue(stringBuilder.toString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private static void sendOkHttpHttpRequest(final MutableLiveData<String> httpResponseLiveData){

        try {
            if(httpResponseLiveData != null){

                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url(TARGET_HTTP_URL)
                                    .build();

                            Response response = client.newCall(request).execute();
                            httpResponseLiveData.postValue(response.body().string());

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
