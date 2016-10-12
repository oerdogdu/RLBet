package com.solstice.atoi.rlbet;

/**
 * Created by Atoi on 17.09.2016.
 */
public class RegisteredAccount {
    private String email, password;

    public RegisteredAccount(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
