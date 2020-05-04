
package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@Controller
//@Secured("ROLE_USER")
@RequestMapping("/matches")
public class MatchController {
    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchRepository repository;
    private final TeamRepository teamRepository;
    private final GoalRepository goalRepository;

    MatchController(final MatchRepository repository, TeamRepository teamRepository, GoalRepository goalRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.goalRepository = goalRepository;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteMatch(@PathVariable Integer id, Model model) {
        repository.deleteById(id);
        model.addAttribute("matchesAll", repository.findAll());
        return "matches";
    }

    @GetMapping(value = "/{id}")
    String showMatch(Model model, @PathVariable int id) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        model.addAttribute("match", match);
        model.addAttribute("goalsById", goalRepository.findByMatchId(id));
        model.addAttribute(goalsCountById,)
        return "match";
    }

    @GetMapping
    String showAllMatches(Model model) {
        model.addAttribute("matchesAll", repository.findAll());
        return "matches";
    }

    @GetMapping(value = "/addmatch")
    String showMatches(Model model) {
        model.addAttribute("match", new Match());
        model.addAttribute("teamsAll", teamRepository.findAll());
        return "addmatch";
    }

    @PostMapping(params = "addMatch", value = "/addmatch")
    String addTeam(@ModelAttribute("match") @Valid Match current,
                   BindingResult bindingResult,
                   Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "addmatch";
        }

        //current.setHomeTeam(teamRepository.findById(current.getHomeTeam().getId()).get());
        //current.setAwayTeam(teamRepository.findById(current.getAwayTeam().getId()).get());

        //current.getHomeTeam().getHomeTeamMatches().add(current);
        //current.getAwayTeam().getAwayTeamMatches().add(current);

        repository.save(current);

        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("match", new Match());
        model.addAttribute("message", "Match added!");
        model.addAttribute("matchesAll", repository.findAll());
        return "matches";
    }

/*
    @GetMapping
    ResponseEntity<List<Match>> readAllMatches(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @PostMapping
    ResponseEntity<Match> createMatch(@RequestBody Match toCreate) {
        toCreate.setHomeTeam(teamRepository.findById(64).get());
        toCreate.setAwayTeam(teamRepository.findById(63).get());
        Match result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }
*/

}
