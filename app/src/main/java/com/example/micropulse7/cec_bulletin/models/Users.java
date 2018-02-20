package com.example.micropulse7.cec_bulletin.models;

/**
 * Created by jason on 15/03/2017.
 */

public class Users {




    private String Name;
    private String IdNumber;
    private String ProfImage;
    private String Course;
    private String Uid;
    private String Gender;
    private String Account_type;
    private String Email;

    public Users(){

    }

    public Users(String Uid, String Email, String Name, String IdNumber, String ProfImage, String Course,  String Gender, String Account_type ){
        this.Name = Name;
        this.Uid = Uid;
        this.IdNumber = IdNumber;
        this.ProfImage = ProfImage;
        this.Course = Course;
        this.Gender = Gender;
        this.Account_type = Account_type;
        this.Email = Email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public String getProfImage() {
        return ProfImage;
    }

    public void setProfImage(String profImage) {
        ProfImage = profImage;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAccount_type() {
        return Account_type;
    }

    public void setAccount_type(String acct_type) {
        Account_type = acct_type;
    }
}
