package com.example.zobaer53.noteapp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NoteDAO {
    @Query("SELECT * FROM " + Constants.Table_Name_Note)
    List<Note> getNotes();

    //Insert Data

    @Insert
    long insertNote(Note note);

    @Update
    void updateNote(Note repos);

    @Delete
    void deleteNote(Note note);

    //Delete All Notes
    @Delete
    void deleteNotes(Note... note);
}
