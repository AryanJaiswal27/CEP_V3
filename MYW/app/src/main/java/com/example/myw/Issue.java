package com.example.myw;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Issue {
    private String title;
    private String key;
    private String severity;
    private String author;
    private String status;
    private String upvote;
    private String downvote;
    private String description;
    private String category;

    private String contact;
    private String location;


   public  Issue(){

   }

    public Issue(String author,String category ,String contact, String description, String downvote, String key  ,String location, String severity, String status, String title , String upvote) {
        this.title = title;
        this.category = category;
        this.contact = contact;
        this.location = location;

        this.key = key;
        this.severity = severity;
        this.author = author;
        this.status = status;
        this.upvote = upvote;
        this.downvote = downvote;
        this.description = description;
    }

    public String getFormattedDetails() {
        return "📌 Title: " + title + "\n" +
                "🗂️ Category: " + category + "\n" +
                "📍 Location: " + location + "\n" +
                "📞 Contact: " + contact + "\n" +
                "📝 Description: " + description + "\n" +
                "🔥 Severity: " + severity + "\n" +
                "👤 Author: " + author + "\n" +
                "🔐 ID: " + key + "\n" +
                "⬆️ Upvotes: " + upvote + "\n" +
                "⬇️ Downvotes: " + downvote + "\n" +
                "📊 Status: " + status;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public String getKey() {
        return key;
    }

    public String getSeverity() {
        return severity;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public String getUpvote() {
        return upvote;
    }

    public String getDownvote() {
        return downvote;
    }

    public String getDescription() {
        return description;
    }
}
