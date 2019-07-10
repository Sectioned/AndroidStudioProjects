package com.bgrummitt.notes.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bgrummitt.notes.controller.adapters.ListAdapter;
import com.bgrummitt.notes.controller.databse.DatabaseHelper;
import com.bgrummitt.notes.model.Note;
import com.bgrummitt.notes.R;
import com.bgrummitt.notes.controller.callback.SwipeToDeleteOrCompleteCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ListAdapter mListAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView =  findViewById(R.id.list);

        // Set FAB and then set on click listener to create new note
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this, "NOTES_DB");

        List<Note> notes = getNotesFromDB();

        setUpRecyclerView(notes);

        // Make it so fab disappears when scrolling down then reappears when scrolling back up
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // If the rate of change of y > 0 hide else show
                if(dy > 0){
                    fab.hide();
                } else{
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // Add "HamBurger" icon inn toolbar with open close action and animation
        // Retrieve draw and navigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Create an action bar toggle with the drawer view and toolbar with string id's for accessibility description
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_select_all) {
            Toast.makeText(this, "Select All", Toast.LENGTH_SHORT).show();
            mListAdapter.selectAll();
            mListAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    public void newNote(){
        // Build the pop up for the new note
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        // Create a new view with the note layout
        View mView = getLayoutInflater().inflate(R.layout.dialog_new_note, null);

        // Get the edit elements from the view
        final EditText mSubject = mView.findViewById(R.id.subjectEditText);
        final EditText mNotes = mView.findViewById(R.id.mainNotesEditText);
        Button mSaveButton = mView.findViewById(R.id.saveButton);

        // Build the dialog with the view
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        // Add the on click listener so when the sections have been filled the note is added and
        // the dialog gets dismissed
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mSubject.getText().toString().isEmpty() && !mNotes.getText().toString().isEmpty()){
                    String subject = mSubject.getText().toString();
                    String note = mNotes.getText().toString();
                    makeNewNote(subject, note);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void markNoteCompleted(int id){
        mDatabaseHelper.moveNoteToCompleted(id);
    }

    public void makeNewNote(String subject, String note){
        //Add note to db
        mDatabaseHelper.addNoteToBeCompleted(subject, note);
        // Add the note in the adapter and refresh
        mListAdapter.addNote(new Note(subject, note, false, mDatabaseHelper.getToBeCompletedCurrentMaxID()));
        mListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.Completed_List){

        }else if(id == R.id.TODO_List){

        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Note> getNotesFromDB(){
        Cursor cursor = mDatabaseHelper.getNotesToBeCompleted();

        List<Note> notes = new ArrayList<>();

        if(cursor.moveToFirst()){
            int indexID = cursor.getColumnIndex(DatabaseHelper.ID_COLUMN_NAME);
            int indexSubject = cursor.getColumnIndex(DatabaseHelper.SUBJECT_COLUMN_NAME);
            int indexNote = cursor.getColumnIndex(DatabaseHelper.NOTE_COLUMN_NAME);

            while(!cursor.isAfterLast()){
                notes.add(new Note(cursor.getString(indexSubject), cursor.getString(indexNote), false, cursor.getInt(indexID)));
                cursor.moveToNext();
            }
        }

        return notes;
    }

    public void setUpRecyclerView(List<Note> notes){
        // Create the list adapter and set the recycler views adapter to the created list adapter
        mListAdapter = new ListAdapter(this, notes);
        mRecyclerView.setAdapter(mListAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // When at end of list give half oval show still pulling
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteOrCompleteCallback(mListAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

}