package com.example.oauthsecurenoteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oauthsecurenoteapp.databinding.FragmentLoginBinding;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private OkHttpClient client;

    private TokenManager tokenManager;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        client = new OkHttpClient();
        tokenManager = new TokenManager(requireContext());

        binding.btnLogin.setOnClickListener(v -> Login());
        return binding.getRoot();

    }

    private void Login() {

            String clientId = binding.editUsername.getText().toString().trim();
            String clientSecret = binding.editPassword.getText().toString().trim();

            if (clientId.isEmpty() || clientSecret.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Call call = ApiClient.getClient().newCall(ApiService.getTokenRequest(clientId, clientSecret));

            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String json = response.body().string();
                        TokenResponse token = new Gson().fromJson(json, TokenResponse.class);

                        tokenManager.saveTokens(token.access_token, token.refresh_token);
                        Log.d("TOKEN_SAVED", "Access: " + token.access_token);

                        tokenManager.saveUserId(clientId);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            navigateToNotesFragment();
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });
        }



        private void navigateToNotesFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, new NotesFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}





