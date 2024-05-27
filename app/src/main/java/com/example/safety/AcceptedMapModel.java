package com.example.safety;

public class AcceptedMapModel {

    String Profile , UserName , City;

    public AcceptedMapModel(String profile, String userName, String city) {
        Profile = profile;
        UserName = userName;
        City = city;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
