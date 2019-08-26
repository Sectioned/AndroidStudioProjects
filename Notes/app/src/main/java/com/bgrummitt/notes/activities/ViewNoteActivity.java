package com.bgrummitt.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.adapters.ListAdapter;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;

public class ViewNoteActivity extends AppCompatActivity {

    final static private String TAG = ViewNoteActivity.class.getSimpleName();

    final static public int EDITED_RETURN_RESULT = 13;

    private Intent mIntent;
    private ListAdapter.ListTypes mNoteType;
    private String mNoteSubject;
    private String mNoteBody;
    private TextView mBodyTextView;
    private int mDbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        mIntent = getIntent();

        mDbID = mIntent.getIntExtra(ListAdapter.NOTE_ID, -1);
        mNoteSubject = mIntent.getStringExtra(ListAdapter.NOTE_SUBJECT);
        mNoteBody = mIntent.getStringExtra(ListAdapter.NOTE_BODY);
        mNoteType = (ListAdapter.ListTypes)mIntent.getSerializableExtra(ListAdapter.NOTE_TYPE);

        // Get the size of the window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set the size of this activity
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));
        getWindow().setBackgroundDrawable(getDrawable(R.drawable.note_background_shape));

        // Center the activity
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Save the positioning
        getWindow().setAttributes(params);

        mBodyTextView = findViewById(R.id.noteBodyTextView);

        setTitle(mNoteSubject);

        mBodyTextView.setText(mNoteBody);
        mBodyTextView.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int id;

        switch (mNoteType){
            case TODO_LIST:
                id = R.menu.menu_note_todo;
                break;
            case COMPLETED_LIST:
                id = R.menu.menu_note_completed;
                break;
            default:
                id = R.menu.menu_note_completed;
        }

        getMenuInflater().inflate(id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_button:
                Log.d(TAG, "Edit Pressed");
                convertActivityToEdit();
                break;
            case R.id.complete_button:
                Log.d(TAG, "Complete Button");
                break;
            case R.id.delete_button:
                Log.d(TAG, "Delete Button");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void convertActivityToEdit(){
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(ListAdapter.NOTE_SUBJECT, mNoteSubject);
        intent.putExtra(ListAdapter.NOTE_BODY, mNoteBody);
        startActivityForResult(intent, EDITED_RETURN_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "ACTIVITY RESULT");

        switch (resultCode){
            case EDITED_RETURN_RESULT:
                Log.d(TAG, "EDITED NOTE");
                String subject = data.getStringExtra(ListAdapter.NOTE_SUBJECT);
                String body = data.getStringExtra(ListAdapter.NOTE_BODY);
                setTitle(subject);
                mBodyTextView.setText(body);
                editTodoNote(subject, body);
                break;
        }
    }

    public void editTodoNote(String subject, String body){
        DatabaseHelper dbHelper = new DatabaseHelper(this, "NOTES_DB");
        dbHelper.editNote(DatabaseHelper.TO_COMPLETE_TABLE_NAME, mDbID, subject, body);
        dbHelper.close();
    }

}
