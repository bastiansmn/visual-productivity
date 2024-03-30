package com.bastiansmn.vp.task.dto;


import com.bastiansmn.vp.common.Task;

import java.util.List;

public record ExecutionOrder(
        Long duration,
        List<Task> tasks
) { }
