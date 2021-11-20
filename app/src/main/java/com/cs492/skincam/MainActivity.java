package com.cs492.skincam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;

public class MainActivity extends AppCompatActivity {

    final private static String TAG = "GILBOMI";

    Button btn_cam;
    Button btn_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cam = (Button) findViewById(R.id.btn_camera);
        btn_gallery = (Button) findViewById(R.id.btn_gallery);

        btn_cam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.btn_camera:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                Log.d(TAG, "권한 설정 완료");
                            }
                            else{
                                Log.d(TAG, "권한 설정 요청");
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        }
                        Intent photoIntent = new Intent(getApplicationContext(), CameraActivity.class);
                        startActivity(photoIntent);
                        break;
                }
            }
        });
    }

}
