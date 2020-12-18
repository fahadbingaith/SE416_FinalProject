package com.example.se416_finalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Weather_container.MakeWeatherImage(this);

        Button firebase = findViewById(R.id.firebase);
        Button sqlite = findViewById(R.id.sqlite);
        Button weatherReport = findViewById(R.id.weatherReport);

        EditText weatherLocation = findViewById(R.id.weatherLocation);

        weatherReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weatherLocation.getText().length()==0){
                    Toast.makeText(MainActivity.this,"Location empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor e = sp.edit();
                e.putString("city",weatherLocation.getText()+"");
                e.commit();
               recreate();
            }
        });

        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,firebase.class));
            }
        });

        sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SQlite.class));
            }
        });




    }
}
class Weather_container{
    static void MakeWeatherImage(AppCompatActivity a){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a);
        String city =
        sp.getString("city","London");

        RequestQueue queue = Volley.newRequestQueue(a);
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=4d6d4fd81939f6bd03ce9e971038547d&units=metric";

        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONObject("main");

                    SharedPreferences.Editor e=sp.edit();
                    e.putFloat("Temperature",(float)weather.getDouble("temp"));
                    e.putFloat("Humidity",(float)weather.getDouble("humidity"));
                    e.putInt("Clouds",response.getJSONObject("clouds").getInt("all"));
                    e.commit();


                    ActionBar actionBar = a.getSupportActionBar();
                    actionBar.setDisplayShowCustomEnabled(true);

                    LayoutInflater inflator = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflator.inflate(R.layout.weather_icon, null);
                    v.findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(a,"Temperature"+sp.getFloat("Temperature",0)+"\n"+
                                    "Humidity"+sp.getFloat("Humidity",0)+"\n"+
                                            "Clouds"+sp.getInt("Clouds",0)+"\n",
                                    Toast.LENGTH_LONG).show();

                        }
                    });
                    actionBar.setCustomView(v);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(a,"A problem has occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(jsonObj);


    }

}