package com.example.xionico_app.viewmodel;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.xionico_app.data.models.House;
import com.example.xionico_app.data.models.HouseAPICallback;
import com.example.xionico_app.data.repository.HouseRepository;
import java.util.List;

public class ExternalDataViewModel extends AndroidViewModel {

    private final MutableLiveData<List<House>> houseList = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final HouseRepository repository;

    public LiveData<List<House>> getHouseList() {
        return houseList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public ExternalDataViewModel(@NonNull Application application) {
        super(application);
        repository = new HouseRepository(application.getApplicationContext());
        loadHouseData();
    }

    public void loadHouseData() {

        repository.getHouses(new HouseAPICallback() {
            @Override
            public void onSuccess(List<House> houses) {
                houseList.postValue(houses);
            }

            @Override
            public void onError(String message) {
                errorMessage.postValue(message);
            }
        });
    }
}
