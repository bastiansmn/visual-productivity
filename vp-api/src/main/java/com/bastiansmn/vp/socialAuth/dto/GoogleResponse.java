package com.bastiansmn.vp.socialAuth.dto;

public class GoogleResponse {

    public static class Success {
        String iss;
        String nbf;
        String aud;
        String sub;
        String email;
        String email_verified;
        String azp;
        String name;
        String picture;
        String given_name;
        String iat;
        String exp;
        String jti;
        String alg;
        String kid;
        String typ;
    }

    public static class Error {
        String error;
        String error_description;
    }

}
