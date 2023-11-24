package com.example.mcamera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Uri> takePictureLaucher;

    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    private Uri uriimage;

    public void checkPermissoes() {
        //Checando as permissões foram concedidas ou as solicitando ao usuário
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, PackageManager.PERMISSION_GRANTED);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissoes();
        takePictureLaucher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {

                ImageView img = findViewById(R.id.imageView);
                img.setImageURI(uriimage);
            } else {


            }
        });

        findViewById(R.id.bntCamera).setOnClickListener(v -> {

        });
    }

    public void takePicture() {
        try {
            String timestamp = new SimpleDateFormat("YYYYmm_HHmmss").format(new Date());
            String imageFileName = "JPEG" + timestamp + "";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFilename = File.createTempFile(imageFileName, "jpg", storageDir);
            uriimage = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".Provider", imageFilename);
            takePictureLaucher.launch(uriimage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}