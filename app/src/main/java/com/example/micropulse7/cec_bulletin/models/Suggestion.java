package com.example.micropulse7.cec_bulletin.models;

/**
 * Created by jason on 14/03/2017.
 */

public class Suggestion {


    private String Content;
    private String DateTime;
    private String UserID;
    private String Username;

    public Suggestion(){

    }

    public Suggestion(String Content, String DateTime, String UserID, String Username){
        this.Content = Content;
        this.DateTime = DateTime;
        this.UserID = UserID;
        this.Username = Username;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
