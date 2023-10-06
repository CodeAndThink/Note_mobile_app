package com.example.noteapp.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.w3c.dom.Node;

import com.example.noteapp.async.DeleteAsyncTask;
import com.example.noteapp.async.InsertAsyncTask;
import com.example.noteapp.async.UpdateAsyncTask;
import com.example.noteapp.models.Note;

import java.util.List;

public class NoteRepository {
    private NoteDataBase mNoteDatabse;
    public NoteRepository(Context context) {
        mNoteDatabse = NoteDataBase.getInstance(context);
    }
    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabse.getNoteDao()).execute(note);
    }
    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabse.getNoteDao()).execute(note);
    }
    public LiveData<List<Note>> retrieveNotesTask(){
        return mNoteDatabse.getNoteDao().getNote();
    }
    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabse.getNoteDao()).execute(note);
    }
}
