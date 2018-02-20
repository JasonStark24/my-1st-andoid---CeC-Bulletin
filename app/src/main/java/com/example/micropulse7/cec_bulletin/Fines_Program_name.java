package com.example.micropulse7.cec_bulletin;

import java.util.Date;

/**
 * Created by jason on 16/02/2017.
 */

public class Fines_Program_name {


    private String Fines_Description;
    private String Program_name;
    private long finesTime;

    public Fines_Program_name(String Fines_Description, String Program_name) {
        this.Fines_Description = Fines_Description;
        this.Program_name = Program_name;

        // Initialize to current time
        finesTime = new Date().getTime();
    }

    public Fines_Program_name(){


    }


    public String getFines_Description() {
        return Fines_Description;
    }

    public void setFines_Description(String Fines_Description) {
        this.Fines_Description = Fines_Description;
    }

    public String getProgram_name() {
        return Program_name;
    }

    public void setProgram_name(String Program_name) {
        this.Program_name = Program_name;
    }

    public long getfinesTime() {
        return finesTime;
    }

    public void setfinesTime(long finesTime) {
        this.finesTime = finesTime;
    }

}
