package com.example.user.bustimings.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

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

public class ActivityBusTiming extends AppCompatActivity {
    TextView busTiming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_timing);



        busTiming = findViewById(R.id.busDetails);
        Intent intent = getIntent();
        String code = intent.getStringExtra("code");

        Links links = new Links();
        String busTimingLink = links.getBusArrival(code);
        Log.i("MainActivity Link: ", busTimingLink);
        BusArrival busArrival = new BusArrival();
        busArrival.execute(busTimingLink);


    }

    public class BusArrival extends AsyncTask<String, Void, String> {

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
            StringBuilder builder = new StringBuilder();
            Log.i("On Post Excecute: ", s);
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("Services");
                for(int i = 0; i < array.length(); i++){
                    JSONObject getBusObject = array.getJSONObject(i);
                    String busNo = getBusObject.getString("ServiceNo");
                    JSONObject busTiming1 = getBusObject.getJSONObject("NextBus");
                    String busTime1 = busTiming1.getString("EstimatedArrival");
                    JSONObject busTiming2 = getBusObject.getJSONObject("NextBus2");
                    String busTime2 = busTiming2.getString("EstimatedArrival");
                    builder.append("Bus Number: ").append(busNo).append("\n  ");
                    builder.append("First Bus: ").append(busTime1).append("\n");
                    builder.append("Next Bus: ").append(busTime2).append("\n\n");
                    busTiming.setText(builder.toString());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
