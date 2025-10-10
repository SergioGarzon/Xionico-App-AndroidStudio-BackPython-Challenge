package com.example.xionico_app.data.DAO;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.xionico_app.data.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDBSQLite extends RoomDatabase {
    public abstract DaoTasks daoTasks();
    private static volatile AppDBSQLite INSTANCE;

    public static AppDBSQLite getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDBSQLite.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDBSQLite.class,
                                    "TodoDB-app"
                            ).allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
