package com.example.xionico_app.data.services;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.xionico_app.data.models.House;
import com.example.xionico_app.data.models.HouseAPICallback;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapidAPIService {

    private final Context context;
    private final String api_url = "https://zillow-com4.p.rapidapi.com/properties/search-coordinates?location=Houston%2C%20TX&eastLng=-94.517205&westLng=-96.193233&southLat=29.170258&northLat=29.657524&status=forSale&sort=relevance&sortType=asc&priceType=listPrice&listingType=agent";
    private final String api_key = "47dc174f4cmsh8332c65ae30cf8bp1e5cc8jsne264a9419954";
    private final String api_host = "zillow-com4.p.rapidapi.com";


    public RapidAPIService(Context context) {
        this.context = context;
    }

    public void fetchHouses(final HouseAPICallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<House> houseList = parseResponse(response);
                            callback.onSuccess(houseList);
                        } catch(Exception e) {
                            e.printStackTrace();
                            callback.onError("Error al procesar el JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = (error.networkResponse != null) ?
                                new String(error.networkResponse.data) : error.getMessage();
                        callback.onError("Error de API: " + errorMessage);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-rapidapi-key", api_key);
                headers.put("x-rapidapi-host", api_host);
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    private List<House> parseResponse(String response) throws Exception {
        List<House> houseList = new ArrayList<>();
        JSONObject rootObject = new JSONObject(response);

        JSONArray jsonArray = rootObject.getJSONArray("data");

        for (int i = 0 ; i < jsonArray.length() ; i++) {
            JSONObject houseObject = jsonArray.getJSONObject(i);

            JSONObject addressObject = houseObject.getJSONObject("address");
            String city = addressObject.getString("city");
            String state = addressObject.getString("state");
            String streetAddress = addressObject.getString("streetAddress");

            JSONObject mediaObject = houseObject.getJSONObject("media");
            JSONObject photoLinksObject = mediaObject.getJSONObject("propertyPhotoLinks");
            String highResolutionLink = photoLinksObject.getString("highResolutionLink");

            houseList.add(new House(city, state, streetAddress, highResolutionLink));
        }
        return houseList;
    }
}