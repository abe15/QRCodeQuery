package com.abe.android.floorcodes.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "FloorCodes")
public class StackingFilter {

    @PrimaryKey
    @ColumnInfo(name = "stacking_filter")
    @NonNull
    public String stackingFilter;
    @ColumnInfo(name = "content")
    public String content;


    public StackingFilter( String stackingFilter, String content) {

        this.stackingFilter = stackingFilter;
        this.content = content;
    }
    @Ignore
    public StackingFilter(){}


    public String getStackingFilter() {
        return stackingFilter;
    }

    public void setStackingFilter(String stackingFilter) {
        this.stackingFilter = stackingFilter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return stackingFilter;

    }


}
