package com.bastiansmn.vp.common.graph;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

@Getter
public class Graph<T extends Comparable<T>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

    private final Map<Node<T>, List<Node<T>>> adjacency;

    public Graph(List<Edge<T>> edges, boolean isDirected) {
        this.adjacency = new LinkedHashMap<>();

        edges.forEach(edge -> {
            if (!this.adjacency.containsKey(edge.getSource()))
                this.adjacency.put(edge.getSource(), new ArrayList<>());
            if (!this.adjacency.containsKey(edge.getDestination()))
                this.adjacency.put(edge.getDestination(), new ArrayList<>());

            Node<T> n = new Node<>(edge.getDestination());
            this.adjacency.get(edge.getSource()).add(n);
            if (!isDirected) {
                n = new Node<>(edge.getSource());
                this.adjacency.get(edge.getDestination()).add(n);
            }
        });
    }

    public Graph(Graph<T> graph) {
        this.adjacency = new LinkedHashMap<>(graph.getAdjacency());
    }

    public List<Node<T>> getAllSources() {
        return this.getAll(this::isSource);
    }

    public List<Node<T>> getAllDestinations() {
        return this.getAll(this::isDestination);
    }

    public List<Node<T>> getAll(Predicate<Node<T>> p) {
        return this.getAllNodes()
                .stream().filter(p)
                .toList();
    }

    public List<Node<T>> getAllNodes() {
        return this.adjacency.keySet()
                .stream().toList();
    }

    public boolean isSource(Node<T> n) {
        List<Node<T>> allDestinations = this.adjacency.values()
                .stream()
                .reduce(new LinkedList<>(), (acc, nodes) -> {
                    acc.addAll(nodes);
                    return acc;
                });

        // If any vertices is going to the node (n), then n isn't a source.
        return !allDestinations.contains(n);
    }

    public boolean isDestination(Node<T> n) {
        List<Node<T>> node = this.adjacency.get(n);
        if (node == null)
            return false;
        return node.isEmpty();
    }

    public void displayGraph() {
        this.adjacency.forEach((node, nodes) -> LOGGER.info(String.format("%s => [%s]", node,
                nodes.stream()
                        .map(n -> "{" + n + ":" + n.getValue() + "}")
                        .reduce("", (acc, n) -> acc + (acc.equals("") ? "" : ", ") + n))));
    }
}
