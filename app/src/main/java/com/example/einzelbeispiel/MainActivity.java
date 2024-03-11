package com.example.einzelbeispiel;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
    private TextView textViewResponse;
    private Button buttonSendToServer;
    private Button buttonDigitSum;

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
        buttonSendToServer = findViewById(R.id.button);
        buttonDigitSum = findViewById(R.id.button2);
        textViewResponse = findViewById(R.id.textView2);


        // Set click listener for button
        buttonSendToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToServer();
            }
        });

        buttonDigitSum.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                calculateDigitSum();
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
                String responseText = getString(R.string.server_response_text, response);
                textViewResponse.setText(responseText);
                //Toast.makeText(MainActivity.this, "Server response: " + response, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void calculateDigitSum(){
        String studentID = editTextNumber.getText().toString();

        if(studentID.isEmpty()){
            Toast.makeText(this, "Bitte geben Sie Ihre Matrikelnummer ein.", Toast.LENGTH_SHORT).show();
        }

        if(isCorrectLength(studentID)){
            int sum = 0;
            for(int i = 0; i < studentID.length(); i++){
                char digitChar = studentID.charAt(i);
                int digitValue = Character.getNumericValue(digitChar);
                sum += digitValue;
            }

            convertToBinary(sum);

        }else {
            Toast.makeText(this, "Not a valid matriculation number.", Toast.LENGTH_SHORT).show();
        }
    }

    private void convertToBinary(int sum){
        String binaryString = Integer.toBinaryString(sum);
        //textViewResponse.setText(binaryString);
        Toast.makeText(this, "The digit sum represented in binary is: " + binaryString, Toast.LENGTH_LONG).show();
    }

    public static boolean isCorrectLength(String input) {
        return input.length() == 8;
    }

}