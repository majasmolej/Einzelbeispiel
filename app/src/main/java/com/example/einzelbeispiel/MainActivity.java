package com.example.einzelbeispiel;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNumber;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        editTextNumber = findViewById(R.id.editTextNumber);
        button = findViewById(R.id.button);

        // Set click listener for button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer();
            }
        });

    }

    private void sendMessageToServer() {
        String studentID = editTextNumber.getText().toString();

        if(studentID.isEmpty()){
            Toast.makeText(this, "Bitte geben Sie Ihre Matrikelnummer ein.", Toast.LENGTH_SHORT).show();
        }

        ServerCommunication.sendMessageToServer(studentID, new ServerCommunication.ServerResponseListener() {
            @Override
            public void onResponseReceived(String response) {
                Toast.makeText(MainActivity.this, "Server response: " + response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        

    }
}