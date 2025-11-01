package com.example.xionico_app.view.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xionico_app.data.models.House;
import com.example.xionico_app.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class HouseDetailsAdapter extends RecyclerView.Adapter<HouseDetailsAdapter.HouseDetailsHolder> {

    private List<House> houseDetailsList;
    private Context context;

    @NonNull
    @Override
    public HouseDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.houserent_activity, parent, false);
        return new HouseDetailsHolder(view);
    }

    @Override
    public int getItemCount() {
        return houseDetailsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull HouseDetailsAdapter.HouseDetailsHolder holder, int position) {
        holder.txtCityValue.setText(houseDetailsList.get(position).getCity());
        holder.txtCountryValue.setText(houseDetailsList.get(position).getState());
        holder.txtCurrencyValue.setText(houseDetailsList.get(position).getStreetAddress());
        Picasso.get().load(houseDetailsList.get(position).getHighResolutionLink()).into(holder.imgSrc);
    }

    public class HouseDetailsHolder extends RecyclerView.ViewHolder {

        private ImageView imgSrc;
        private TextView txtCountryValue, txtCityValue, txtCurrencyValue;

        public HouseDetailsHolder(@NotNull View itemView) {
            super(itemView);

            imgSrc = itemView.findViewById(R.id.imgHouseRent);
            txtCountryValue = itemView.findViewById(R.id.txtCountryValue);
            txtCityValue = itemView.findViewById(R.id.txtCityValue);
            txtCurrencyValue = itemView.findViewById(R.id.txtCurrencyValue);
        }
    }
}
