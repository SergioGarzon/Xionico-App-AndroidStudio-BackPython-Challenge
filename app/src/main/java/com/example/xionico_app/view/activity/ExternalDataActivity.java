package com.example.xionico_app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xionico_app.R;
import com.example.xionico_app.view.extra.HouseDetailsAdapter;
import com.example.xionico_app.data.models.House;
import com.example.xionico_app.viewmodel.ExternalDataViewModel;
import java.util.ArrayList;
import java.util.List;

public class ExternalDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnBackExternalDataShow;
    private ExternalDataViewModel viewModel;
    private HouseDetailsAdapter adapter;
    private final List<House> houseList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_external_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.house_rent);
        btnBackExternalDataShow = findViewById(R.id.btnBackExternalDataShow);

        adapter = new HouseDetailsAdapter(houseList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        btnBackExternalDataShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        viewModel = new ViewModelProvider(this).get(ExternalDataViewModel.class);

        viewModel.getHouseList().observe(this, new Observer<List<House>>() {
            @Override
            public void onChanged(List<House> houses) {
                houseList.clear();
                houseList.addAll(houses);
                adapter.notifyDataSetChanged();
            }
        });

        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null && !message.isEmpty()) {
                    showToast("Error: " + message);
                }
            }
        });
    }


    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}