package com.example.xionico_app.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.view.View;
import android.content.Intent;

import com.android.volley.BuildConfig;
import com.example.xionico_app.R;
import com.example.xionico_app.data.models.Task;
import com.example.xionico_app.data.repository.TaskRepository;
import com.example.xionico_app.view.extra.TaskDetailsAdapter;
import com.example.xionico_app.workers.TaskSyncWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Configuration;

public class MainActivity extends AppCompatActivity {

    private Button btnTarea, btnDatosExternos;
    private RecyclerView rcvTaskList;
    private TaskDetailsAdapter taskAdapter;
    private TaskRepository taskRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnTarea = findViewById(R.id.btnTarea);
        btnDatosExternos = findViewById(R.id.btnDatosExternos);
        rcvTaskList = findViewById(R.id.rcvTaskList);

        taskRepository = new TaskRepository(getApplicationContext());

        try {
            scheduleSyncWorker();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Task> currentTasks = taskRepository.getAllTasks();

        taskAdapter = new TaskDetailsAdapter(this, taskRepository);
        rcvTaskList.setLayoutManager(new LinearLayoutManager(this));
        rcvTaskList.setAdapter(taskAdapter);


        btnTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivityNewTask(v);
            }
        });

        btnDatosExternos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivityExternos(view);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //taskRepository.syncWithServer();
        List<Task> currentTasks = taskRepository.getAllTasks();

        if (taskAdapter != null)
            taskAdapter.setTasks(currentTasks);
    }

    private void changeActivityNewTask(View v) {
        startActivity(new Intent(this, AddTaskActivity.class));
    }

    private void changeActivityExternos(View v) {
        startActivity(new Intent(this, ExternalDataActivity.class));
    }

    private void scheduleSyncWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // solo con internet
                .build();

        PeriodicWorkRequest syncWorkRequest =
                new PeriodicWorkRequest.Builder(TaskSyncWorker.class, 2, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "task_sync_worker", // ID único del trabajo
                ExistingPeriodicWorkPolicy.KEEP, // No lo sobrescribe si ya está encolado
                syncWorkRequest
        );
    }
}