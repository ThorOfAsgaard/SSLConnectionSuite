package examples.asgaardianworkshop.com.sslconnectiontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    HttpsURLConnection httpsURLConnection = null;
    URL url = null;
    String urlString = "https://www.google.com";
    String connectionMessage;
    TextView connectionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionStatus = (TextView) findViewById(R.id.connection_status);
        try {
            url = new URL(urlString);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionStatus.setText(urlString + ":" + String.valueOf(httpsURLConnection != null));
    }
}
