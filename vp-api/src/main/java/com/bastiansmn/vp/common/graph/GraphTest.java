package com.bastiansmn.vp.common.graph;

import com.bastiansmn.vp.task.dto.ExecutionOrder;
import com.bastiansmn.vp.common.Task;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GraphTest {

    public static ExecutionOrder getExecutionOrder(Graph<Long> graph) {
        return getExecutionOrder(graph, Long.MAX_VALUE);
    }

    public static ExecutionOrder getExecutionOrder(Graph<Long> graph, Long maxParallelTasks) {
        // Adapt graph to the algorithm (spread weight of the nodes to the outgoing edges)
        graph.getAdjacency().forEach((node, nodes) -> nodes.forEach(n -> n.setValue(node.getValue())));

        // Copy to a new graph to keep the old one clean.
        Graph<Long> graphCopy = new Graph<>(graph);
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
        Graph<Long> graph = new Graph<>(List.of(
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

        Task e = new Task(UUID.randomUUID().toString(), null, 4L, List.of());
        Task g = new Task(UUID.randomUUID().toString(), null, 7L, List.of());
        Task a = new Task(UUID.randomUUID().toString(), null, 3L, List.of(g));
        Task b = new Task(UUID.randomUUID().toString(), null, 6L, List.of(a, e));
        Task f = new Task(UUID.randomUUID().toString(), null, 5L, List.of(a, g));
        Task c = new Task(UUID.randomUUID().toString(), null, 10L, List.of(a, e));
        Task d = new Task(UUID.randomUUID().toString(), null, 6L, List.of(b, e, f));
        Task h = new Task(UUID.randomUUID().toString(), null, 1L, List.of(f));
        List<Task> tasks = List.of(a, b, c, d, e, f, g, h);

        graph.displayGraph();
        System.out.println(getExecutionOrder(graph));
        System.out.println("=========");
//        System.out.println(getExecutionOrder(toGraph(tasks)));
    }

}
