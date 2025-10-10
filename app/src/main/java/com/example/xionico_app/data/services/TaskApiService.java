package com.example.xionico_app.data.services;

import com.example.xionico_app.data.models.Task;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskApiService {

    @GET("tasks/")
    Call<List<TaskResponse>> getTasks();

    @POST("tasks/")
    Call<TaskResponse> createTask(@Body CreateTask task);

    @PATCH("tasks/{apiId}")
    Call<TaskResponse> updateTaskStatus(@Path("apiId") String apiId, @Body StatusUpdate status);

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTask {
        String title;
        String description;
    }

    @Setter
    @Getter
    class TaskResponse {
        int id;
        String title;
        String description;
        String status;
    }
    class StatusUpdate {
        String status;
        public StatusUpdate(String status) { this.status = status; }
    }
}
