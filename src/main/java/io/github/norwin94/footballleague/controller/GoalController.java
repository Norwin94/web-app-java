
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
@RequestMapping("/goals")
public class GoalController {
    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);

    private final GoalRepository repository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    GoalController(final GoalRepository repository, MatchRepository matchRepository, PlayerRepository playerRepository) {
        this.repository = repository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteGoal(@PathVariable Integer id, Model model) {
        repository.deleteById(id);
        model.addAttribute("goalsAll", repository.findAll());
        return "goals";
    }

    @GetMapping
    String showAllGoals(Model model) {
        model.addAttribute("goalsAll", repository.findAll());
        return "goals";
    }

    @GetMapping(value = "/addgoal")
    String showGoals(Model model) {
        model.addAttribute("matchesAll", matchRepository.findAll());
        model.addAttribute("playersAll", playerRepository.findAll());
        model.addAttribute("goal", new Goal());
        model.addAttribute("allGoals", repository.findAll());
        return "addgoal";
    }

    @PostMapping(params = "addGoal", value = "/addgoal")
    String addGoal(@ModelAttribute("goal") @Valid Goal current,
                   BindingResult bindingResult,
                   Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "addgoal";
        }

        repository.save(current);

        model.addAttribute("playersAll", playerRepository.findAll());
        model.addAttribute("matchesAll", matchRepository.findAll());
        model.addAttribute("goal", new Goal());
        model.addAttribute("message", "Goal added!");
        model.addAttribute("goalsAll", repository.findAll());
        return "goals";
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
