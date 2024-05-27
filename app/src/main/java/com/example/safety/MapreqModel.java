package com.example.safety;

public class MapreqModel {
    String Profile , City , RoadName , Coordinatel , UserName,Status;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public MapreqModel(String profile, String city, String roadName, String coordinatel , String UserName,String status) {
        Profile = profile;
        City = city;
        RoadName = roadName;
        Coordinatel = coordinatel;
        this.UserName = UserName;
        Status = status;

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
}
