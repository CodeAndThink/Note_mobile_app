package com.example.noteapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.noteapp.models.Note;

@Database(entities = {Note.class}, version = 2)
public abstract class NoteDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "nodes_db";
    private static NoteDataBase instance;
    static NoteDataBase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NoteDataBase.class,DATABASE_NAME).build();
        }
        return instance;
    }
    public abstract NoteDAO getNoteDao();
}
