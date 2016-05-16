package com.jamlabs.SWT;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.nononsenseapps.filepicker.FilePickerActivity;

/**
 * Created by Jordon McKoy on 16/04/16.
 */
public class MainActivity extends AppCompatActivity {
    int FILE_CODE = 4848;
    Button startServerBtn, startFileUploadBtn;
    String fileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFileUploadBtn = (Button) findViewById(R.id.fileBtn);
        startFileUploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                startActivityForResult(i, FILE_CODE);
            }
        });

        startServerBtn = (Button) findViewById(R.id.servBtn);
        startServerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fileUri != null) {
                    Intent intent = new Intent(getApplicationContext(), ServerActivity.class);
                    intent.putExtra(ServerActivity.FILE_URI,fileUri);
                    startActivity(intent);
                } else {
                    Log.i("Main","file uri is null");
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            fileUri = uri.toString();
            startServerBtn.setEnabled(true);
        }
    }
}

