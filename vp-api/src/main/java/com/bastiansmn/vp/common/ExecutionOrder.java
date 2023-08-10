package com.bastiansmn.vp.common;


import java.util.List;

public record ExecutionOrder(
        Long duration,
        List<Task> tasks
) { }
