package com.example.myw;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ListView listView;
    Button add;


    ArrayList<Issue> issueList;
    IssueAdapter adapter;






    Button toggleButton;
    FirebaseAuth mAuth;
    String currentUserId;





    boolean[] showOnlyMyIssues = {false}; // use array to mutate inside onClick

    private void loadAllIssues() {

        DatabaseReference issuesRef = FirebaseDatabase.getInstance().getReference("Issues");

        listView.setAdapter(adapter);
        issuesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                issueList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Issue issue = data.getValue(Issue.class);
                    issueList.add(issue);
                    Log.d("issue" , issue.getFormattedDetails());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Home.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });

        // Clear and reload all data


    }

    private void loadFilteredIssues(String userId) {

        DatabaseReference issuesRef = FirebaseDatabase.getInstance().getReference("Issues");

        listView.setAdapter(adapter);
        issuesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                issueList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Issue issue = data.getValue(Issue.class);

                    if (issue.getAuthor().equals(userId)) {
                        issueList.add(issue);
                    }

                    Log.d("issue" , issue.getFormattedDetails());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Home.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.issue_list);
        add = findViewById(R.id.home_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(Home.this , AddIssueActivity.class);
                startActivity(i);

            }
        });




        toggleButton = findViewById(R.id.home_my_issues);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        issueList = new ArrayList<>();
        adapter = new IssueAdapter(this, issueList);


        DatabaseReference issuesRef = FirebaseDatabase.getInstance().getReference("Issues");

        listView.setAdapter(adapter);
        issuesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                issueList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Issue issue = data.getValue(Issue.class);
                    issueList.add(issue);
                    Log.d("issue" , issue.getFormattedDetails());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Home.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });








        toggleButton.setOnClickListener(v -> {
            showOnlyMyIssues[0] = !showOnlyMyIssues[0];

            if (showOnlyMyIssues[0]) {
                toggleButton.setText("Show All Issues");
                loadFilteredIssues(currentUserId); // shows only current user's issues
            } else {
                toggleButton.setText("Show My Issues");
                loadAllIssues(); // loads all issues
            }
        });




//        List<Issue> issueList = new ArrayList<>();
//
//
//        DatabaseReference quotesRef = FirebaseDatabase.getInstance().getReference("Issues");
//
//        quotesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Issue quote = snapshot.getValue(Issue.class);
//
//                    // Do something with the data (e.g., log or display)
//                    Log.d("Quote", "Text: " + quote.text + " | Author: " + quote.author);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w("FirebaseError", "loadQuotes:onCancelled", databaseError.toException());
//            }
//        });


//
//        issueList.add(new Issue(
//                "Crash on Startup", "99090", "High", "Alice", "Open", "120", "5",
//                "The app crashes immediately upon launching on Android 12 devices."
//        ));
//
//        issueList.add(new Issue(
//                "UI Elements Misaligned", "99091", "Low", "Bob", "Resolved", "30", "2",
//                "Layout breaks on smaller screen devices."
//        ));
//
//        issueList.add(new Issue(
//                "Login Delay", "99092", "Medium", "Carlos", "Open", "60", "7",
//                "It takes too long to log in with Google authentication."
//        ));
//
//        issueList.add(new Issue(
//                "Missing Error Messages", "99093", "Medium", "Dana", "In Progress", "25", "1",
//                "Form validation errors are not being shown to the user."
//        ));
//
//        issueList.add(new Issue(
//                "Theme Not Switching", "99094", "Low", "Elena", "Closed", "15", "0",
//                "Switching between light and dark theme doesn't reflect immediately."
//        ));
//
//        issueList.add(new Issue(
//                "Search Not Filtering", "99095", "High", "Frank", "Open", "90", "12",
//                "Search bar does not filter results as expected."
//        ));
//
//        issueList.add(new Issue(
//                "App Freezes on Scroll", "99096", "Medium", "Grace", "In Review", "55", "3",
//                "Scrolling through a long list causes occasional freezing."
//        ));
//
//        issueList.add(new Issue(
//                "Incorrect Profile Image", "99097", "Low", "Hank", "Resolved", "12", "1",
//                "Profile pictures appear pixelated after upload."
//        ));
//
//        issueList.add(new Issue(
//                "Push Notifications Delayed", "99098", "High", "Ivy", "Open", "110", "6",
//                "Notifications take several minutes to arrive."
//        ));
//
//        issueList.add(new Issue(
//                "Data Sync Issues", "99099", "Medium", "Jack", "In Progress", "48", "4",
//                "User data does not sync correctly between sessions."
//        ));








    }









}