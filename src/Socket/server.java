/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import sslencript.SSLEncript;

/**
 *
 * @author ricardopazdemiquel
 */
public class server {

    private static server INSTANCE;
    private ServerSocket serverSocket;
    private Socket socket;

    public static server getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new server();
        }
        return INSTANCE;
    }

    private server() {
        try {
            serverSocket = new ServerSocket(6868);
            hilo();

        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    final static ArrayList<Thread> hilos = new ArrayList<>();

    private void hilo() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Socket socket = serverSocket.accept();

                        

//                        OutputStream output = socket.getOutputStream();
//                        PrintWriter writer = new PrintWriter(output, true);
//
//                        writer.println(new Date().toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        
                        String line = reader.readLine();
                       
                        System.out.println( SSLEncript.recivir(line));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        hilos.add(t);
    }

}
