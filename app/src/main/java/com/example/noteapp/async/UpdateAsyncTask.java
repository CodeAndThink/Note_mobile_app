package com.example.noteapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.noteapp.models.Note;
import com.example.noteapp.persistence.NoteDAO;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
    private NoteDAO mNoteDao;
    private static final String TAG = "UpdateAsyncTask";
    public UpdateAsyncTask(NoteDAO dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackgrond: thread: " + Thread.currentThread().getName());
        mNoteDao.update(notes);
        return null;
    }
}
