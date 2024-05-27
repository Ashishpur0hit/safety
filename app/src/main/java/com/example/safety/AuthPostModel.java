package com.example.safety;

public class AuthPostModel {

    String PostImage , Caption, Profile , Name , Suggestions ;
    Integer Upvote , Downvote;
    String isAccidental ,status,Location;

    public AuthPostModel(String postImage, String caption, String profile, String userName, String suggestions, Integer upvote, Integer downvote, String isAccidentProne, String status, String isAccidentProne1, String location) {
        PostImage = postImage;
        Caption = caption;
        Profile = profile;
        Name = userName;
        Suggestions = suggestions;
        Upvote = upvote;
        Downvote = downvote;
        this.status = status;
        this.Location = location;
        this.isAccidental = isAccidentProne;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public AuthPostModel(String postImage, String caption, String profile, String name, String suggestions, Integer upvote, Integer downvote, String isAccidental, String status, String location) {
        PostImage = postImage;
        Caption = caption;
        Profile = profile;
        Name = name;
        Suggestions = suggestions;
        Upvote = upvote;
        Downvote = downvote;
        this.isAccidental = isAccidental;
        this.status = status;
        this.Location = location;
    }



    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSuggestions() {
        return Suggestions;
    }

    public void setSuggestions(String suggestions) {
        Suggestions = suggestions;
    }

    public Integer getUpvote() {
        return Upvote;
    }

    public void setUpvote(Integer upvote) {
        Upvote = upvote;
    }

    public Integer getDownvote() {
        return Downvote;
    }

    public void setDownvote(Integer downvote) {
        Downvote = downvote;
    }

    public String getIsAccidental() {
        return isAccidental;
    }

    public void setIsAccidental(String isAccidental) {
        this.isAccidental = isAccidental;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
