package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    RequestQueue requestQueue;
    String url = "https://api.covid19api.com/summary";
    TextView activeNo, activeNoChng, dischargeNO, dischargeNOChng, deathNo, deathNoChng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spinner = findViewById(R.id.spinner);
        activeNo = findViewById(R.id.active_no);
        activeNoChng = findViewById(R.id.active_no_chng);
        dischargeNO = findViewById(R.id.discharge_no);
        dischargeNOChng = findViewById(R.id.discharge_no_chng);
        deathNo = findViewById(R.id.death_no);
        deathNoChng = findViewById(R.id.death_no_change);

        requestQueue = Volley.newRequestQueue(this);

        List<String> country = new ArrayList<String>();
        country.add("Global");
//        country.add("India");
//        country.add("Business Services");
//        country.add("Computers");
//        country.add("Education");
//        country.add("Personal");
//        country.add("Travel");


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("Countries");

                            for(int i=0;i<jsonArray.length(); i++){
                                JSONObject Countries = jsonArray.getJSONObject(i);
                                String Country = Countries.getString("Country");

                                country.add(Country);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (parent.getSelectedItem().toString().equals("Global")){
            Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

            activeNo.setText(String.valueOf(0));
            activeNoChng.setText("( "+0+" )");
            dischargeNO.setText(String.valueOf(0));
            dischargeNOChng.setText("( "+0+" )");
            deathNo.setText(String.valueOf(0));
            deathNoChng.setText("( "+0+" )");

        } else {
            Toast.makeText(this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            setData(parent.getSelectedItem().toString());
        }


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setData(String countryName) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("Countries");

                            for(int i=0;i<jsonArray.length(); i++){
                                JSONObject Countries = jsonArray.getJSONObject(i);
                                String Country = Countries.getString("Country");

                                if (Country.equals(countryName)){
                                    int TotalConfirmed = Countries.getInt("TotalConfirmed");
                                    int TotalDeaths = Countries.getInt("TotalDeaths");
                                    int TotalRecovered = Countries.getInt("TotalRecovered");
                                    int NewConfirmed = Countries.getInt("NewConfirmed");
                                    int NewDeaths = Countries.getInt("NewDeaths");
                                    int NewRecovered = Countries.getInt("NewRecovered");

                                    int activeChng = NewConfirmed-NewRecovered-NewDeaths;
                                    if (activeChng<0){
                                        activeNoChng.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_downward, 0, 0, 0);
                                    } else if (activeChng==0){
                                        activeNoChng.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    } else {
                                        activeNoChng.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_upward, 0, 0, 0);
                                    }

                                    if (NewRecovered==0){
                                        dischargeNOChng.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    }
                                    if (NewDeaths==0){
                                        deathNoChng.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    }

                                    activeNo.setText(String.valueOf(TotalConfirmed-TotalRecovered-TotalDeaths));
                                    activeNoChng.setText("( "+String.valueOf(Math.abs(activeChng))+" )");
                                    dischargeNO.setText(String.valueOf(TotalRecovered));
                                    dischargeNOChng.setText("( "+NewRecovered+" )");
                                    deathNo.setText(String.valueOf(TotalDeaths));
                                    deathNoChng.setText("( "+NewDeaths+" )");

                                    break;
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }
}