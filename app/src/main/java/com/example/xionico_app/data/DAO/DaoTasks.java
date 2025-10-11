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

    @Query("UPDATE Task SET status = :status, needsSync = :needsSync, apiId = :apiId  WHERE id= :id")
    void updateTask(int id, String status, boolean needsSync, int apiId);

    @Insert
    void insertTasks(Task ... task);

    @Query("SELECT COUNT(id) FROM Task")
    int countTasks();

    @Query("DELETE FROM task WHERE id > 0")
    void deleteAllTask();

    @Query("SELECT EXISTS(SELECT 1 FROM Task WHERE apiId = :apiId)")
    boolean getTaskByApiId(int apiId);
}
