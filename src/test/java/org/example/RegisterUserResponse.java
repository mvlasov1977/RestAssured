package org.example;

public class RegisterUserResponse {
    private int id;
    private String token;

    public RegisterUserResponse() {
    }

    public RegisterUserResponse(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
