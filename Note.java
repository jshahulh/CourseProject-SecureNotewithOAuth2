package com.example.oauthsecurenoteapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String content;
    public String userId;

    public Note(String title, String content, String userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }



}
