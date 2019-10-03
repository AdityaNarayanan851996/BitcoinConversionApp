package com.example.bitcoinconversionapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class CameraRecordActivity extends AppCompatActivity {

    VideoView videoView;
    Uri videoFileUri;
    Button captureVideoButton;
    Button playVideoButton;
    Button captureWithoutDataVideoButton;
    public static int VIDEO_CAPTURED = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_record);
        captureVideoButton = (Button) this.findViewById(R.id.CaptureVideoButton);
        playVideoButton = (Button) this.findViewById(R.id.PlayVideoButton);
        captureWithoutDataVideoButton = (Button) this.findViewById(R.id.CaptureVideoWithoutDataButton);
        videoView = (VideoView) this.findViewById(R.id.VideoView);
        playVideoButton.setEnabled(false);

        captureVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (ContextCompat.checkSelfPermission(CameraRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ) ||
                        (ContextCompat.checkSelfPermission(CameraRecordActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                                == PackageManager.PERMISSION_GRANTED)){
                    System.out.println((ContextCompat.checkSelfPermission(CameraRecordActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ) );
                    Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    captureVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    startActivityForResult(captureVideoIntent, VIDEO_CAPTURED);
                } else {
                    Toast.makeText(CameraRecordActivity.this, "Click other Button to request permissions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        playVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoURI(videoFileUri);
                videoView.start();
            }
        });
        captureWithoutDataVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(CameraRecordActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(CameraRecordActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {

                    if(ActivityCompat.shouldShowRequestPermissionRationale(CameraRecordActivity.this,
                            Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(CameraRecordActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                         Toast.makeText(CameraRecordActivity.this, "In Progress", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(CameraRecordActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                200);
                    }
                } else{
                    Toast.makeText(CameraRecordActivity.this, "U Already Have Permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == VIDEO_CAPTURED) {
            videoFileUri = data.getData();
            // Gets Video's File Location
            Toast.makeText(this, videoFileUri.getPath(), Toast.LENGTH_LONG).show();
            playVideoButton.setEnabled(true);
        }

    }
}
