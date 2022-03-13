package com.abe.android.floorcodes;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abe.android.floorcodes.models.StackingFilter;

import java.io.File;


@Database(entities = {StackingFilter.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract MyDao myDao();

    public static AppDatabase getAppDatabase(Context context) {
        File s = context.getDatabasePath("database_name.db");
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database_name")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

     public void cleanUp()
     {
         INSTANCE = null;

     }

}
