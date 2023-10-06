package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.noteapp.models.Note;
import com.example.noteapp.persistence.NoteRepository;
import com.example.noteapp.util.Utility;

import org.w3c.dom.Node;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {
    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;
    //UI Component
    private LineEditText mLineEditText;
    private EditText mEditText;
    private TextView mTextView;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;
    //Vars
    private boolean mIsNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mFinalNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLineEditText = findViewById(R.id.note_text);
        mEditText = findViewById(R.id.note_edit_title);
        mTextView = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        mNoteRepository = new NoteRepository(this);
        setListener();
        if(getIncomingIntent()){
            //this is a new note (edit mode)
            setNewNoteProperties();
            enableEditMode();
        }else{
            //this is a old note (view mode)
            setNoteProperties();
            disableContentInteraction();
        }
    }
    private void setListener(){
        mGestureDetector = new GestureDetector(this,this);
        mLineEditText.setOnTouchListener(this);
        mTextView.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
    }
    private boolean getIncomingIntent() {
        if(getIntent().hasExtra("selected_note")){
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            mFinalNote.setId(mInitialNote.getId());
            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mIsNewNote = true;
        mMode = EDIT_MODE_ENABLED;
        return true;
    }
    private void saveChanges(){
        if(mIsNewNote){
            saveNewNote();
        }else {
            updateNote();
        }
    }
    private void updateNote(){
        mNoteRepository.updateNote(mFinalNote);
    }
    private void saveNewNote(){
        mNoteRepository.insertNoteTask( mFinalNote);
    }
    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mTextView.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null){
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    private void disableEditMode(){
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mTextView.setVisibility(View.VISIBLE);
        mEditText.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();
        String temp = mLineEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "" );
        if(temp.length() > 0){
            mFinalNote.setTitle(mEditText.getText().toString());
            mFinalNote.setContent(mLineEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mFinalNote.setTimestamp(timestamp);
            if(!mFinalNote.getContent().equals(mInitialNote.getContent()) || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                Log.d(TAG, "disableEditMode: called.");
                saveChanges();
            }
        }
    }
    private void setNoteProperties(){
        mTextView.setText(mInitialNote.getTitle());
        mEditText.setText(mInitialNote.getTitle());
        mLineEditText.setText(mInitialNote.getContent());
    }
    private void setNewNoteProperties(){
        mTextView.setText("Note Title");
        mEditText.setText("Note Title");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: double tapped");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.toolbar_check){
            hideSoftKeyboard();
            disableEditMode();
        }
        if (view.getId() == R.id.note_text_title){
            enableEditMode();
            mEditText.requestFocus();
            mEditText.setSelection(mEditText.length());
        }
        if (view.getId() == R.id.toolbar_back_arrow){
            finish();
        }
    }
    private void disableContentInteraction(){
        mLineEditText.setKeyListener(null);
        mLineEditText.setFocusable(false);
        mLineEditText.setFocusableInTouchMode(false);
        mLineEditText.setCursorVisible(false);
        mLineEditText.clearFocus();
    }
    private void enableContentInteraction(){
        mLineEditText.setKeyListener(new EditText(this).getKeyListener());
        mLineEditText.setFocusable(true);
        mLineEditText.setFocusableInTouchMode(true);
        mLineEditText.setCursorVisible(true);
        mLineEditText.requestFocus();
    }
    @Override
    public void onBackPressed() {
        if(mMode ==EDIT_MODE_ENABLED){
            onClick(mCheck);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextView.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}