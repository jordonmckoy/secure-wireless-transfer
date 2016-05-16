package com.jamlabs.SWT;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Jordon McKoy on 16/04/16.
 */
public class Server extends IntentService {
    public static final String serverIP = "192.168.43.1"; // default android ip address
    public static final int serverPORT = 3000;

    SSLServerSocket sslServerSocket;
    public static byte[] fileName = null;
    public static byte[] fileData = null;
    String fileUri = null;

    public Server() {
        super("name");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Get file data
        if (intent.getExtras()!= null) {
            fileUri = intent.getExtras().getString(ServerActivity.FILE_URI);
            Log.i("Server","File Uri is here");
            getFileBytes();
        } else {
            Log.i("Server","File Uri is null");
        }

        sslServerSocket = createSSLSS();

        if (sslServerSocket != null) {
            Log.i("SERVER", "server established @ "+String.valueOf(sslServerSocket.getInetAddress()));
            ConnectSocket conn = new ConnectSocket();
            conn.run();
        } else {
            Log.i("SERVER","Server not found");
        }

    }

    public void getFileBytes() {
        String scheme = Uri.parse(fileUri).getScheme();
        if (scheme.equals("file")) {
            fileName = Uri.parse(fileUri).getLastPathSegment().getBytes();
        }
        File file = new File(Uri.parse(fileUri).getPath());

        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024*8];
            int bytesRead = 0;
            while ((bytesRead = is.read(buf)) != -1) {
                bos.write(buf, 0, bytesRead);
            }
            fileData = bos.toByteArray();

            Log.i("BYTE","byte array initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SSLServerSocket createSSLSS() {
        try {
            System.setProperty("https.protocols","TLSv1.2");
            KeyManagerFactory keyManager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            KeyStore keyStore = KeyStore.getInstance("bks","BC");
            keyStore.load(this.getResources().openRawResource(R.raw.swt_ecv3), "jordon".toCharArray());
            keyManager.init(keyStore, "jordon".toCharArray());

            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            trustManager.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManager.getKeyManagers(), null, null);
            SSLServerSocketFactory sslFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) sslFactory.createServerSocket(serverPORT,0,InetAddress.getByName(serverIP));
            String[] pro = {"TLSv1.2"};
            serverSocket.setEnabledProtocols(pro);

            return serverSocket;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class ConnectSocket implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    SSLSocket socket = (SSLSocket) sslServerSocket.accept();
                    (new ServerSock(getApplicationContext(), socket)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
