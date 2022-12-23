package com.example.intine_panik_uts;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
    public abstract class NoteRoomDatabase extends RoomDatabase{

        public abstract NoteDao noteDao();
        public static volatile NoteRoomDatabase INSTANCE;

        public static NoteRoomDatabase getDatabase(final Context context){
            if (INSTANCE == null){
                synchronized (NoteRoomDatabase.class){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class, "note-database").build();
                }
            }
            return INSTANCE;
        }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
