package com.example.micropulse7.cec_bulletin;

import java.util.Date;

/**
 * Created by Micropulse7 on 1/26/2017.
 */

public class Message {

    private String messageText;
    private String messageName;
    //private String image_profile;
    private long messageTime;

    public Message(String messageText, String messageName) {
        this.messageText = messageText;
        this.messageName = messageName;
       // this.image_profile = image_profile;
        // Initialize to current time
        messageTime = new Date().getTime();
    }



    public Message(){

    }

    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    /*public String getimage_profile() {
        return image_profile;
    }

    public void setimage_profile(String image_profile) {
        this.image_profile = image_profile;
    }*/
    public String getMessageEmail() {
        return messageName;
    }

    public void setMessageEmail(String messageEmail) {
        this.messageName = messageEmail;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}