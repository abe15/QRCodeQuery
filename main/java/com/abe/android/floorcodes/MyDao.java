package com.abe.android.floorcodes;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abe.android.floorcodes.models.StackingFilter;

import java.util.List;

@Dao
public interface MyDao {


    @Query("SELECT * FROM FloorCodes WHERE stacking_filter LIKE :search LIMIT 1")
    public StackingFilter findCode(String search);

    @Query("SELECT * FROM FloorCodes WHERE stacking_filter like '%' || :search || '%'")
    public List<StackingFilter> findCodeMultiple(String search);


    @Query("SELECT * FROM FloorCodes")
    public List<StackingFilter> getAll();

    @Insert(onConflict=1)
    public void insertAll(List<StackingFilter> s);

    @Delete
    public void delete(StackingFilter s);




}
