package com.natalie.myapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    EditText name;
    EditText convertTo;
    EditText startingCurr;
    String baseCurrency;
    String currency;
    Double conversionRate = 0.0;
    Double newValue = 0.0;

    String number;

    public void clickFunc(View view) {
        baseCurrency = startingCurr.getText().toString();
        currency = convertTo.getText().toString();
        number = name.getText().toString();
        new RetrieveFeedTask().execute();


        makeToast("processing...");

        //TIMER

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                if(baseCurrency.matches(currency)){
                    makeToast("don't be a wanker");
                } else {
                    makeToast(String.format("%.2f", newValue) + " " + currency);
                }
            }
        }, 2000);
    }

    public void makeToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.myStupidGBP);
        convertTo = (EditText) findViewById(R.id.convertTo);
        startingCurr = (EditText) findViewById(R.id.baseCurrency);


//        new RetrieveFeedTask().execute();

    }


    class RetrieveFeedTask extends AsyncTask {

        protected void onPreExecute() {
//            name.setText("");
        }

        @Override
        protected Object doInBackground(Object[] urls) {
            String API_URL = "http://api.fixer.io/latest?base=" + baseCurrency;
            // Do some validation here
//            Looper.prepare();

            try {
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();


                    String response = stringBuilder.toString();
                    Log.i("INFO", "THIS IS THE RESPONSE: " + response);
                    try {
                        JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                        String requestID = object.getString("base");
//                        Double dollarVal = object.getDouble("USD");
                        JSONObject rates = object.getJSONObject("rates");

                        Double dollarVal = rates.getDouble(currency);
                        conversionRate = dollarVal;
                        Log.i("Info", requestID + " buuuuuutts " + dollarVal);



                        if (number.matches("")) {
                            number = "0";
                        }
                        Double baseValue = Double.parseDouble(number);
                        newValue = baseValue * conversionRate;

//                        makeToast(String.format("%.2f", newValue) + " " + currency);
//                        convertTo.setText(String.format("%.2f", newValue) + " " + currency);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return response;
                } finally {

                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            //            progressBar.setVisibility(View.GONE);
            Log.i("INFO", "THIS IS THE RESPONSE: " + response);
            name.setText(response);
        }
    }


}