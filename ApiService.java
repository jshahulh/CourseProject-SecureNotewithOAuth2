package com.example.oauthsecurenoteapp;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiService {
    private static final String BASE_URL = "https://b7e560ca-21d4-42c5-a603-50552c15ee5e.mock.pstmn.io";

    public static Request getTokenRequest(String clientId, String clientSecret) {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .build();

        return new Request.Builder()
                .url(BASE_URL + "/oauth/token")
                .post(body)
                .build();
    }

    public static Request getNotesRequest(String accessToken) {
        Log.d("REQUEST_HEADER", "Bearer " + accessToken);
        return new Request.Builder()
                .url(BASE_URL + "/notes")
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();
    }

    public static Request createNoteRequest(String accessToken, String title, String content) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"title\": \"" + title + "\", \"content\": \"" + content + "\" }";
        RequestBody body = RequestBody.create(json, JSON);
        // RequestBody body = new FormBody.Builder()
        // .add("title", title)
                //.add("content", content)
                //.build();

        return new Request.Builder()
                .url(BASE_URL + "/notes")
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();
    }




}
