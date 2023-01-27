package com.bastiansmn.vp.label;

import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.label.dto.LabelCreationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/label")
@RequiredArgsConstructor
@Slf4j
public class LabelController {

    private final LabelService labelService;

    @GetMapping("/allOfProject")
    public ResponseEntity<Set<LabelDAO>> fetchAllOfProject(@RequestParam String project_id) throws FunctionalException {
        return ResponseEntity.ok(this.labelService.fetchAllOfProject(project_id));
    }

    @GetMapping("/filterByName")
    public ResponseEntity<List<LabelDAO>> filterByName(@RequestParam String project_id, @RequestParam String name) throws FunctionalException {
        return ResponseEntity.ok(this.labelService.filterByName(project_id, name));
    }

    @PostMapping("/create")
    public ResponseEntity<LabelDAO> create(@RequestBody LabelCreationDTO label) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/label/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(this.labelService.create(label));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long label_id) throws FunctionalException {
        this.labelService.delete(label_id);
        return ResponseEntity.noContent().build();
    }

}
