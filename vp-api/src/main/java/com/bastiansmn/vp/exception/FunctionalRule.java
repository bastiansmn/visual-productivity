package com.bastiansmn.vp.exception;

import lombok.Getter;

@Getter
public enum FunctionalRule {

    USER_0001("USER_0001", "Un utilisateur avec cet email existe déjà"),
    USER_0002("USER_0002", "Un utilisateur avec ce nom d'utilisateur existe déjà"),
    USER_0003("USER_0003", "L'email n'est pas valide"),
    USER_0004("USER_0004", "Le mot de passe n'est pas valide"),
    USER_0005("USER_0005", "L'utilisateur n'existe pas"),
    ROLE_0001("ROLE_0001", "Le rôle n'existe pas"),
    AUTH_0001("AUTH_0001", "L'authorité n'existe pas"),
    ;

    private String name;
    private String message;
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
