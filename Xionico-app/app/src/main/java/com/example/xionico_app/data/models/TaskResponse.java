package com.example.xionico_app.data.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskResponse {
    int id;
    String title;
    String description;
    String status;
}