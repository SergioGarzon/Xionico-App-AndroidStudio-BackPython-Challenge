package com.example.xionico_app.data.services;


import java.util.List;
import com.example.xionico_app.data.models.CreateTask;
import com.example.xionico_app.data.models.TaskResponse;
import com.example.xionico_app.data.models.StatusUpdate;

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





}
