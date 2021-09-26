package com.amol.example.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.amol.example.data.local.converter.TimestampConverter;
import com.amol.example.data.local.dao.GithubDao;
import com.amol.example.data.local.entity.GithubEntity;


@Database(entities = {GithubEntity.class}, version = 1,  exportSchema = false)
@TypeConverters({TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract GithubDao githubDao();
}
