package dev.azsoft.wifiattendancesystemadmin.models;

public class CreateUserResponse {
            String kind;
            String idToken;
            String email;
            String refreshToken;
            String expiresIn;
            String localId;

    public CreateUserResponse() {
    }

    public String getKind() {
        return kind;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getLocalId() {
        return localId;
    }
}
