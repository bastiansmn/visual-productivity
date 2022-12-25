package com.bastiansmn.vp.goal;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.goal.dto.GoalCreationDTO;
import com.bastiansmn.vp.goal.dto.StatusUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/goal")
@Slf4j
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/create")
    public ResponseEntity<GoalDAO> create(@RequestBody GoalCreationDTO goal) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/goal/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.goalService.create(goal));
    }

    @GetMapping("/fetchByID")
    public ResponseEntity<GoalDAO> fetchByID(@RequestParam Long goal_id) throws FunctionalException {
        return ResponseEntity.ok(this.goalService.fetchById(goal_id));
    }

    @GetMapping("/fetchAllOfProject")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Collection<GoalDAO>> fetchAll(@RequestParam String project_id) throws FunctionalException {
        return ResponseEntity.ok(this.goalService.fetchAll(project_id));
    }

    @PatchMapping("/updateStatus")
    public ResponseEntity<GoalDAO> update(@RequestBody StatusUpdateDTO goal) throws FunctionalException {
        return ResponseEntity.ok(this.goalService.update(goal));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long goal_id) throws FunctionalException {
        this.goalService.delete(goal_id);
        return ResponseEntity.noContent().build();
    }

}
