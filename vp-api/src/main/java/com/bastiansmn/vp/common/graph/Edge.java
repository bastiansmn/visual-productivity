package com.bastiansmn.vp.common.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Edge<T extends Comparable<T>> {

    private static Long instanceCount = 0L;

    private final String randomUUID;
    private final Long readableID;

    private Node<T> source;
    private Node<T> destination;
    private T weight;

    private Edge() {
        this.randomUUID = UUID.randomUUID().toString();
        instanceCount++;
        this.readableID = instanceCount;
    }

    public Edge(Node<T> source, Node<T> destination, T weight) {
        this();
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return this.randomUUID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge<?> edge) {
            return
                this.source.equals(edge.getSource()) &&
                this.destination.equals(edge.getDestination()) &&
                this.weight.equals(edge.getWeight());
        }
        return false;
    }

    @Override
    public String toString() {
        return source.toString() + " - " + weight.toString() + " -> " + destination.toString();
    }
}
