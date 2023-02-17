package com.bastiansmn.vp.common.specifiation;

import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.Map;

public abstract class BaseSpecification<T> {
    private final String wildcard = "%";

    public abstract Specification<T> getFilter(Map<String, String> filters);

    /**
     * Lower case input string
     *
     * @param searchField to format
     * @return formatted string
     */
    protected String containsUpperCase(String searchField) {
        return wildcard + searchField.toUpperCase() + wildcard;
    }

    /**
     * Delete accents
     *
     * @param text to format
     * @return formatted string
     */
    protected String unaccent(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}

