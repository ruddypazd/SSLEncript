/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sslencript;

import Socket.server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.Builder;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author ricardopazdemiquel
 */
public class SSLEncript {

    /**
     * @param args the command line arguments
     */
    private static final String CERTIFICATE_ALIAS = "prueba_socket";
    private static final String CERTIFICATE_PASS = "poloclub78";

    private static final String CERTIFICATE_DN = "CN=Ricardo Paz Demiquel,OU=SRL,O=Servisis,L=Santa Cruz de la Sierra,ST=Santa Cruz,C=BO";
    private static final String CERTIFICATE_NAME = "keystore.key";

    public static void main(String[] args) {
        server.getInstance();
//        try {
//            KeyStore keyStore = SSLEvent.createKeyStore(CERTIFICATE_NAME, CERTIFICATE_PASS);
//            Certificate certificate = keyStore.getCertificate(CERTIFICATE_ALIAS);
//            byte[] texto = enviar("Hola Mundo", certificate);
//            
//            Key key = keyStore.getKey(CERTIFICATE_ALIAS, CERTIFICATE_PASS.toCharArray());
//            String text =recivir(texto, key);
//
//            System.out.println(text);
//            
//           
//
//        } catch (Exception ex) {
//            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    public static byte[] enviar(String texto, Certificate certificate) {
        try {
            byte[] text2 = Encrypt(texto, certificate);
            return text2;
        } catch (Exception ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String recivir(String texto) {
        try {
            KeyStore keyStore = SSLEvent.createKeyStore(CERTIFICATE_NAME, CERTIFICATE_PASS);
            Key key = keyStore.getKey(CERTIFICATE_ALIAS, CERTIFICATE_PASS.toCharArray());
            String text2 = Decrypt(stringToBytes(texto), key);
            return text2;
        } catch (Exception ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void crearJKS() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            Certificate certificate = SSLEvent.generateCertificate(CERTIFICATE_DN, keyPair, 365);
            KeyStore keyStore = SSLEvent.createKeyStore(CERTIFICATE_NAME, CERTIFICATE_PASS);
            keyStore.setKeyEntry(CERTIFICATE_ALIAS, keyPair.getPrivate(), CERTIFICATE_PASS.toCharArray(), new Certificate[]{certificate});
            File file = new File(".", CERTIFICATE_NAME);
            keyStore.store(new FileOutputStream(file), CERTIFICATE_PASS.toCharArray());
            System.out.println("it worked!");

        } catch (IOException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SSLEncript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public static byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }

    public static byte[] Encrypt(String text, Certificate certificate) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException, NoSuchProviderException {

        byte[] encryptedBytes;

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
        encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));

        return encryptedBytes;

    }

    public static String Decrypt(byte[] result, Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] decryptedBytes;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedBytes = cipher.doFinal(result);
        return new String(decryptedBytes);

    }

}
