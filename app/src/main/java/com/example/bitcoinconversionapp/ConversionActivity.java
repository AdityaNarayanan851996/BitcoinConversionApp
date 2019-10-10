package com.example.bitcoinconversionapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConversionActivity extends AppCompatActivity {
    private static final String BTC_ENDPOINT = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static final String TAG = "ConversionActivity";
    private TextView conversionTxt;
    private ProgressDialog progressDialog;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        conversionTxt = findViewById(R.id.conversiontxt);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait ...");
        Intent i = getIntent();
        /*
            Extract relevant variables from the Intent.
         */
        int usd = i.getIntExtra("usd", 0);
        int gbp = i.getIntExtra("gbp", 0);
        int eur = i.getIntExtra("eur", 0);
        load(usd, gbp, eur);

    }

    /*
        Whenever User Minimises at this Activity, it should finish and will take back to
        Main Activity.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conv_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.back) {

            toMainActivity();

        }


        return super.onOptionsItemSelected(item);
    }
    /*
        from okHTTP library to parse the URL
        https://square.github.io/okhttp/
     */

    private void load(final int u, final int g, final int e) {

        Request request = new Request.Builder()
                .url(BTC_ENDPOINT)
                .build();

        progressDialog.show();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ConversionActivity.this, "Error during loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                assert response.body() != null;
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        parseBpiResponse(body, u, g, e);
                    }
                });
            }
        });

    }

    /*
        To Parse the JSON Response.
     */
    private void parseBpiResponse(String body, int u, int g, int e) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);
            builder.append("Following is the rate:").append("\n\n");
            builder.append("").append("\n\n");

            JSONObject timeObject = jsonObject.getJSONObject("time");
            builder.append("Time: ").append(timeObject.getString("updated")).append("\n\n");

            JSONObject bpiObject = jsonObject.getJSONObject("bpi");
            if (u == 1) {
                JSONObject usdObject = bpiObject.getJSONObject("USD");
                builder.append("Rate: ").append(usdObject.getString("rate")).append("$").append("\n");
            }
            if (g == 1) {
                JSONObject gbpObject = bpiObject.getJSONObject("GBP");
                builder.append("Rate: ").append(gbpObject.getString("rate")).append("£").append("\n");
            }
            if (e == 1) {
                JSONObject euroObject = bpiObject.getJSONObject("EUR");
                builder.append("Rate: ").append(euroObject.getString("rate")).append("€").append("\n");
            }


            conversionTxt.setText(builder.toString());


        } catch (Exception ex) {
            Toast.makeText(this, "Error Occurred! Try Again", Toast.LENGTH_SHORT).show();

        }
    }

    private void toMainActivity() {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.one_time_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        Intent myIntent = new Intent(ConversionActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}
