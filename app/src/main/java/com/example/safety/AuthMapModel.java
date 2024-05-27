package com.example.safety;

public class AuthMapModel {
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Profile , City , RoadName , Coordinatel , UserName,Status;

    public AuthMapModel(String profile, String city, String roadName, String coordinatel, String userName,String staus) {
        Profile = profile;
        City = city;
        RoadName = roadName;
        Coordinatel = coordinatel;
        UserName = userName;
        Status = staus;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getRoadName() {
        return RoadName;
    }

    public void setRoadName(String roadName) {
        RoadName = roadName;
    }

    public String getCoordinatel() {
        return Coordinatel;
    }

    public void setCoordinatel(String coordinatel) {
        Coordinatel = coordinatel;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
