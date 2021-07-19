package com.example.zobaer53.noteapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zobaer53.noteapp.adapters.NoteAdapter;
import com.example.zobaer53.noteapp.roomdb.Note;
import com.example.zobaer53.noteapp.roomdb.NoteDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NoteAdapter.OnNoteItemClick {

    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private NoteDatabase noteDatabase;
    private List<Note> notes;
    private NoteAdapter noteAdapter;
    private int pos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Methods
        displayList();
        initializeViews();

    }

    private void displayList() {
        noteDatabase = NoteDatabase.getInstance(NoteListActivity.this);
        new RetrieveTask(this).execute();

    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Note>>{

        private WeakReference<NoteListActivity> activityWeakReference;

        RetrieveTask(NoteListActivity context){
            activityWeakReference = new WeakReference<>(context);

        }

        @Override
        protected List<Note> doInBackground(Void... voids) {

            if(activityWeakReference.get() != null){


                return activityWeakReference.get().noteDatabase.getNoteDao().getNotes();

            } else
            return null;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if(notes !=null && notes.size() >0){

                activityWeakReference.get().notes.clear();
                activityWeakReference.get().notes.addAll(notes);
                activityWeakReference.get().textViewMsg.setVisibility(View.GONE);
                activityWeakReference.get().noteAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initializeViews() {

        textViewMsg = findViewById(R.id.textViewEmpty);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoteListActivity.this));
         notes = new ArrayList<>();
         noteAdapter = new NoteAdapter(notes,NoteListActivity.this);
         recyclerView.setAdapter(noteAdapter);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(NoteListActivity.this,
                    NoteActivity.class),100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && requestCode > 0) {

            if (requestCode == 1) {


                notes.add((Note) data.getSerializableExtra("note"));

            } else if (resultCode == 2) {

                notes.set(pos, (Note) data.getSerializableExtra("note"));


            }
            ListVisibility();
        }

    }

    private void ListVisibility() {
        int emptyMsgvisibility = View.GONE;
        if(notes.size() == 0){
            if(textViewMsg.getVisibility() == View.GONE ){

                emptyMsgvisibility = View.VISIBLE;
            }
        }
        textViewMsg.setVisibility((emptyMsgvisibility));
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(int pos) {

        new AlertDialog.Builder(NoteListActivity.this)
                .setItems(new String[]{"Delete","Update"}, new  DialogInterface.OnClickListener(){


                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        switch (i){
                            case 0:
                                noteDatabase.getNoteDao().deleteNote(notes.get(pos));
                                notes.remove((pos));
                                ListVisibility();
                                break;
                            case 1:
                                NoteListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(NoteListActivity.this,
                                                NoteActivity.class).putExtra("note",notes.get(pos)),
                                        100);
                                break;

                        }
                    }
                }).show();

    }

    @Override
    protected void onDestroy() {
        noteDatabase.cleanUp();
        super.onDestroy();
    }
}