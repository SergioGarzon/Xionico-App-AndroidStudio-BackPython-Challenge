package com.example.xionico_app.data.repository;

import com.example.xionico_app.data.DAO.AppDBSQLite;
import com.example.xionico_app.data.DAO.DaoTasks;
import com.example.xionico_app.data.models.CreateTask;
import com.example.xionico_app.data.models.StatusUpdate;
import com.example.xionico_app.data.models.Task;
import com.example.xionico_app.data.models.TaskResponse;
import com.example.xionico_app.data.services.ApiClient;
import com.example.xionico_app.data.services.TaskApiService;

import android.content.Context;
import java.util.List;

import lombok.extern.java.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Log
public class TaskRepository {
    private final DaoTasks daoTasks;
    private final TaskApiService api;

    public TaskRepository(Context context) {
        AppDBSQLite db = AppDBSQLite.getDatabase(context);
        daoTasks = db.daoTasks();
        api = ApiClient.getService();
    }

    public void deleteAllTask() {
        daoTasks.deleteAllTask();
    }

    public void insertTask(Task task) {
        daoTasks.insertTasks(task);
    }

    public void updateTask(int id, String status, int apiId) {
        daoTasks.updateTask(id, status, true, apiId);
    }

    public List<Task> getAllTasks() {
        return daoTasks.getTask();
    }

    public void syncWithServer() {
        List<Task> localTasks = daoTasks.getTasksToSync();
        for (Task task : localTasks) {
            log.info(task.toString());
            if(task.getApiId() == 0) {
                var apiTask = new CreateTask(task.getTitle(), task.getDescription());
                api.createTask(apiTask).enqueue(new Callback<TaskResponse>() {
                    @Override
                    public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                        var taskResponse = response.body();
                        log.info("SYNC - Created new task: " + task.getId());
                        daoTasks.updateTask(task.getId(), task.getStatus(), false, taskResponse.getId());
                    }

                    @Override
                    public void onFailure(Call<TaskResponse> call, Throwable t) {
                        log.warning("SYNC - Failed to create: " + task.getId());
                    }
                });
            } else {
                api.updateTaskStatus(String.valueOf(task.getApiId()), new StatusUpdate(task.getStatus()))
                        .enqueue(new Callback<TaskResponse>() {

                    @Override
                    public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                        var taskResponse = response.body();
                        log.info("SYNC - Updated task: " + task.getId());
                        daoTasks.updateTask(task.getId(), task.getStatus(), false, taskResponse.getId());
                    }

                    @Override
                    public void onFailure(Call<TaskResponse> call, Throwable t) {
                        log.warning("SYNC - Failed to update: " + t.getMessage());
                    }
                });
            }
        }

        api.getTasks().enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful()) {
                    var serverTasks = response.body();
                    for (TaskResponse apiTask : serverTasks) {
                        if(daoTasks.getTaskByApiId(apiTask.getId())) {
                            continue;
                        }
                        Task task = new Task();
                        task.setApiId(apiTask.getId());
                        task.setTitle(apiTask.getTitle());
                        task.setDescription(apiTask.getDescription());
                        task.setStatus(apiTask.getStatus());
                        task.setNeedsSync(false);
                        daoTasks.insertTasks(task);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                log.warning("SYNC - Failed to fetch tasks: " + t.getMessage());
            }
        });
    }
}