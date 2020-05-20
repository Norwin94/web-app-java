
package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
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

    //POST ADD
    @Secured("ROLE_ADMIN")
    @PostMapping(params = "addGoal", value = "/add")
    String addGoal(@ModelAttribute("goal") @Valid Goal current,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "goal-add";
        }

        repository.save(current);

        model.addAttribute("goal", new Goal());
        model.addAttribute("goalsAll", repository.findAll());
        redirectAttributes.addFlashAttribute("messageGood", "Goal added!");
        return "redirect:/goals";
    }

    //POST DELETE
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteGoal(@PathVariable Integer id,
                      Model model,
                      RedirectAttributes redirectAttributes
    ) {
        Goal goal = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        repository.deleteById(goal.getId());

        redirectAttributes.addFlashAttribute("messageGood", "Goal deleted!");
        model.addAttribute("goalsAll", repository.findAll());
        return "redirect:/goals";
    }

    //POST EDIT
    /*@Secured("ROLE_USER")
    @PostMapping(params = "editGoal", value = "/{id}/edit")
    String editGoal(@ModelAttribute("goal") @Valid Goal current, @PathVariable int id,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "goal-add";
        }
        Goal goal = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        //goal.setMinute(5);
        int i = current.getId();
        repository.save(current);
        int j = current.getId();
        //model.addAttribute("goal", new Goal());
        //model.addAttribute("goalsAll", repository.findAll());
        redirectAttributes.addFlashAttribute("messageGood", "Goal edited!" + i + ", " + j);
        return "redirect:/matches/13/see";
    }*/

    //GET EDIT
    @Secured("ROLE_USER")
    @GetMapping(value = "{id}/edit")
    String editGoal(Model model,
                    @ModelAttribute("match") @Valid Match current,
                    @PathVariable int id) {
        Goal goal = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        int matchId = goal.getMatch().getId();
        int playerId = goal.getPlayer().getId();
        //String caseHomeAway = "";
        //if()

        model.addAttribute("goal", goal);
        model.addAttribute("match", matchRepository.findAll());
        model.addAttribute("playersHome", playerRepository.findAll());
        model.addAttribute("modeType", "edit");
        return "goal-add";
    }

    //GET ADD
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/add")
    String showGoals(Model model) {
        model.addAttribute("matchesAll", matchRepository.findAll());
        model.addAttribute("playersAll", playerRepository.findAll());
        model.addAttribute("goal", new Goal());
        return "goal-add";
    }

    //GET SEE ALL
    @GetMapping
    String showAllGoals(Model model) {
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
