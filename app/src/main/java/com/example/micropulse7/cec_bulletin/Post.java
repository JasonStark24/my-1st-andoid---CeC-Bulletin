package com.example.micropulse7.cec_bulletin;

/**
 * Created by Micropulse7 on 1/18/2017.
 */


/*Getters and Setters*/
//Constructor

public class Post {
    private String Title;
    private String Content;
    private String Image;
    private String Username;
    private String DateTime;

    public Post(){

    }
/*Getters and Setters*/
    //Constructor

    public Post(String Title, String Content, String Image, String Username, String DateTime ){
        this.Title = Title;
        this.Content = Content;
        this.Image = Image;
        this.Username = Username;
        this.DateTime = DateTime;
    }
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

}
