package com.github.pires.obd.reader.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pires.obd.reader.R;
import com.github.pires.obd.reader.Vehicle;
import com.github.pires.obd.reader.VehicleSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class NewDrive extends Activity {

    private Spinner spinner;
    private static final String PATH_TO_SERVER = "http://192.168.0.106:8080/vehicleREST";
    /*Lista de veiculos armazenada para o spinner*/
    protected List<Vehicle> spinnerData;
    /*Fila de requests do volley*/
    private RequestQueue queue;
    private Spinner VehicleSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drive);
        queue = Volley.newRequestQueue(this);
        VehicleSpinner = (Spinner) findViewById(R.id.vehicle);
        requestJsonObject();

        // Class Spinner implementing onItemSelectedListener
        VehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView txtmodel = (TextView) findViewById(R.id.model);
                TextView txtbrand = (TextView) findViewById(R.id.brand);
                TextView txtyear = (TextView) findViewById(R.id.year);
                TextView txtpower = (TextView) findViewById(R.id.power);
                TextView txttorque = (TextView) findViewById(R.id.torque);
                TextView txtengsize = (TextView) findViewById(R.id.eng_size);

                // Set the text followed by the position
                txtmodel.setText("Model : "
                        + spinnerData.get(position).getModel());
                txtbrand.setText("Brand : "
                        + spinnerData.get(position).getBrand());
                txtyear.setText("Year : "
                        + spinnerData.get(position).getYear());
                txtpower.setText("Power (hp) : "
                        + spinnerData.get(position).getPower());
                txttorque.setText("Torque (Nm) : "
                        + spinnerData.get(position).getTorque());
                txtengsize.setText("Engine size (L) : "
                        + spinnerData.get(position).getEng_size());

                /*Joga dados pra atividade principal*/
                /*Intent passVehicle = new Intent(NewDrive.this, MainActivity.class);
                String model = txtmodel.getText().toString();
                passVehicle.putExtra("model", "HARDCODE");
                startActivity(passVehicle);*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.putExtra("model", "HARDCODE");
        setResult(99, i);
        finish();
    }

    private void requestJsonObject(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_SERVER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                spinnerData = Arrays.asList(mGson.fromJson(response, Vehicle[].class));
                //display first question to the user
                if(null != spinnerData){
                    spinner = (Spinner) findViewById(R.id.vehicle);
                    assert spinner != null;
                    spinner.setVisibility(View.VISIBLE);
                    VehicleSpinnerAdapter spinnerAdapter = new VehicleSpinnerAdapter(NewDrive.this, spinnerData);
                    spinner.setAdapter(spinnerAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
}
