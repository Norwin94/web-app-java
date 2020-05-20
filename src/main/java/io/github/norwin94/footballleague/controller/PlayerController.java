package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.logic.PlayerService;
import io.github.norwin94.footballleague.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/players")
public class PlayerController {
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerRepository repository;
    private final GoalRepository goalRepository;
    private final TeamRepository teamRepository;
    private final PlayerService service;

    PlayerController(final PlayerRepository repository, GoalRepository goalRepository, TeamRepository teamRepository, PlayerService service) {
        this.repository = repository;
        this.goalRepository = goalRepository;
        this.teamRepository = teamRepository;
        this.service = service;
    }

    //POST ADD
    @Secured("ROLE_ADMIN")
    @PostMapping(params = "addPlayer", value = "/add")
    String addTeam(@ModelAttribute("player") @Valid Player current,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "player-add";
        }
        repository.save(current);

        redirectAttributes.addFlashAttribute("message", "Player added!");
        model.addAttribute("playersAll", repository.findAll());
        return "redirect:/players";
    }

    //POST DELETE
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteMatch(@PathVariable Integer id,
                       Model model,
                       RedirectAttributes redirectAttributes
    ) {
        Player player = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        repository.deleteById(player.getId());
        redirectAttributes.addFlashAttribute("message", "Player deleted!");
        model.addAttribute("playersAll", repository.findAll());
        return "redirect:/players";
    }

    //POST EDIT
    @Secured("ROLE_USER")
    @PostMapping(params = "editPlayer", value = "/{id}/edit")
    String editPlayer(@ModelAttribute("player") @Valid Player toUpdate,
                      BindingResult bindingResult,
                      Model model,
                      @PathVariable Integer id,
                      RedirectAttributes redirectAttributes
    ) {
        repository.save(toUpdate);
        redirectAttributes.addFlashAttribute("messageGood", "Player edited!");
        model.addAttribute("playersAll", repository.findAll());
        return "redirect:/players";
    }

    //GET ADD
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/add")
    String showPlayers(Model model) {
        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("player", new Player());
        return "player-add";
    }

    //GET EDIT
    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/edit")
    String showPlayerToEdit(Model model, @PathVariable int id) {
        Player player = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        model.addAttribute("player", player);
        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("actualPage", "players");
        model.addAttribute("modeType", "edit");
        return "player";
    }

    //GET SEE ONE
    @GetMapping(value = "/{id}/see")
    String showPlayer(Model model, @PathVariable int id) {
        Player player = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        List<Goal> goals = goalRepository.findAllByPlayer_IdOrderByMatch_IdAscMinuteAsc(id);

        model.addAttribute("player", player);
        model.addAttribute("goals", goals);
        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("actualPage", "players");
        model.addAttribute("modeType", "see");
        return "player";
    }

    //GET ALL
    @GetMapping
    String showAllPlayers(Model model) {
        model.addAttribute("playersAll", repository.findAll());
        model.addAttribute("actualPage", "players");
        return "players";
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

}
