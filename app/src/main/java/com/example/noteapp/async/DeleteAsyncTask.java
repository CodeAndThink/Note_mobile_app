package com.example.noteapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.noteapp.models.Note;
import com.example.noteapp.persistence.NoteDAO;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
    private NoteDAO mNoteDao;
    private static final String TAG = "DeleteAsyncTask";
    public DeleteAsyncTask(NoteDAO dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackgrond: thread: " + Thread.currentThread().getName());
        mNoteDao.delete(notes);
        return null;
    }
}
