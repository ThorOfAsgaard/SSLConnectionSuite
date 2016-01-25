package asgaardianworkshop.com.sslconnectiontest;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by thor@asgaardianworkshop.com on 1/25/2016.
 */
public class CustomKeyStoreSSLConnection {
    static KeyStore trusted = null;
    String TRUSTSTORE_PASSWORD = "ChangeMe";
    Context mContext;
    TrustManagerFactory tmf;
    SSLContext c = null;

    protected CustomKeyStoreSSLConnection(Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, KeyManagementException {
        mContext = context;

        InputStream is = context.getResources().openRawResource(R.raw.keystore); //replaces with whatever you named your keystore
        trusted = KeyStore.getInstance("BKS");
        trusted.load(is, TRUSTSTORE_PASSWORD.toCharArray());
        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trusted);
        c = SSLContext.getInstance("TLS");
        c.init(null, tmf.getTrustManagers(), null);
        Enumeration<String> aliases = trusted.aliases();
        while (aliases.hasMoreElements()) {
            Certificate cert = trusted.getCertificate(aliases.nextElement());
            Log.d("CERT", cert.getPublicKey().toString());
            Log.d("CERT", cert.getType());


        }

    }

    public HttpsURLConnection returnConnection(URL url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trusted);
        c = SSLContext.getInstance("TLS");
        c.init(null, tmf.getTrustManagers(), null);
        HttpsURLConnection httpsURLConnect = (HttpsURLConnection) url.openConnection();

        httpsURLConnect.setSSLSocketFactory(c.getSocketFactory());
        httpsURLConnect.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {

                try {
                    if (trusted.containsAlias(hostname)) {

                        return true;
                    }
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        try {
            httpsURLConnect.connect();
            Log.d("httpsURLConnection", httpsURLConnect.getCipherSuite());
        } catch (IOException e) {
            httpsURLConnect = null;
            e.printStackTrace();
        }


        return httpsURLConnect;
    }


}
