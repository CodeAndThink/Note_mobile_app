package com.example.noteapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.noteapp.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    long[] insertNotes(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNote();
    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Note> getNoteWithCustomQuery(String title);

    @Delete
    int delete(Note... notes);

    @Update
    int update(Note... notes);
}
