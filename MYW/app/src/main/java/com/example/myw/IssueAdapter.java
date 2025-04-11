package com.example.myw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class IssueAdapter extends ArrayAdapter<Issue> {

    Context c;
    public IssueAdapter(Context context, List<Issue> issues) {
        super(context, 0, issues);
        c = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Issue issue = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.issue, parent, false);
        }

        TextView titleView = convertView.findViewById(R.id.issue_title);
        TextView id = convertView.findViewById(R.id.issue_id);
        TextView severityView = convertView.findViewById(R.id.issue_severity);
        TextView authorView = convertView.findViewById(R.id.issue_author);
        TextView statusView = convertView.findViewById(R.id.issue_status);
        TextView upvote = convertView.findViewById(R.id.issue_upvote);
        TextView downvote = convertView.findViewById(R.id.issue_downvote);
        TextView descriptionView = convertView.findViewById(R.id.issue_desc);

        titleView.setText(Objects.requireNonNull(issue).getTitle());
        id.setText(String.format("Id: #%s", issue.getKey()));
        severityView.setText(String.format("Severity: %s", issue.getSeverity()));
        authorView.setText(String.format("Author: %s", issue.getAuthor()));
        statusView.setText(String.format("Status: %s", issue.getStatus()));
        upvote.setText(issue.getUpvote() );
        downvote.setText( issue.getDownvote());
        descriptionView.setText(issue.getDescription());

        String severity = issue.getSeverity().toLowerCase();
        switch (severity) {
            case "high":
                convertView.setBackgroundResource(R.drawable.red); // light red
                break;
            case "medium":
                convertView.setBackgroundResource(R.drawable.orange); // light yellow
                break;
            case "low":
                convertView.setBackgroundResource(R.drawable.yellow); // light green
                break;
            default:
                convertView.setBackgroundColor(Color.WHITE); // default
                break;
        }


        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference issueRef = database.getReference("Issues").child(issue.getKey()); // issueId is the unique key of the issue
//
//                Map<String, Object> updateMap = new HashMap<>();
//                updateMap.put("upvote", issue.getUpvote()+1);
//
//
//
//
//                issueRef.updateChildren(updateMap)
//                        .addOnSuccessListener(aVoid -> {
//                            // Successfully updated
//                            Log.d("FirebaseUpdate", "Status updated successfully");
//                            upvote.setEnabled(false);
//
//                        })
//                        .addOnFailureListener(e -> {
//                            // Failed to update
//                            Log.e("FirebaseUpdate", "Failed to update status", e);
//                        });
            }
        });
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent  = new Intent(c , RequestDetailsActivity.class);
                intent.putExtra("title", issue.getTitle());
                intent.putExtra("category", issue.getCategory());
                intent.putExtra("status", issue.getStatus());
                intent.putExtra("description", issue.getDescription());
                intent.putExtra("id", issue.getKey());
                intent.putExtra("severity", issue.getSeverity());
                intent.putExtra("author", issue.getAuthor());

// Make sure upvotes/downvotes are integers (you can parse them if they're strings)
                intent.putExtra("upvotes", Integer.parseInt(issue.getUpvote()));
                intent.putExtra("downvotes", Integer.parseInt(issue.getDownvote()));


               c.startActivity(intent);


            }
        });


        return convertView;
    }
}
