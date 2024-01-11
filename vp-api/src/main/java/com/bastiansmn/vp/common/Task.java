package com.bastiansmn.vp.common;

import java.util.List;

public record Task(String uuid, Long start, Long duration, List<Task> dependencies) {

    @Override
    public String toString() {
        return "Task[start=" + start + ", duration=" + duration + "]";
    }

}
