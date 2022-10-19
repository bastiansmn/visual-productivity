package com.bastiansmn.vp.socialAuth;

public enum UserProvider {
    LOCAL,
    GOOGLE
    ;

    public static UserProvider fromString(String provider) {
        if (provider == null) return null;
        switch (provider) {
            case "local":
                return LOCAL;
            case "google":
                return GOOGLE;
            default:
                return null;
        }
    }
}
