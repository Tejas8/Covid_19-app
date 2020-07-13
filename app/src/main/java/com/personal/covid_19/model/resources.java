package com.personal.covid_19.model;

public class resources {
    private String category;
    private String city;
    private String contact;
    private String descriptionandorserviceprovided;
    private String nameoftheorganisation;
    private String phonenumber;
    private String state;


    // Getter Methods

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getContact() {
        return contact;
    }

    public String getDescriptionandorserviceprovided() {
        return descriptionandorserviceprovided;
    }

    public String getNameoftheorganisation() {
        return nameoftheorganisation;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getState() {
        return state;
    }

    // Setter Methods

    public void setCategory( String category ) {
        this.category = category;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public void setContact( String contact ) {
        this.contact = contact;
    }

    public void setDescriptionandorserviceprovided( String descriptionandorserviceprovided ) {
        this.descriptionandorserviceprovided = descriptionandorserviceprovided;
    }

    public void setNameoftheorganisation( String nameoftheorganisation ) {
        this.nameoftheorganisation = nameoftheorganisation;
    }

    public void setPhonenumber( String phonenumber ) {
        this.phonenumber = phonenumber;
    }

    public void setState( String state ) {
        this.state = state;
    }
}
