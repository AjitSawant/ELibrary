package com.palash.sampleapp.api;

import android.content.Context;
import android.util.Log;

import com.palash.sampleapp.database.DatabaseAdapter;
import com.palash.sampleapp.database.DatabaseContract;
import com.palash.sampleapp.entiry.ELLogin;
import com.palash.sampleapp.utilities.Constants;
import com.palash.sampleapp.utilities.LocalSetting;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class WebServiceConsumer {
    private OkHttpClient client;
    private String username;
    private String password;
    private DatabaseAdapter.LoginAdapter loginAdapter;
    private ArrayList<ELLogin> listLogin;

    public WebServiceConsumer(Context context, String username, String password) {
        client = new OkHttpClient();
        loginAdapter = new DatabaseAdapter(new DatabaseContract(context)).new LoginAdapter();
        if (username != null && password != null) {
            this.username = username;
            this.password = password;
        } else {
            listLogin = loginAdapter.listAll();
            this.username = listLogin.get(0).getLoginName();
            this.password = listLogin.get(0).getPassword();
        }
        this.username = LocalSetting.encodeString(this.username);
        this.password = LocalSetting.encodeString(this.password);
    }

    public Response POST(String url, String jSon) {
        Response response = null;
        try {
            RequestBody formBody = new FormEncodingBuilder().add(
                    Constants.KEY_REQUEST_DATA, jSon).build();
            Request request = new Request.Builder().url(url).post(formBody)
                    .addHeader("Username", username)
                    .addHeader("Password", password).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + username);
            Log.d(Constants.TAG, "Password:" + password);
            Log.d(Constants.TAG, "Json:" + jSon);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response GET(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().noCache().build())
                    .url(url).get().addHeader("Username", username)
                    .addHeader("Password", password).build();
            System.setProperty("http.keepAlive", "false");
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + username);
            Log.d(Constants.TAG, "Password:" + password);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response PUT(String url, String jSon) {
        Response response = null;
        try {
            RequestBody formBody = new FormEncodingBuilder().add(
                    Constants.KEY_REQUEST_DATA, jSon).build();
            Request request = new Request.Builder().url(url).put(formBody)
                    .addHeader("Username", username)
                    .addHeader("Password", password).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + username);
            Log.d(Constants.TAG, "Password:" + password);
            Log.d(Constants.TAG, "Json:" + jSon);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response DELETE(String url) {
        Response response = null;
        try {
            Request request = new Request.Builder().url(url).delete()
                    .addHeader("Username", username)
                    .addHeader("Password", password).build();
            Log.d(Constants.TAG, "Url:" + url);
            Log.d(Constants.TAG, "Username:" + username);
            Log.d(Constants.TAG, "Password:" + password);
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
