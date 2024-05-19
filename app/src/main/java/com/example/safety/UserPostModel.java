package com.example.safety;

public class UserPostModel {
    String PostImage , Caption, Profile , Name , Suggestions ;
    Integer Upvote , Downvote;
    String isAccidental ,status;

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

    public UserPostModel()
    {

    }

    public UserPostModel(String postImage, String caption, String profile, String name, String Suggestions,Integer Upvote , Integer Downvote,String isAccidental , String status ) {
        PostImage = postImage;
        Caption = caption;
        Profile = profile;
        Name = name;
        this.Suggestions = Suggestions;
        this.Upvote = Upvote;
        this.Downvote=Downvote;
        this.isAccidental = isAccidental;
        this.status = status;
    }

}
