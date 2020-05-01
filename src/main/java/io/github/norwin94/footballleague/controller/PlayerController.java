package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.logic.PlayerService;
import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.PlayerRepository;
import io.github.norwin94.footballleague.model.Team;
import io.github.norwin94.footballleague.model.TeamRepository;
import org.apache.tomcat.util.http.parser.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class PlayerController {
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerRepository repository;
    private final PlayerService service;

    PlayerController(final PlayerRepository repository, PlayerService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("/players")
    ResponseEntity<Player> createPlayer(@RequestBody @Valid Player toCreate) {
        Player result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(value = "/players", params = {"!sort", "!page", "!size"})
    CompletableFuture<ResponseEntity<List<Player>>> readAllPlayers() {
        logger.warn("Exposing all the players!");
        return service.findAllAsync().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/players")
    ResponseEntity<List<Player>> readAllPlayers(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/players/{id}")
    ResponseEntity<Player> readPlayer(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/players/{id}/team")
    ResponseEntity<Team> readPlayersTeam(@PathVariable int id) {
        return repository.findByTeam(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/players/{id}")
    ResponseEntity<?> updatePlayer(@PathVariable int id, @RequestBody @Valid Player toUpdate) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(team -> {
                    team.updateFrom(toUpdate);
                    repository.save(team);
                });
        return ResponseEntity.noContent().build();
    }

    //nie dziala, gdy teamName is null, bo teamName w Team ma @NotBlank
    //@Transactional tutaj dodal, rollbackuje gdy nie przejdzie cale
    @PatchMapping("/players/{id}")
    ResponseEntity<?> changePlayerName(@PathVariable int id, @RequestBody @Valid Player partialUpdates) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Player player = repository.findById(id).get();
        if(!partialUpdates.getFirstName().isEmpty()) {
            player.setFirstName(partialUpdates.getFirstName());
        }
        if(partialUpdates.getBirthDate() != null) {
            player.setBirthDate(partialUpdates.getBirthDate());
        }
        repository.save(player);

        return ResponseEntity.noContent().build();
    }
}
