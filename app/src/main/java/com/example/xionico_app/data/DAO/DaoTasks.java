package com.example.xionico_app.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.xionico_app.data.models.Task;
import java.util.List;

@Dao
public interface DaoTasks {

    @Query("SELECT * FROM Task")
    List<Task> getTask();

    @Query("SELECT * FROM Task WHERE needsSync = 1")
    List<Task> getTasksToSync();

    @Query("UPDATE Task SET status= :status, needsSync = 1 WHERE id= :id")
    void updateTasks(int id, String status);

    @Insert
    void insertTasks(Task ... task);

    @Query("SELECT COUNT(id) FROM Task")
    int countTasks();

    @Query("DELETE FROM task WHERE id > 0")
    void deleteAllTask();

}
