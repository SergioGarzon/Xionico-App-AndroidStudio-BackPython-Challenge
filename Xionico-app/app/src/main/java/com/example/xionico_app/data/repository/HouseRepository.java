package com.example.xionico_app.data.repository;

import android.content.Context;
import com.example.xionico_app.data.models.HouseAPICallback;
import com.example.xionico_app.data.services.RapidAPIService;

public class HouseRepository {

    private final RapidAPIService apiService;

    public HouseRepository(Context context) {
        this.apiService = new RapidAPIService(context);
    }

    public void getHouses(final HouseAPICallback callback) {
        apiService.fetchHouses(callback);
    }
}