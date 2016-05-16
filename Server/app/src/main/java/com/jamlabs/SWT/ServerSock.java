package com.jamlabs.SWT;

import android.content.Context;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 * Created by Jordon McKoy on 16/04/16.
 */
public class ServerSock extends Thread {
    private SSLSocket sock = null;
    Context context;

    public ServerSock(Context context, SSLSocket socket) throws Exception {
        this.context = context;
        this.sock = socket;
    }

    public void run() {
        try {
            byte[] clientResponse = new byte[1000];
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream os = new DataOutputStream(sock.getOutputStream());

            Log.i("ServerSock","File name bytes " + Server.fileName.toString());
            Log.i("ServerSock","File name " + new String(Server.fileName,"UTF-8"));
            Log.i("ServerSock","File name length " + Server.fileName.length);
            Log.i("ServerSock","File size " + Server.fileData.length);

            os.write(Server.fileName);
            os.flush();

            os.writeInt(Server.fileData.length);
            os.flush();

            os.write(Server.fileData);
            os.flush();
            Log.i("ServerSock", "write complete");

            in.read(clientResponse);
            Log.i("ServerSock", new String(clientResponse,"UTF-8"));

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
