package com.example.safety;

public class AcceptedPostModel {
    String PostImage , UserName , Caption;

    public AcceptedPostModel(String postImage, String userName, String caption) {
        PostImage = postImage;
        UserName = userName;
        Caption = caption;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }
}
