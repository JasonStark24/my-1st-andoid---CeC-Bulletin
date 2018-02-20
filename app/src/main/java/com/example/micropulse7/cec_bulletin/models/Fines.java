package com.example.micropulse7.cec_bulletin.models;

/**
 * Created by jason on 11/03/2017.
 */

public class Fines {

    private String FinesProgram;
    private String FinesValue;
    private String DateTime;
    private String Username;

    public Fines(){

    }

    public Fines(String username, String finesProg, String finesVal, String dateTime) {
        FinesProgram = finesProg;
        FinesValue = finesVal;
        DateTime = dateTime;
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFinesProgram() {
        return FinesProgram;
    }

    public void setFinesProgram(String finesProg) {
        FinesProgram = finesProg;
    }

    public String getFinesValue() {
        return FinesValue;
    }

    public void setFinesValue(String finesVal) {
        FinesValue = finesVal;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

}
