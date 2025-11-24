package com.example.oauthsecurenoteapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class TokenManager {
    private final SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        sharedPreferences.edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .apply();
    }

    public String getAccessToken() {
        return sharedPreferences.getString("access_token", null);
    }

    public String getRefreshToken() {
        return sharedPreferences.getString("refresh_token", null);
    }

    public void saveUserId(String userId) {
        sharedPreferences.edit().putString("user_id", userId).apply();
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", null);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }


}
