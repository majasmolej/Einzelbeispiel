package com.example.einzelbeispiel;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerCommunication {

    private static final String SERVER_ADDRESS = "se2-submission.aau.at";
    private static final int SERVER_PORT = 20080;

    public interface ServerResponseListener {
        void onResponseReceived(String response);
        void onError(String errorMessage);
    }

    public static void sendMessageToServer(String studentID, ServerResponseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(studentID);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String response = in.readLine();

                    socket.close();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponseReceived(response);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Invoke the listener on the main thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError("Error communicating with server");
                        }
                    });
                }
            }
        }).start();
    }
}
