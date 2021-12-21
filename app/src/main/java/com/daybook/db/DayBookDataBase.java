package com.daybook.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.daybook.db.dao.CategoryDao;
import com.daybook.db.dao.TransactionDao;
import com.daybook.db.model.CategoryModel;
import com.daybook.db.model.TransactionModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TransactionModel.class, CategoryModel.class}, version = 1, exportSchema = false)
public abstract class DayBookDataBase extends RoomDatabase {

    public abstract TransactionDao transactions();

    public abstract CategoryDao categoryDao();

    private static volatile DayBookDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static DayBookDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DayBookDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DayBookDataBase.class, "day_book_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}