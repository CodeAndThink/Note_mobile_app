package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.noteapp.adapters.NoteRecycleAdapter;
import com.example.noteapp.models.Note;
import com.example.noteapp.persistence.NoteRepository;
import com.example.noteapp.util.VerticalSpacingItemDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteRecycleAdapter.OnNoteListener, View.OnClickListener{
    //Ui components
    private static final String TAG = "NotesListActivity";
    private RecyclerView mRecycleView;
    //Vars
    private ArrayList<Note> mNote = new ArrayList<>();
    private NoteRecycleAdapter mNoteRecycleAdapter;
    private NoteRepository mNoteRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = findViewById(R.id.recycleView);
        findViewById(R.id.fab).setOnClickListener(this);
        initRecycleView();
        mNoteRepository = new NoteRepository(this);
        retrieveNotes();
        setSupportActionBar((Toolbar)findViewById(R.id.note_toolbar));
        setTitle("Notes");
        Log.d(TAG, "onCreate: thread: " + Thread.currentThread().getName());
        mNoteRepository.insertNoteTask(new Note());
    }
    private void retrieveNotes(){
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNote.size() > 0){
                    mNote.clear();
                }
                if(notes != null){
                    mNote.addAll(notes);
                }
                mNoteRecycleAdapter.notifyDataSetChanged();
            }
        });
    }
    private void initRecycleView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecycleView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(mRecycleView);
        mNoteRecycleAdapter = new NoteRecycleAdapter(mNote, this);
        mRecycleView.setAdapter(mNoteRecycleAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: Clicked" + position);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNote.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,NoteActivity.class);
        startActivity(intent);
    }
    private void deleteNote(Note note){
        mNote.remove(note);
        mNoteRecycleAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNote.get(viewHolder.getAdapterPosition()));
        }
    };
}