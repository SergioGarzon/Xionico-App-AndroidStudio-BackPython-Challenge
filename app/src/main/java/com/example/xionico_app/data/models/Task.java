package com.example.xionico_app.data.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Task {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    private int id;

    private int apiId = 0;

    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String status;

    private boolean needsSync = true;
}