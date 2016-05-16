Secure Wireless Transfer
-------------

A demo project that transfers files securely over a TLS connection from an Android application to a workstation running the Java client.

The Android app and Java client use self-signed certificates when establishing the TLS connection. They can be found in the raw resource folder on the server and in the SWT_Client folder on the client. 

The included keystores are:

swt_ecv3/1 -> Public Key: EC P-256, Signature Algorithm: SHA256withECDSA

swt_rsa -> Public Key: RSA 4096, Signature Algorithm: SHA256withRSA

Instructions
-------------

Connect your Android phone to your workstation. Turn on a hotspot and connect your workstation to the network.

Open the server folder in Android Studio and run the project on the connected device.

Select a single file to transfer and launch the server.

Open the client folder in Netbeans and run SWT_Client.java. Select a location, name, and extension to save the file.

After the file transfer is complete, stop running the app in Android Studio.


Known Limitations
------------------

The application currently only supports the transfer of a signle file.

With larger file transfers, a broken pipe error occurs.

The keystore passwords should be moved to environment variables or a more secure means of storage.

No status updates available in the Android app


http://www.syntacsstudios.com/

http://www.jordonmckoy.ca/
