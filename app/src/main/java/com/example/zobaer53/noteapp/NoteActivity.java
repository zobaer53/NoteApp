 package com.example.zobaer53.noteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.app.Activity;
import android.app.AsyncNotedAppOp;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zobaer53.noteapp.roomdb.Note;
import com.example.zobaer53.noteapp.roomdb.NoteDatabase;

import java.lang.ref.WeakReference;

 public class NoteActivity extends AppCompatActivity {

    private EditText editTextTitle,editTextContent;
     private Button button;
    private boolean update;

    private NoteDatabase noteDatabase;
    private Note note;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
         button = findViewById(R.id.buttonSave);

       noteDatabase = NoteDatabase.getInstance(NoteActivity.this); // getting instance from db

        //check for correct obj and date
        if((note = (Note) getIntent().getSerializableExtra("note")) !=null){

            getSupportActionBar().setTitle("Update Note");
            update = true;
            button.setText("Update");
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(update){

                    note.setContent(editTextContent.getText().toString());
                    note.setTitle(editTextTitle.getText().toString());
                    noteDatabase.getNoteDao().updateNote(note);
                    setResult(note,2);
                }
                else{

                    note = new Note(editTextContent.getText().toString(),
                            editTextTitle.getText().toString());
                    new InsertTask(NoteActivity.this,note).execute();

                }

            }
        });

    }
    public void setResult(Note note,int flag){


        setResult(flag,new Intent().putExtra("note",note));
        finish();
    }




private static class InsertTask extends AsyncTask<Void, Void ,Boolean> {
 private WeakReference<NoteActivity> activityWeakReference;
 private Note note;

    public InsertTask(NoteActivity context, Note note) {
        activityWeakReference = new WeakReference<>(context);
        this.note = note;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        long j = activityWeakReference.get().noteDatabase.getNoteDao().insertNote(note);
        note.setNote_id(j);
        notify();

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      if(aBoolean){

          activityWeakReference.get().setResult(note,1);
          activityWeakReference.get().finish();
      }
    }
}

}