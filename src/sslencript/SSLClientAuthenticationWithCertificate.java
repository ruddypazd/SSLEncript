/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sslencript;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author ricardopazdemiquel
 */
public class SSLClientAuthenticationWithCertificate {
    private static final String HOST = "YOUR_SERVER_URL";

    public static void main(String[] args) throws Exception {

        System.setProperty("javax.net.debug", "ssl,handshake");

        System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
        System.setProperty("javax.net.ssl.keyStore", "/PATH/TO/YOUR/KEYSTORE.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "THE_KEYSTORE_PASSWORD");

        final URL url = new URL(HOST);
        HttpURLConnection conection = (HttpURLConnection)url.openConnection();

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();
        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }
        bufferedReader.close();
        System.out.println(response.toString());
    }
}
