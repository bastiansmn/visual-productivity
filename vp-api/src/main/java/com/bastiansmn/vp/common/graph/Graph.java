package com.bastiansmn.vp.common.graph;

import com.bastiansmn.vp.common.ExecutionOrder;
import com.bastiansmn.vp.common.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Predicate;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Graph {

    // Adjacency list implementation of graph
    @Getter
    static class AdjacencyList<T extends Comparable<T>> {

        private static final Logger LOGGER = LoggerFactory.getLogger(AdjacencyList.class);

        private final Map<Node<T>, List<Node<T>>> adjacency;

        public AdjacencyList(List<Edge<T>> edges, boolean isDirected) {
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

        public AdjacencyList(Graph.AdjacencyList<T> graph) {
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

        private boolean isSource(Node<T> n) {
            List<Node<T>> allDestinations = this.adjacency.values()
                    .stream()
                    .reduce(new LinkedList<>(), (acc, nodes) -> {
                        acc.addAll(nodes);
                        return acc;
                    });

            // If any vertices is going to the node (n), then n isn't a source.
            return !allDestinations.contains(n);
        }

        private boolean isDestination(Node<T> n) {
            List<Node<T>> node = this.adjacency.get(n);
            if (node == null)
                return false;
            return node.isEmpty();
        }

        void displayGraph() {
            this.adjacency.forEach((node, nodes) -> LOGGER.info(String.format("%s => [%s]", node,
                    nodes.stream()
                            .map(n -> "{" + n + ":" + n.getValue() + "}")
                            .reduce("", (acc, n) -> acc + (acc.equals("") ? "" : ", ") + n))));
        }
    }

    public static ExecutionOrder getExecutionOrder(Graph.AdjacencyList<Long> graph) {
        return getExecutionOrder(graph, Long.MAX_VALUE);
    }

    public static ExecutionOrder getExecutionOrder(Graph.AdjacencyList<Long> graph, Long maxParallelTasks) {
        // Adapt graph to the algorithm (spread weight of the nodes to the outgoing edges)
        graph.adjacency.forEach((node, nodes) -> nodes.forEach(n -> n.setValue(node.getValue())));

        // Copy to a new graph to keep the old one clean.
        Graph.AdjacencyList<Long> graphCopy = new Graph.AdjacencyList<>(graph);
        List<Task> tasks = new LinkedList<>();

        // Algorithm reference :
        // https://www.youtube.com/watch?v=zUeSavd-qUk&t=639s
        List<Node<Long>> sources;
        do {
            sources = graphCopy.getAllSources();

            sources
                .stream()
                .limit(maxParallelTasks)
                .forEach(s -> {
                    List<Task> prec = new LinkedList<>();
                    var precTask = new Object() {
                        Long maxStart = -1L;
                        Task maxTask = null;
                    };

                    // Get all prec tasks
                    graph.getAdjacency().forEach((key, value) -> {
                        if (value.contains(s)) {
                            Task task = tasks.stream()
                                    .filter(t -> t.uuid().equals(key.getRandomUUID())).findFirst()
                                    .orElseThrow(() -> new RuntimeException("Task not found"));

                            if (task.start() > precTask.maxStart) {
                                precTask.maxStart = task.start();
                                precTask.maxTask = task;
                            }

                            prec.add(task);
                        }
                    });

                    tasks.add(new Task(
                            s.getRandomUUID(),
                            precTask.maxTask == null ? 0L : precTask.maxTask.start() + precTask.maxTask.duration(),
                            s.getValue(),
                            prec
                    ));

                    graphCopy.getAdjacency().remove(s);
                });
        } while (!sources.isEmpty());

        return new ExecutionOrder(
                tasks.stream().map(t -> t.start()+t.duration()).max(Long::compareTo).orElse(0L),
                tasks
        );
    }

//    public static ExecutionOrder getExecutionOrder(Graph.AdjacencyList<Task> graph) {
//        return getExecutionOrder(graph, Long.MAX_VALUE);
//    }
//
//    public static ExecutionOrder getExecutionOrder(Graph.AdjacencyList<Task> graph, Long maxParallelTasks) {
//        // Adapt graph to the algorithm (spread weight of the nodes to the outgoing edges)
//        graph.adjacency.forEach((node, nodes) -> nodes.forEach(n -> n.setValue(node.getValue())));
//
//        // Copy to a new graph to keep the old one clean.
//        Graph.AdjacencyList<Task> graphCopy = new Graph.AdjacencyList<>(graph);
//        List<Task> tasks = new LinkedList<>();
//
//        // Algorithm reference :
//        // https://www.youtube.com/watch?v=zUeSavd-qUk&t=639s
//        List<Node<Task>> sources;
//        do {
//            sources = graphCopy.getAllSources();
//
//            sources
//                    .stream()
//                    .limit(maxParallelTasks)
//                    .forEach(s -> {
//                        List<Task> prec = new LinkedList<>();
//                        var precTask = new Object() {
//                            Long maxStart = -1L;
//                            Task maxTask = null;
//                        };
//
//                        // Get all prec tasks
//                        graph.getAdjacency().forEach((key, value) -> {
//                            if (value.contains(s)) {
//                                Task task = tasks.stream()
//                                        .filter(t -> t.uuid().equals(key.getRandomUUID())).findFirst()
//                                        .orElseThrow(() -> new RuntimeException("Task not found"));
//
//                                if (task.start() > precTask.maxStart) {
//                                    precTask.maxStart = task.start();
//                                    precTask.maxTask = task;
//                                }
//
//                                prec.add(task);
//                            }
//                        });
//
//                        tasks.add(new Task(
//                                s.getRandomUUID(),
//                                precTask.maxTask == null ? 0L : precTask.maxTask.start() + precTask.maxTask.duration(),
//                                s.getValue().duration(),
//                                prec
//                        ));
//
//                        graphCopy.getAdjacency().remove(s);
//                    });
//        } while (!sources.isEmpty());
//
//        return new ExecutionOrder(
//                tasks.stream().map(t -> t.start()+t.duration()).max(Long::compareTo).orElse(0L),
//                tasks
//        );
//    }

    public static void main(String[] args) {
        List<Node<Long>> nodes = List.of(
                new Node<>(3L), // a
                new Node<>(6L), // b
                new Node<>(10L), // c
                new Node<>(6L), // d
                new Node<>(4L), // e
                new Node<>(5L), // f
                new Node<>(7L), // g
                new Node<>(1L)  // h
        );

        // a -> b
        // a -> c
        // a -> f
        // b -> d
        // e -> b
        // e -> c
        // e -> d
        // f -> d
        // f -> h
        // g -> a
        // g -> f
        Graph.AdjacencyList<Long> graph = new Graph.AdjacencyList<>(List.of(
                new Edge<>(nodes.get(0), nodes.get(1), null),
                new Edge<>(nodes.get(0), nodes.get(2), null),
                new Edge<>(nodes.get(0), nodes.get(5), null),
                new Edge<>(nodes.get(1), nodes.get(3), null),
                new Edge<>(nodes.get(4), nodes.get(1), null),
                new Edge<>(nodes.get(4), nodes.get(2), null),
                new Edge<>(nodes.get(4), nodes.get(3), null),
                new Edge<>(nodes.get(5), nodes.get(3), null),
                new Edge<>(nodes.get(5), nodes.get(7), null),
                new Edge<>(nodes.get(6), nodes.get(0), null),
                new Edge<>(nodes.get(6), nodes.get(5), null)
        ), true);

//        List<Node<Task>> tasks = List.of(
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 3L, null)), // a
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 6L, null)), // b
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 10L, null)), // c
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 6L, null)), // d
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 4L, null)), // e
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 5L, null)), // f
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 7L, null)), // g
//                new Node<>(new Task(UUID.randomUUID().toString(), null, 1L, null))// h
//        );
//
//        Graph.AdjacencyList<Task> tasksGraph = new Graph.AdjacencyList<>(List.of(
//                new Edge<>(tasks.get(0), tasks.get(1), null),
//                new Edge<>(tasks.get(0), tasks.get(2), null),
//                new Edge<>(tasks.get(0), tasks.get(5), null),
//                new Edge<>(tasks.get(1), tasks.get(3), null),
//                new Edge<>(tasks.get(4), tasks.get(1), null),
//                new Edge<>(tasks.get(4), tasks.get(2), null),
//                new Edge<>(tasks.get(4), tasks.get(3), null),
//                new Edge<>(tasks.get(5), tasks.get(3), null),
//                new Edge<>(tasks.get(5), tasks.get(7), null),
//                new Edge<>(tasks.get(6), tasks.get(0), null),
//                new Edge<>(tasks.get(6), tasks.get(5), null)
//        ), true);

        graph.displayGraph();
        System.out.println(getExecutionOrder(graph));
//        System.out.println(getExecutionOrder(tasksGraph));
    }

}
