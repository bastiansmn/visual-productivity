package com.bastiansmn.vp.event;

import com.bastiansmn.vp.event.dto.EventCreationDto;
import com.bastiansmn.vp.event.dto.EventDto;
import com.bastiansmn.vp.exception.FunctionalException;
import com.bastiansmn.vp.utils.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<EventDto> fetchById(@RequestParam Long event_id) throws FunctionalException {
        return ResponseEntity.ok(
            EventMapper.toDto(
                this.eventService.fetchById(event_id)
            )
        );
    }

    @GetMapping("/allOfProject")
    public ResponseEntity<List<EventDto>> fetchAllOfProject(
            @RequestParam String project_id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) throws FunctionalException {
        return ResponseEntity.ok(
            this.eventService.fetchAllOfProject(project_id, from, to)
                    .stream()
                    .map(event -> EventMapper.toDto(event, this.eventService.isParticipating(event), this.eventService.createdByMe(event)))
                    .toList()
        );
    }

    @GetMapping("/myEvents")
    public ResponseEntity<List<EventDto>> fetchMyEvents(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) throws FunctionalException {
        return ResponseEntity.ok(
            this.eventService.myEvents(from, to)
                    .stream()
                    .map(event -> EventMapper.toDto(event, Boolean.TRUE, this.eventService.createdByMe(event)))
                    .toList()
        );
    }


    @PostMapping("/create")
    public ResponseEntity<EventDto> create(@RequestBody EventCreationDto event) throws FunctionalException {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/v1/event/create")
                        .toUriString()
        );
        return ResponseEntity.created(uri).body(
            EventMapper.toDto(
                this.eventService.create(event),
                Boolean.TRUE,
                Boolean.TRUE
            )
        );
    }

    @PutMapping("/participate")
    public ResponseEntity<EventDto> participate(@RequestParam Long event_id) throws FunctionalException {
        return ResponseEntity.ok(
            EventMapper.toDto(
                this.eventService.participate(event_id),
                Boolean.TRUE,
                Boolean.FALSE
            )
        );
    }

    @PutMapping("/unparticipate")
    public ResponseEntity<EventDto> unparticipate(@RequestParam Long event_id) throws FunctionalException {
        return ResponseEntity.ok(
            EventMapper.toDto(
                this.eventService.unparticipate(event_id),
                Boolean.FALSE,
                Boolean.FALSE
            )
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long event_id) throws FunctionalException {
        this.eventService.delete(event_id);
        return ResponseEntity.noContent().build();
    }

}
