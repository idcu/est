package ltd.idcu.est.gateway.impl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SslContextFactory {
    private static final String DEFAULT_PROTOCOL = "TLS";

    public static SSLContext createFromKeystore(String keystorePath, String keystorePassword, String keyPassword) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, 
                   CertificateException, UnrecoverableKeyException, KeyManagementException {
        return createFromKeystore(keystorePath, keystorePassword, keyPassword, DEFAULT_PROTOCOL);
    }

    public static SSLContext createFromKeystore(String keystorePath, String keystorePassword, 
                                                  String keyPassword, String protocol) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, 
                   CertificateException, UnrecoverableKeyException, KeyManagementException {
        try (InputStream is = new FileInputStream(keystorePath)) {
            return createFromKeystore(is, keystorePassword, keyPassword, protocol);
        }
    }

    public static SSLContext createFromKeystore(InputStream keystoreStream, String keystorePassword, 
                                                  String keyPassword, String protocol) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, 
                   CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(keystoreStream, keystorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(keyManagers, null, null);
        return sslContext;
    }

    public static SSLContext createWithTrustStore(String keystorePath, String keystorePassword, 
                                                    String keyPassword, String trustStorePath, 
                                                    String trustStorePassword) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, 
                   CertificateException, UnrecoverableKeyException, KeyManagementException {
        return createWithTrustStore(keystorePath, keystorePassword, keyPassword, 
                                    trustStorePath, trustStorePassword, DEFAULT_PROTOCOL);
    }

    public static SSLContext createWithTrustStore(String keystorePath, String keystorePassword, 
                                                    String keyPassword, String trustStorePath, 
                                                    String trustStorePassword, String protocol) 
            throws KeyStoreException, IOException, NoSuchAlgorithmException, 
                   CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream is = new FileInputStream(keystorePath)) {
            keyStore.load(is, keystorePassword.toCharArray());
        }

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream is = new FileInputStream(trustStorePath)) {
            trustStore.load(is, trustStorePassword.toCharArray());
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance(protocol);
        sslContext.init(keyManagers, trustManagers, null);
        return sslContext;
    }
}
