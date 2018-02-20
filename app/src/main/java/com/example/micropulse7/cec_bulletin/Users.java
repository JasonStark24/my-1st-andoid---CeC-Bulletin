package com.example.micropulse7.cec_bulletin;

/**
 * Created by Micropulse7 on 2/5/2017.
 */
public class Users {
    private String Name;
    private String IdNumber;
    private String ProfImage;
    private String Course;
    private String YearLevel;
    private String Gender;
    private String Address;
    private String Birthday;
    private String Account_type;

    public Users(){

    }

    public Users(String Name, String IdNumber, String ProfImage, String Course, String YearLevel, String Address, String Birthday, String Gender, String Account_type ){
        this.Name = Name;
        this.IdNumber = IdNumber;
        this.ProfImage = ProfImage;
        this.Course = Course;
        //this.YearLevel = YearLevel;
        //this.Address = Address;
        //this.Birthday = Birthday;
        this.Gender = Gender;
        this.Account_type = Account_type;
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
    /***
    public String getYearLevel() {
        return YearLevel;
    }

    public void setYearLevel(String yearLevel) {
        YearLevel = yearLevel;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
    ***/

    public String getGender() {
        return Gender;
    }

    public void setSex(String gender) {
        Gender = gender;
    }

    public String getAccount_type() {
        return Account_type;
    }

    public void setAccount_type(String acct_type) {
        Account_type = acct_type;
    }

}
