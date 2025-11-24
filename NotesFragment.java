package com.example.oauthsecurenoteapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.oauthsecurenoteapp.databinding.FragmentNotesBinding;
import com.example.oauthsecurenoteapp.databinding.ItemNoteBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NotesFragment extends Fragment {

    private FragmentNotesBinding binding;
    private TokenManager tokenManager;
    private OkHttpClient client;


    public NotesFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        tokenManager = new TokenManager(requireContext());
        client = new OkHttpClient();
        loadNotes();


        binding.btnAddNote.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main, new AddNoteFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.btnLogout.setOnClickListener(v -> {
            tokenManager.clear();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main, new LoginFragment());
            transaction.commit();
        });

        return binding.getRoot();

    }
    private void loadNotes() {
        String accessToken = tokenManager.getAccessToken();
        Log.d("TOKEN_USED", accessToken);

        Request request = ApiService.getNotesRequest(accessToken);


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("RESPONSE_CODE", String.valueOf(response.code()));

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d("RESPONSE_BODY", json);

                    List<Note> notes = new Gson().fromJson(json, new TypeToken<List<Note>>(){}.getType());

                    Activity activity = getActivity();
                    if (activity != null && isAdded()) {
                        activity.runOnUiThread(() -> {
                            if (binding != null) {
                                NoteAdapter adapter = new NoteAdapter(notes);
                                binding.listNotes.setAdapter(adapter);
                            }
                        });


                    }
                       //     requireActivity().runOnUiThread(() -> {
                       // if (!isAdded() || binding == null) return;
                       // NoteAdapter adapter = new NoteAdapter(notes);
                       // binding.listNotes.setAdapter(adapter);
                  //  });
                } else if (response.code() == 401) {

                    if (!isAdded() || binding == null) return;
                    Activity activity = getActivity();
                    if (activity != null && isAdded()) {
                        activity.runOnUiThread(() -> {
                            if (binding != null) {

                                //requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
                                tokenManager.clear();
                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.main, new LoginFragment());
                                transaction.commit();
                            }
                        });
                    }
                } else {
                    showError("Failed to load notes: " + response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                showError("Error: " + e.getMessage());
            }

            private void showError(String msg) {
                Activity activity = getActivity();
                if (activity != null && isAdded()) {
                    activity.runOnUiThread(() ->
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }




    @Override
    public void onResume() {
        super.onResume();
        loadNotes(); // Refresh notes when returning from AddNoteFragment
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 🔹 Inner Adapter Class
    private class NoteAdapter extends BaseAdapter {
        private final List<Note> notes;
        private final LayoutInflater inflater;

        public NoteAdapter(List<Note> notes) {
            this.notes = notes;
            this.inflater = LayoutInflater.from(requireContext());
        }

        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public Object getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return notes.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemNoteBinding itemBinding;
            if (convertView == null) {
                itemBinding = ItemNoteBinding.inflate(inflater, parent, false);
                convertView = itemBinding.getRoot();
                convertView.setTag(itemBinding);
            } else {
                itemBinding = (ItemNoteBinding) convertView.getTag();
            }

            Note note = notes.get(position);
            itemBinding.textTitle.setText(note.title);
            itemBinding.textContent.setText(note.content);

            return convertView;
        }
    }



}