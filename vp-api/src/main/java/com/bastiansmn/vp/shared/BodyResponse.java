package com.bastiansmn.vp.shared;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BodyResponse<T> {

    private final T data;
    private String message;

}
