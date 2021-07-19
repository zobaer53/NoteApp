package com.example.zobaer53.noteapp.roomdb;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataRoomConverter {
    @TypeConverter
    public  static Date toDate(Long value){

    return  value == null ? null : new Date(value);

    }
    @TypeConverter
    public static Long toLong (Date value){

        return value == null ? null : value.getTime();
    }
}
