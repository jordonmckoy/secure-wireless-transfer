package com.jamlabs.SWT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.security.Security;

/**
 * Created by Jordon McKoy on 16/04/16.
 */
public class ServerActivity extends AppCompatActivity {
    public static final String FILE_URI = "FILE_URI";
    public static TextView connectionStage;
    public static Button backButton;
    private Intent intent;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String fileUri = null;

        connectionStage = (TextView) findViewById(R.id.connectionStage);

        backButton = (Button) findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(intent);
                Intent home = new Intent(ServerActivity.this,MainActivity.class);
                startActivity(home);
            }
        });

        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.getExtras()!= null) {
            fileUri = receivedIntent.getExtras().getString(FILE_URI);
        }

        if (fileUri != null) {
            intent = new Intent(this,Server.class);
            intent.putExtra(FILE_URI,fileUri);
            startService(intent);
        } else {
            Log.i("ServerActivity","File uri is NULL");
        }

    }

}
