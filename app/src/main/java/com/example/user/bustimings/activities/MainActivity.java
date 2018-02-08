package com.example.user.bustimings.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.bustimings.R;
import com.example.user.bustimings.webService.Key;
import com.example.user.bustimings.webService.Links;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.*;

public class MainActivity extends AppCompatActivity {
    Links links;
    ListView lvBusStops;
    ArrayList<String> busStops;
    ArrayList<String> busStops2;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        links = new Links();
        String busStopsLink = links.getBusStops();
        lvBusStops = findViewById(R.id.busStops);
        busStops2 = new ArrayList<String>();
        busStops = new ArrayList<String>();
        BusStops busStops = new BusStops();

        busStops.execute(busStopsLink);

//        String busTimingLink = links.getBusArrival("83139");
//        Log.i("MainActivity Link: ", busTimingLink);
//        BusArrival busArrival = new BusArrival();
//        busArrival.execute(busTimingLink);

    }

    public class BusStops extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... URL) {
            Key key = new Key();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL[0]).header("AccountKey", key.GetKey()).header("accept", key.GetAccept()).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.isSuccessful()) {
                Log.i("Retrieve SUCCESS", "Webservice works");
                try {
                    String responseBody = response.body().string();
                    Log.i("Response Body: ", responseBody);
                    return responseBody;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("On Post Excecute: ", s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("value");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject getBusStop = array.getJSONObject(i);
                    String busStopCode = getBusStop.getString("BusStopCode");
                    String busStopname = getBusStop.getString("Description");
                    busStops.add(busStopCode + " " + busStopname);
                    busStops2.add(busStopCode);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Array", busStops.toString());
            adapter = new ArrayAdapter<String>(MainActivity.this, layout.simple_list_item_1, busStops);
            lvBusStops.setAdapter(adapter);

            lvBusStops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.i("Clicked on: ", busStops.get(i));
                    Toast.makeText(MainActivity.this, "Clicked on: " + busStops.get(i), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ActivityBusTiming.class);
                    intent.putExtra("code", busStops2.get(i));
                    startActivity(intent);
                }
            });
        }
    }


}
