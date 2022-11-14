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
    PROJ_0001("PROJ_0001", "Le projet n'existe pas"),
    PROJ_0002("PROJ_0002", "Aucune deadline spécifiée"),
    PROJ_0003("PROJ_0003", "Aucun nom spécifié"),
    PROJ_0004("PROJ_0004", "Aucune description spécifiée"),
    PROJ_0005("PROJ_0005", "La deadline ne peut pas être déjà passée"),
    PROJ_0006("PROJ_0006", "Impossible de créer un projet sans utilisateur"),
    PROJ_0007("PROJ_0007", "Vous devez être membre du projet pour effectuer cette action"),
    PROJ_0008("PROJ_0008", "L'utilisateur est déjà membre du projet"),
    GOAL_0001("GOAL_0001", "L'objectif n'existe pas"),
    GOAL_0002("GOAL_0002", "Aucun nom spécifié"),
    GOAL_0003("GOAL_0003", "Aucune description spécifiée"),
    GOAL_0004("GOAL_0004", "Aucun projet spécifié"),
    GOAL_0005("GOAL_0005", "Les dates précisées ne sont pas valides"),
    GOAL_0006("GOAL_0006", "Vous devez être membre du projet pour effectuer cette action"),
    TASK_0001("TASK_0001", "La tâche n'existe pas"),
    TASK_0002("TASK_0002", "Aucun nom spécifié"),
    TASK_0003("TASK_0003", "Aucune description spécifiée"),
    TASK_0004("TASK_0004", "Les dates précisées ne sont pas valides"),
    TASK_0005("TASK_0005", "Vous devez être membre du projet pour effectuer cette action"),
    TASK_0006("TASK_0006", "Aucun objectif spécifié"),
    TASK_0007("TASK_0007", "Aucun projet spécifié"),
    TASK_0008("TASK_0008", "L'objectif n'appartient pas au projet mentionné"),
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
