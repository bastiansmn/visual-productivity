package com.bastiansmn.vp.task;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.task.dto.TaskCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/task")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDAO> create(@RequestBody TaskCreationDTO task) throws FunctionalException {
        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/task/create")
                .toUriString()
        );
        return ResponseEntity.created(uri).body(this.taskService.create(task));
    }

    @GetMapping("/fetchByID")
    public ResponseEntity<TaskDAO> fetchByID(@RequestParam Long task_id) throws FunctionalException {
        return ResponseEntity.ok(this.taskService.fetchById(task_id));
    }

    @GetMapping("/fetchAllOfProject")
    public ResponseEntity<Collection<TaskDAO>> fetchAllOfProject(@RequestParam String project_id) throws FunctionalException {
        return ResponseEntity.ok(this.taskService.fetchAllOfProject(project_id));
    }

    @GetMapping("/fetchAllOfGoal")
    public ResponseEntity<Collection<TaskDAO>> fetchAllOfGoal(@RequestParam Long goal_id) throws FunctionalException {
        return ResponseEntity.ok(this.taskService.fetchAllOfGoal(goal_id));
    }

    @PatchMapping("/markAsDone")
    public ResponseEntity<TaskDAO> markAsDone(@RequestParam Long task_id) throws FunctionalException {
        return ResponseEntity.ok(this.taskService.markAsDone(task_id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long task_id) throws FunctionalException {
        this.taskService.delete(task_id);
        return ResponseEntity.noContent().build();
    }
}
