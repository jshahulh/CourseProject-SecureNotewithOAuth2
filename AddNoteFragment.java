package com.example.oauthsecurenoteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oauthsecurenoteapp.databinding.FragmentAddNoteBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AddNoteFragment extends Fragment {
    private FragmentAddNoteBinding binding;
    private TokenManager tokenManager;
    private OkHttpClient client;


    public AddNoteFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        tokenManager = new TokenManager(requireContext());
        client = new OkHttpClient();

        binding.btnSaveNote.setOnClickListener(v -> saveNote());
        return binding.getRoot();

    }
    private void saveNote() {
        String title = binding.editTitle.getText().toString().trim();
        String content = binding.editContent.getText().toString().trim();
        String accessToken = tokenManager.getAccessToken();


        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = ApiService.createNoteRequest(accessToken, title, content);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
                        navigateBackToNotes();
                    } else {
                        Toast.makeText(getContext(), "Failed to save note", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void navigateBackToNotes() {
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