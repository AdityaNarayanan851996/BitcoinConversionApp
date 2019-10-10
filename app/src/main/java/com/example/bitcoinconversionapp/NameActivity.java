package com.example.bitcoinconversionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        editText = (findViewById(R.id.editName));
        Button button = (findViewById(R.id.button));
        /*
            Create A Shared Preference File to save Name.
         */
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.one_time_pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        if (mSharedPreferences.getBoolean("isfirstTime", true)) {
            mEditor.putBoolean("isfirstTime", false);
            mEditor.apply();
            mEditor.commit();


        } else {
            /*
                If the Name has already been saved call the Main Activity.
             */
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }

        /*
            Continue to Main Activity while saving the name.
         */
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NameActivity.this, MainActivity.class);
                myIntent.putExtra("name", editText.getText().toString());
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                if (editText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter A Proper Name", Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("name", editText.getText().toString());
                    editor.apply();
                    startActivity(myIntent);
                    finish();
                }

            }
        });
    }
}
