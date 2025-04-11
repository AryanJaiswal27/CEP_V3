package com.example.myw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddIssueActivity extends AppCompatActivity {

    // UI References
    private EditText etIssueTitle, etIssueDescription, etIssueLocation, etContactNumber;
    private Spinner spinnerCategory, spinnerSeverity;
    private Button btnUploadProof, btnProceed;
    private TextView tvNoMedia;
    private RelativeLayout rlImagePreview;
    private ImageButton btnLeft, btnRight;
    private ViewPager2 viewPagerImages;

    // Image picking
    private static final int PICK_IMAGES_REQUEST_CODE = 101;
    private ArrayList<Uri> selectedMediaUris = new ArrayList<>();
    private ImagePagerAdapter imagePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue); // activity_main.xml

        // Initialize UI components
        etIssueTitle = findViewById(R.id.etIssueTitle);
        etIssueDescription = findViewById(R.id.etIssueDescription);
        etIssueLocation = findViewById(R.id.etIssueLocation);
        etContactNumber = findViewById(R.id.etContactNumber);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSeverity = findViewById(R.id.spinnerSeverity);

        btnUploadProof = findViewById(R.id.btnUploadProof);
        btnProceed = findViewById(R.id.btnProceed);
        tvNoMedia = findViewById(R.id.tvNoMedia);

        rlImagePreview = findViewById(R.id.rlImagePreview);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        viewPagerImages = findViewById(R.id.viewPagerImages);

        // 1. Set up Category Spinner
        List<String> categories = Arrays.asList(
                "Garbage Collection", "Noise Pollution", "Street Lighting", "Road Maintenance", "Water Supply", "Sewage Issues", "Public Transport", "Traffic Issues", "Illegal Construction", "Public Property Damage", "Air Pollution", "Stray Animals", "Illegal Dumping", "Public Toilets", "Electricity Supply", "Other"
        );
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // 2. Set up Severity Spinner
        List<String> severities = Arrays.asList("Low", "Medium", "High");
        ArrayAdapter<String> severityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                severities
        );
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSeverity.setAdapter(severityAdapter);

        // 3. Set up ViewPager2 adapter
        imagePagerAdapter = new ImagePagerAdapter(selectedMediaUris);
        viewPagerImages.setAdapter(imagePagerAdapter);

        // 4. Handle the "Add Media" button click
        btnUploadProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the gallery picker (for images/videos)
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*"); // or "image/* video/*" if you want images & videos
                startActivityForResult(intent, PICK_IMAGES_REQUEST_CODE);
            }
        });

        // 5. Left arrow
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPagerImages.getCurrentItem();
                if (currentItem > 0) {
                    viewPagerImages.setCurrentItem(currentItem - 1, true);
                }
            }
        });

        // 6. Right arrow
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPagerImages.getCurrentItem();
                if (currentItem < selectedMediaUris.size() - 1) {
                    viewPagerImages.setCurrentItem(currentItem + 1, true);
                }
            }
        });

        // 7. Proceed button -> Go to "Review" page
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gather user input
                String title = etIssueTitle.getText().toString().trim();
                String description = etIssueDescription.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();
                String severity = spinnerSeverity.getSelectedItem().toString();
                String location = etIssueLocation.getText().toString().trim();
                String contact = etContactNumber.getText().toString().trim();

                // Validate if you want (e.g. check if fields are empty)
                if (title.isEmpty() || description.isEmpty() || contact.isEmpty() || contact.length()!=10 || location.isEmpty()) {
                    Toast.makeText(AddIssueActivity.this, "Please fill in all mandatory fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(description.length()<50){
                    Toast.makeText(AddIssueActivity.this, "Description Should be Atlest 50 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(location.length()<20){
                    Toast.makeText(AddIssueActivity.this, "Describe the locaton more.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pass data to the next activity (ReviewComplaintActivity)
                Intent intent = new Intent(AddIssueActivity.this, ReviewComplaintActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("category", category);
                intent.putExtra("severity", severity);
                intent.putExtra("location", location);
                intent.putExtra("contact", contact);

                // Optionally, pass the media URIs as well
                // (We'll do it as a string array of URIs for simplicity)
                ArrayList<String> uriStrings = new ArrayList<>();
                for (Uri uri : selectedMediaUris) {
                    uriStrings.add(uri.toString());
                }
                intent.putStringArrayListExtra("mediaUris", uriStrings);

                startActivity(intent);
            }
        });
    }

    // Handle result from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Clear previous selections if needed
            selectedMediaUris.clear();

            if (data.getClipData() != null) {
                // Multiple files selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    selectedMediaUris.add(uri);
                }
            } else if (data.getData() != null) {
                // Single file selected
                Uri uri = data.getData();
                selectedMediaUris.add(uri);
            }

            // Update UI
            imagePagerAdapter.notifyDataSetChanged();
            if (!selectedMediaUris.isEmpty()) {
                tvNoMedia.setVisibility(View.GONE);
                rlImagePreview.setVisibility(View.VISIBLE);

                // Show arrows if more than 1 media
                if (selectedMediaUris.size() > 1) {
                    btnLeft.setVisibility(View.VISIBLE);
                    btnRight.setVisibility(View.VISIBLE);
                } else {
                    btnLeft.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                }
            }
        }
    }
}