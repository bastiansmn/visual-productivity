package com.bastiansmn.vp.exception;

import lombok.Getter;

@Getter
public enum FunctionalRule {

    USER_0001("USER_0001", "Un utilisateur avec cet email existe déjà"),
    USER_0002("USER_0002", "L'email n'est pas valide"),
    USER_0003("USER_0003", "Le mot de passe n'est pas valide"),
    USER_0004("USER_0004", "L'utilisateur n'existe pas"),
    USER_0005("USER_0005", "L'utilisateur est bloqué"),
    USER_0006("USER_0006", "L'utilisateur est désactivé"),
    USER_0007("USER_0007", "Vous êtes déjà connecté avec un compte externe (Google)"),
    USER_0008("USER_0008", "Utilisateur externe invalide"),
    ROLE_0001("ROLE_0001", "Le rôle n'existe pas"),
    AUTH_0001("AUTH_0001", "L'authorité n'existe pas"),
    MAIL_0001("MAIL_0001", "Votre compte est déjà activé"),
    MAIL_0002("MAIL_0002", "Le code est invalide"),
    MAIL_0003("MAIL_0003", "Le code est expiré"),
    TOKEN_0001("TOKEN_0001", "Le token est invalide"),
    TOKEN_0002("TOKEN_0002", "Aucun token renseigné"),
    PROJ_0001("PROJ_0001", "Le projet n'existe pas")
    ;
    private final String name;
    private final String message;
    FunctionalRule(final String name, final String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.getName(), this.getMessage());
    }

    public String toString(final Object... params) {
        return String.format("%s - %s", this.getName(), String.format(this.getMessage(), params));
    }

}
