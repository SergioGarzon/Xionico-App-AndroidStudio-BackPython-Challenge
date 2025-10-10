package com.example.xionico_app.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.xionico_app.data.repository.TaskRepository;

public class TaskSyncWorker  extends Worker {
    public TaskSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskRepository repo = new TaskRepository(getApplicationContext());
        try {
            repo.syncWithServer();  // Sincroniza tareas
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry(); // Reintenta si falla
        }
    }
}
