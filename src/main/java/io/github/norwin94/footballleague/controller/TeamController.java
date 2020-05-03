package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.PlayerRepository;
import io.github.norwin94.footballleague.model.Team;
import io.github.norwin94.footballleague.model.TeamRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
@Secured("ROLE_USER")
@RequestMapping("/teams")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    private final TeamRepository repository;

     TeamController(final TeamRepository repository, PlayerRepository playerRepository) {
        this.repository = repository;
     }

    /////////////////////////////////////////////////////////////////////////////
    //ALL TEAMS VIEW
    @GetMapping
    String showAllTeams(Model model) {
        model.addAttribute("teamsAll", repository.findAll());
        return "teams";
    }

    /////////////////////////////////////////////////////////////////////////////
    //ADD NEW TEAM
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteTeam(@PathVariable Integer id, Model model, Authentication auth) {
         //if(auth.getAuthorities().stream().anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"))) {
             for (Player temp : repository.findById(id).get().getPlayers()) {
                 temp.setTeam(null);
             }
             repository.deleteById(id);
             model.addAttribute("teamsAll", repository.findAll());
             return "teams";
         //}
         //return "index";
    }

    @GetMapping(value = "/add")
    String showTeams(Model model) {
        model.addAttribute("team", new Team());
        return "add";
    }

    @PostMapping(params = "addPlayer", value = "/add")
    String addPlayer(@ModelAttribute("team") Team current) {
         current.getPlayers().add(new Player());
         return "add";
    }

    @PostMapping(params = "addTeam", value = "/add")
    String addTeam(@ModelAttribute("team") @Valid Team current,
                   BindingResult bindingResult,
                   Model model
    ) {
         if(bindingResult.hasErrors()) {
             return "add";
         }
        //current.getPlayers().get(0).setTeam(current);
        for (Player temp : current.getPlayers()) {
            temp.setTeam(current);
        }
        repository.save(current);
        model.addAttribute("team", new Team());
        model.addAttribute("message", "Team added!");
        model.addAttribute("teamsAll", repository.findAll());
        return "teams";
    }


    /////////////////////////////////////////////////////////////////////////////
    //ONE TEAM VIEW
    @Timed(value = "teams.show.team", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @GetMapping(value = "/{id}")
    String showTeam(Model model, @PathVariable int id) {
         Team team = repository.findById(id)
                 .orElseThrow(()-> new IllegalStateException("Not found this id"));
        model.addAttribute("team", team);
        return "team";
    }

    /////////////////////////////////////////////////////////////////////////////
    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Team> createTeam(@RequestBody @Valid Team toCreate) {
        Team result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @ResponseBody
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Team> readTeam(@PathVariable int id) {
        return repository.findById(id)
                         .map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/players")
    ResponseEntity<List<Player>> readAllPlayersFromGroup(@PathVariable int id) {
         return ResponseEntity.ok(repository.findPlayersById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTeam(@PathVariable int id, @RequestBody @Valid Team toUpdate) {
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

    @PutMapping("/{id}/addPlayer")
    ResponseEntity<?> updateTeamWithPlayer(@PathVariable int id, @RequestBody @Valid Player toUpdate) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(team -> {
                    team.updateFrom(team.addPlayer(toUpdate));
                    repository.save(team);
                });
        return ResponseEntity.noContent().build();
    }
/*
    @PutMapping("/{id}/addPlayer/{srid}")
    ResponseEntity<?> updateTeamWithPlayer(@PathVariable int id, @PathVariable int srid) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(team -> {
                    team.updateFrom(team.addPlayer());
                    repository.save(team);
                });
        return ResponseEntity.noContent().build();
    }
*/
    //nie dziala, gdy teamName is null, bo teamName w Team ma @NotBlank
    //@Transactional tutaj dodal, rollbackuje gdy nie przejdzie cale
    @PatchMapping("/{id}")
    ResponseEntity<?> changeTeamName(@PathVariable int id, @RequestBody @Valid Team partialUpdates) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        Team team = repository.findById(id).get();
        if(!partialUpdates.getTeamName().isEmpty()) {
            team.setTeamName(partialUpdates.getTeamName());
        }
        if(partialUpdates.getFounded() != null) {
            team.setFounded(partialUpdates.getFounded());
        }
        repository.save(team);

        return ResponseEntity.noContent().build();
    }
}
