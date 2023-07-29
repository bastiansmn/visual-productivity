package com.bastiansmn.vp.common.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Node<T extends Comparable<T>> {

    private static Long instanceCount = 0L;

    private final String randomUUID;
    private final String readableID;

    private T value;

    private Node() {
        this.randomUUID = UUID.randomUUID().toString();
        instanceCount++;
        // Alphabetical ID
        this.readableID = String.valueOf((char) (instanceCount + 64));
    }

    public Node(T value) {
        this();
        this.value = value;
    }

    public Node(Node<T> node) {
        this.randomUUID = node.getRandomUUID();
        this.value = node.getValue();
        this.readableID = node.getReadableID();
    }

    @Override
    public String toString() {
        return '(' + this.readableID + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node<?> node) {
            return this.randomUUID.equals(node.getRandomUUID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.randomUUID.hashCode();
    }

}
