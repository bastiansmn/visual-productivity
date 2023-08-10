package com.bastiansmn.vp.common;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record Task(String uuid, Long start, Long duration, List<Task> dependencies) implements Comparable<Task> {

    @Override
    public String toString() {
        return "Task[start=" + start + ", duration=" + duration + "]";
    }

    @Override
    public int compareTo(@NotNull Task o) {
        return this.duration.compareTo(o.duration);
    }
}
