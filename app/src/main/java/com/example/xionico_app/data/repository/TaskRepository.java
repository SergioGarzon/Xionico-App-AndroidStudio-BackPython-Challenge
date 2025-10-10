package com.example.xionico_app.data.repository;

import com.example.xionico_app.data.DAO.AppDBSQLite;
import com.example.xionico_app.data.DAO.DaoTasks;
import com.example.xionico_app.data.models.Task;
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

    public void updateTask(int id, String status) {
        daoTasks.updateTasks(id, status);
    }

    public List<Task> getAllTasks() {
        return daoTasks.getTask();
    }

    public void syncWithServer() {
        // 1. Subir tareas locales nuevas o modificadas
        List<Task> localTasks = daoTasks.getTasksToSync();
        for (Task task : localTasks) {
            if(task.getApiId() == 0) {
                var apiTask = new TaskApiService.CreateTask(task.getTitle(), task.getDescription());
                api.createTask(apiTask).enqueue(new Callback<TaskApiService.TaskResponse>() {
                    @Override
                    public void onResponse(Call<TaskApiService.TaskResponse> call, Response<TaskApiService.TaskResponse> response) {
                        log.info("SYNC - Created new task: " + task.getId());
                    }

                    @Override
                    public void onFailure(Call<TaskApiService.TaskResponse> call, Throwable t) {
                        log.warning("SYNC - Failed to create: " + task.getId());
                    }
                });
            } else {
                api.updateTaskStatus(String.valueOf(task.getApiId()), new TaskApiService.StatusUpdate(task.getStatus()))
                        .enqueue(new Callback<TaskApiService.TaskResponse>() {

                    @Override
                    public void onResponse(Call<TaskApiService.TaskResponse> call, Response<TaskApiService.TaskResponse> response) {
                        log.info("SYNC - Updated task: " + task.getId());
                    }

                    @Override
                    public void onFailure(Call<TaskApiService.TaskResponse> call, Throwable t) {
                        log.warning("SYNC - Failed to update: " + t.getMessage());
                    }
                });
            }
        }

        // 2. Obtener tareas del servidor y guardar localmente
        api.getTasks().enqueue(new Callback<List<TaskApiService.TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskApiService.TaskResponse>> call, Response<List<TaskApiService.TaskResponse>> response) {
                if (response.isSuccessful()) {
                    var serverTasks = response.body();
                    for (TaskApiService.TaskResponse apiTask : serverTasks) {
                        Task task = new Task();
                        task.setApiId(apiTask.getId());
                        task.setTitle(apiTask.getTitle());
                        task.setDescription(apiTask.getDescription());
                        task.setStatus(apiTask.getStatus());
                        daoTasks.insertTasks(task);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskApiService.TaskResponse>> call, Throwable t) {
                log.warning("SYNC - Failed to fetch tasks: " + t.getMessage());
            }
        });
    }

    /*
    public void syncWithServer() {
        // 1. Subir tareas locales nuevas o modificadas
        List<Task> localTasks = daoTasks.getTasksToSync();
        for (Task task : localTasks) {
            try {
                if(task.getApiId() == 0) {
                    // LLAMADA SÍNCRONA
                    Response<TaskApiService.TaskResponse> response = api.createTask(
                            new TaskApiService.CreateTask(task.getTitle(), task.getDescription())
                    ).execute(); // <-- ¡USAR .execute() AQUÍ!

                    if (response.isSuccessful()) {
                        log.info("SYNC - Created new task: " + task.getId());
                        // TODO: ACTUALIZAR LA TAREA LOCAL CON API_ID Y needsSync = false
                    } else {
                        log.warning("SYNC - Failed to create. Code: " + response.code());
                    }

                } else {
                    // LLAMADA SÍNCRONA
                    Response<TaskApiService.TaskResponse> response = api.updateTaskStatus(
                            String.valueOf(task.getApiId()),
                            new TaskApiService.StatusUpdate(task.getStatus())
                    ).execute(); // <-- ¡USAR .execute() AQUÍ!

                    if (response.isSuccessful()) {
                        log.info("SYNC - Updated task: " + task.getId());
                        // TODO: ACTUALIZAR needsSync = false
                    } else {
                        log.warning("SYNC - Failed to update. Code: " + response.code());
                    }
                }
            } catch (Exception e) {
                // Manejar error de red (Timeouts, DNS, etc.)
                log.severe("Network Error during sync: " + e.getMessage());
                // No reintentamos aquí, dejamos que el Worker lo maneje
            }
        }

        // 2. Obtener tareas del servidor (Sincronización Pull)
        try {
            // LLAMADA SÍNCRONA
            Response<List<TaskApiService.TaskResponse>> response = api.getTasks().execute(); // <-- ¡USAR .execute() AQUÍ!

            if (response.isSuccessful() && response.body() != null) {
                log.info("SYNC - Successfully fetched tasks from server.");
                // TODO: LÓGICA DE FUSIONAR/REEMPLAZAR EN DB LOCAL
            } else {
                log.warning("SYNC - Failed to fetch tasks. Code: " + response.code());
            }
        } catch (Exception e) {
            log.severe("Network Error during pull sync: " + e.getMessage());
        }
    }*/
}