package com.github.pires.obd.reader.activity;
import com.github.pires.obd.reader.Drive;
import com.github.pires.obd.reader.GsonRequest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pires.obd.reader.R;
import com.github.pires.obd.reader.Vehicle;
import com.github.pires.obd.reader.VehicleSpinnerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewDrive extends Activity {

    private Spinner spinner;
    private static final String PATH_TO_SERVER = "http://192.168.1.103:8080/vehicleREST";
    /*Lista de veiculos armazenada para o spinner*/
    protected List<Vehicle> spinnerData;
    /*Fila de requests do volley*/
    private RequestQueue queue;
    private Spinner VehicleSpinner;
    private int driveid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_drive);
        queue = Volley.newRequestQueue(this);
        VehicleSpinner = (Spinner) findViewById(R.id.vehicle);
        requestJsonObject();
        final Intent passVehicle = new Intent(NewDrive.this, MainActivity.class);
        //Set button to disabled until user selects vehicle
        final Button button = (Button) findViewById(R.id.new_drive);
        button.setEnabled(false);
        final TextView txtvin = (TextView) findViewById(R.id.vin);
        // Class Spinner implementing onItemSelectedListener
        VehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TextView txtvin = (TextView) findViewById(R.id.vin);
                TextView txtmodel = (TextView) findViewById(R.id.model);
                TextView txtbrand = (TextView) findViewById(R.id.brand);
                TextView txtyear = (TextView) findViewById(R.id.year);

                // Set the text followed by the position
                txtmodel.setText("Model : "
                        + spinnerData.get(position).getModel());
                txtbrand.setText("Brand : "
                        + spinnerData.get(position).getBrand());
                txtyear.setText("Year : "
                        + spinnerData.get(position).getYear());
                txtvin.setText(
                        spinnerData.get(position).getVin());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // can leave this empty
            }
        });

        //Event listener for create drive button
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //When button gets pressed, a POST request is sent to the API creating a new drive with
                //the selected vehicle
                Drive drive = new Drive();
                drive.setMileage_end(1234);
                drive.setMileage_start(123);
                GsonRequest<Drive> new_drive = new GsonRequest<Drive>("http://192.168.1.103:8080/vehicle/" + txtvin.getText().toString() + "/drive",
                        drive,
                        Drive.class,
                        new HashMap<String, String>(),
                        new Response.Listener<Drive>() {
                            @Override
                            public void onResponse(Drive response) {
                            //Retorna o id da drive e passa para a proxima atividade
                                driveid = response.getId();
                                passVehicle.putExtra("driveid", driveid);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("post error","post error",error);
                            }

                        });

                queue.add(new_drive);
                /*Joga dados pra atividade principal*/

                TextView txtmodel = (TextView) findViewById(R.id.model);
                String model = txtmodel.getText().toString();

                TextView txtvin = (TextView) findViewById(R.id.vin);
                String vin = txtvin.getText().toString();

                TextView txtbrand = (TextView) findViewById(R.id.brand);
                String brand = txtbrand.getText().toString();

                TextView txtyear = (TextView) findViewById(R.id.year);
                String year = txtyear.getText().toString();

                passVehicle.putExtra("model", model);
                passVehicle.putExtra("vin", vin);
                passVehicle.putExtra("brand", brand);
                passVehicle.putExtra("year", year);

                startActivity(passVehicle);

            }
        });
    }

   /* @Override
    public void onBackPressed(){

        TextView txtmodel = (TextView) findViewById(R.id.model);
        TextView txtbrand = (TextView) findViewById(R.id.brand);
        TextView txtyear = (TextView) findViewById(R.id.year);
        TextView txtvin = (TextView) findViewById(R.id.vin);
        Intent i = new Intent();
        i.putExtra("model", txtmodel.getText().toString());
        i.putExtra("brand", txtbrand.getText().toString());
        i.putExtra("year", txtyear.getText().toString());
        i.putExtra("vin", txtvin.getText().toString());
        setResult(99, i);
        finish();
    }*/

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
                    //Sets button enable to true
                    final Button button = (Button) findViewById(R.id.new_drive);
                    button.setEnabled(true);
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
