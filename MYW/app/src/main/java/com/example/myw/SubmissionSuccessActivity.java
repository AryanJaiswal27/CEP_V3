package com.example.myw;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class SubmissionSuccessActivity extends AppCompatActivity {

    private TextView tvComplaintID;
    private Button btnRegisterAnother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_success);

        tvComplaintID = findViewById(R.id.tvComplaintID);
        btnRegisterAnother = findViewById(R.id.btnRegisterAnother);

        // 1. Get the complaint ID from the intent, if you passed it from ReviewComplaintActivity
        //    Otherwise, generate a random ID here
        String complaintId = getIntent().getStringExtra("complaintId");
        if (complaintId == null || complaintId.isEmpty()) {
            // Generate a random complaint ID
            complaintId = generateComplaintId();
        }

        // 2. Display the complaint ID
        tvComplaintID.setText("Complaint ID: " + complaintId);

        // 3. Button to go back to main complaint page
        btnRegisterAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Option 1: Start a new MainActivity
                Intent intent = new Intent(SubmissionSuccessActivity.this, Home.class);
                // Optionally, clear the back stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private String generateComplaintId() {
        // Simple random ID generator: #CMP-<4 digit random number>
        int randomNum = new Random().nextInt(9000) + 1000; // from 1000 to 9999
        return "#CMP-" + randomNum;
    }
}