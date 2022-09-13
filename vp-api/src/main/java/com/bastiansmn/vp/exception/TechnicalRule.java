package com.bastiansmn.vp.exception;

import lombok.Getter;

@Getter
public enum TechnicalRule {


    ;

    private String name;

    private String message;

    TechnicalRule(final String name, final String message) {
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
