package asgaardianworkshop.com.sslconnectiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class MainActivity extends AppCompatActivity {
    HttpsURLConnection httpsURLConnection = null;
    URL url = null;
    String urlString = "https://www.google.com";
    String connectionMessage = "";
    TextView connectionStatus;
    Button standard;
    Button no_cert;
    Button key_chain;
    Button key_chain_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionStatus = (TextView) findViewById(R.id.connection_status);
        standard = (Button) findViewById(R.id.standard);
        no_cert = (Button) findViewById(R.id.no_cert);
        key_chain = (Button) findViewById(R.id.key_chain);
        key_chain_google = (Button) findViewById(R.id.key_chain_google);

        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    StandardSSL("https://www.google.com");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        no_cert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StandardSSL("https://www.pcwebshop.co.uk/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        key_chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customKeyStore("https://eaport.eains.com:443");

            }
        });
        key_chain_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customKeyStore("https://wwww.google.com:443");

            }

        });
        try {
            url = new URL(urlString);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionStatus.setText("Test Connection to: " + urlString + ":" + String.valueOf(httpsURLConnection != null));
    }

    void customKeyStore(final String urlString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(urlString);
                    CustomKeyStoreSSLConnection cks = new CustomKeyStoreSSLConnection(getApplicationContext());
                    httpsURLConnection = cks.returnConnection(u);
                } catch (MalformedURLException e) {
                    connectionMessage = "Malformed URL";
                    e.printStackTrace();
                } catch (CertificateException e) {
                    connectionMessage = "Certificate Exception";
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    connectionMessage = "No Such Algorithm";
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    connectionMessage = "KeyStore Exception";
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    connectionMessage = "KeyManagementException";
                    e.printStackTrace();
                } catch (IOException e) {
                    connectionMessage = "IOException";
                    e.printStackTrace();
                }
                updateScreen();
            }


        }).start();
    }
    void updateScreen() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String info = "";
                if (httpsURLConnection != null) {
                    try {
                        Certificate[] certs = httpsURLConnection.getServerCertificates();
                        info = "Cipher Suite:" + httpsURLConnection.getCipherSuite() + "\r\n" +
                                "Server Certificate:\r\n" + certs[0].getPublicKey();
                    } catch (SSLPeerUnverifiedException e) {
                        info = "Error Connecting to  " + httpsURLConnection.getURL().toString();
                        e.printStackTrace();
                    }

                } else {
                    info = "FAILURE - unable to establish a connection:\r\n"+connectionMessage;
                }
                connectionStatus.setText(info);
            }
        });


    }

    void StandardSSL(String URLString) throws IOException {
        final URL u = new URL(URLString);


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    httpsURLConnection = (HttpsURLConnection) u.openConnection();
                } catch (IOException e) {
                    httpsURLConnection = null;
                    e.printStackTrace();
                }
                try {
                    httpsURLConnection.connect();
                } catch (IOException e) {
                    httpsURLConnection = null;

                    e.printStackTrace();
                }

                updateScreen();


            }
        }).start();

    }
}
