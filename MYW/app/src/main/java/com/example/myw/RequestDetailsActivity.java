package com.example.myw;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RequestDetailsActivity extends AppCompatActivity {

    TextView titleText, categoryText, statusText, descriptionText,
            idText, severityText, authorText, upvotesText, downvotesText;
    //LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_details);

        // Initialize views
        titleText = findViewById(R.id.titleText);
        categoryText = findViewById(R.id.categoryText);
        statusText = findViewById(R.id.statusText);
        descriptionText = findViewById(R.id.descriptionText);
        idText = findViewById(R.id.idText);
        severityText = findViewById(R.id.severityText);
        authorText = findViewById(R.id.authorText);
        upvotesText = findViewById(R.id.upvotesText);
        downvotesText = findViewById(R.id.downvotesText);
      //  imageContainer = findViewById(R.id.imageContainer);

        ScrollView convertView;
        convertView = findViewById(R.id.background);



        // Receive data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            titleText.setText(intent.getStringExtra("title"));
            categoryText.setText("Category : " +intent.getStringExtra("category"));
            statusText.setText("Status : " +intent.getStringExtra("status"));
            descriptionText.setText(intent.getStringExtra("description"));
            idText.setText("ID: " + intent.getStringExtra("id"));
            severityText.setText("Severity: " + intent.getStringExtra("severity"));
            authorText.setText("Author: " + intent.getStringExtra("author"));
            upvotesText.setText("Upvotes: " + intent.getIntExtra("upvotes", 0));
            downvotesText.setText("Downvotes: " + intent.getIntExtra("downvotes", 0));

            String severity =intent.getStringExtra("severity").toString().toLowerCase();
            switch (severity) {
                case "high":
                    convertView.setBackgroundColor(Color.rgb(255, 214, 214)); // light red
                    break;
                case "medium":
                    convertView.setBackgroundColor(Color.rgb(255, 211, 150)); // light yellow
                    break;
                case "low":
                    convertView.setBackgroundColor(Color.rgb(252, 255, 212)); // light green
                    break;
                default:
                    convertView.setBackgroundColor(Color.WHITE); // default
                    break;
            }

            // Load image URLs into horizontal scroll
            ArrayList<String> imageUrls = intent.getStringArrayListExtra("images");
            if (imageUrls != null) {
                for (String url : imageUrls) {
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            400, 400);
                    layoutParams.setMargins(8, 0, 8, 0);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Glide.with(this).load(url).into(imageView);
                    //imageContainer.addView(imageView);
                }
            }
        }
    }
}
