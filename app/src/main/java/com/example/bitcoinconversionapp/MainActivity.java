package com.example.bitcoinconversionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private static int usd = 0;
    private static int gbp = 0;
    private static int eur = 0;
    protected Button loadBtn;
    protected EditText editTxt;
    protected TextView nameTxt;
    private RadioGroup radioCurr;
    private RadioButton currButton;
    protected Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTxt = findViewById(R.id.editName);
        nameTxt = findViewById(R.id.nametxt);
        radioCurr = findViewById(R.id.currgrp);
        loadBtn = findViewById(R.id.loader);
        cameraBtn = findViewById(R.id.camera);

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraRecordActivity.class));
            }
        });
        /*
            Take Relevant Currency and Make an API Call for the particular Currency.
            While Checking for internet connection and validity of radio checkbox.
         */
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioCurr.getCheckedRadioButtonId();
                currButton = findViewById(selectedId);
                if (isInternetAvailable(MainActivity.this)) {
                    if (currButton != null) {
                        if (currButton.getText().toString().equals(getString(R.string.USD))) {
                            usd = 1;
                            gbp = 0;
                            eur = 0;
                            toConversion(usd, gbp, eur);
                        }
                        if (currButton.getText().toString().equals(getString(R.string.GBP))) {
                            usd = 0;
                            gbp = 1;
                            eur = 0;
                            toConversion(usd, gbp, eur);
                        }
                        if (currButton.getText().toString().equals(getString(R.string.EUR))) {
                            usd = 0;
                            gbp = 0;
                            eur = 1;
                            toConversion(usd, gbp, eur);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Select An Option To Proceed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
            Read from Shared Preference File if it exists.
         */
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.pref_name), 0);

        String nameOfUser = sharedPref.getString("name", "UnNamedPerson");
        assert nameOfUser != null;
        if (nameOfUser.equals("UnNamedPerson")) {
            restart();
        } else {
            nameTxt.setText(getString(R.string.hello) + nameOfUser);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logout1) {
            /*
                Calls the Restart Method.
             */
            restart();
        }


        return super.onOptionsItemSelected(item);
    }

    /*
        Fill Relevant Fields According to User Input and Start the Conversion Activity.
     */
    private void toConversion(int u, int g, int e) {
        Intent next = new Intent(MainActivity.this, ConversionActivity.class);
        next.putExtra("usd", u);
        next.putExtra("gbp", g);
        next.putExtra("eur", e);
        startActivity(next);
        finish();

    }

    /*
        Clear All Shared Preferences and Go to Initial Activity Page.
    */
    private void restart() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.one_time_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
        Intent myIntent = new Intent(MainActivity.this, NameActivity.class);
        startActivity(myIntent);
        finish();
    }

    /*
        Method to Check Internet Connectivity.
     */
    public boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

}
