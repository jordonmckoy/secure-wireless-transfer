package SWT_Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.Security;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.JFileChooser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author Jordon McKoy
 */
public class SWT_Client {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Generate our key store
        KeyStore keyStore = KeyStore.getInstance("bks");
        keyStore.load(SWT_Client.class.getResourceAsStream("./swt_ecv3.bks"),"jordon".toCharArray());
        
        // Create a custom trust manager that accepts the server self-signed certificate
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Create the SSLContext for the SSLSocket to use
        SSLContext sslctx = SSLContext.getInstance("TLS");
        sslctx.init(null, trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory factory = sslctx.getSocketFactory();

        // Create socket on default Android IP address
        SSLSocket client = (SSLSocket) factory.createSocket("192.168.43.1", 3000);

  //      String[] suites = {"TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA"};
  //      client.setEnabledCipherSuites(suites);
        String[] pro = {"TLSv1.2"};
        client.setEnabledProtocols(pro);

        // Print system information
        System.out.println("Connected to server " + client.getInetAddress() + ": " + client.getPort());

        // Writer and Reader
        byte[] fileNameBytes = new byte[100];
        String fileName, fileExt;
        int fileSize;
        byte[] fileBytes;
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        // Read in file name and size
        in.read(fileNameBytes);
        fileSize = in.readInt();

        // Convert file name from byte[] to string
        fileName = new String(fileNameBytes,"UTF-8");
        fileExt = fileName.substring(fileName.lastIndexOf('.')+1);

        // Initialize new file byte[] with appropriate size
        fileBytes = new byte[fileSize];

        // Log info
        System.out.println("File name: " + fileName);
        System.out.println("File extension " + fileExt);
        System.out.println("File size: " + fileSize);

        // Open save file dialog
        JFileChooser fileChooser = new JFileChooser();
//        int chooserRes = JFileChooser.CANCEL_OPTION;
//        chooserRes = fileChooser.showSaveDialog(null);
        int chooserRes = fileChooser.showSaveDialog(null);
        if (chooserRes == JFileChooser.APPROVE_OPTION) {
            try {
                // Read in encrypted file
                in.read(fileBytes);
                System.out.println("Encrypted file size: " + fileBytes.length);

                // Write data to a file
                FileOutputStream fs = null;
                File file;
                try {
                    if (String.valueOf(fileChooser.getSelectedFile()).indexOf('.') == -1) {
                        System.out.println("File path: " + fileChooser.getSelectedFile()+"."+fileExt);
                        file = new File(String.valueOf(fileChooser.getSelectedFile()+"."+fileExt).trim());
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fs = new FileOutputStream(file,false);
                    } else {
                        System.out.println("File path: " + fileChooser.getSelectedFile());
                        file = new File(String.valueOf(fileChooser.getSelectedFile()).trim());
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fs = new FileOutputStream(file,false);
                    }

                    fs.write(fileBytes);
                    fs.flush();
                } finally {
                    if (fs != null) {
                        fs.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       
        System.out.println("File transfer complete.");
        byte[] confirmMsg = "File received!".getBytes();
        out.write(confirmMsg);
        out.flush();
    }
}
