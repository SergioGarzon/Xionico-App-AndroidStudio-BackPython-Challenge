package com.example.xionico_app.data.models;

import java.util.List;

public interface HouseAPICallback {
    void onSuccess(List<House> houseList);
    void onError(String errorMessage);
}