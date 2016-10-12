package com.solstice.atoi.rlbet;

/**
 * Created by Atoi on 13.09.2016.
 */
public class GoogleAccount {
    private String name;
    private String email;
    private String photoUrl;

    public GoogleAccount(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
