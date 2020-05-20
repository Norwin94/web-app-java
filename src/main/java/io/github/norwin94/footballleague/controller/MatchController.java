
package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchRepository repository;
    private final TeamRepository teamRepository;
    private final GoalRepository goalRepository;
    private final PlayerRepository playerRepository;

    MatchController(final MatchRepository repository, TeamRepository teamRepository, GoalRepository goalRepository, PlayerRepository playerRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.goalRepository = goalRepository;
        this.playerRepository = playerRepository;
    }

    //POST ADD MATCH
    @Secured("ROLE_ADMIN")
    @PostMapping(params = "addMatch", value = "/add")
    String addMatch(@ModelAttribute("match") @Valid Match current,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("messageBad", bindingResult.getAllErrors().toString());
            return "redirect:/matches/add";
        }

        if(current.getHomeTeam().getId() == current.getAwayTeam().getId()) {
            redirectAttributes.addFlashAttribute("messageBad", "Home team must not be the same as away team!");
            return "redirect:/matches/add";
        }

        repository.save(current);

        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("match", new Match());
        redirectAttributes.addFlashAttribute("messageGood", "Match added!");
        return "redirect:/matches";
    }

    //POST DELETE
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteMatch(@PathVariable Integer id,
                       Model model,
                       RedirectAttributes redirectAttributes
    ) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        repository.deleteById(match.getId());
        model.addAttribute("matchesAll", repository.findAll());
        redirectAttributes.addFlashAttribute("messageGood", "Match deleted!");
        return "redirect:/matches";
    }

    //POST EDIT MATCH
    @Secured("ROLE_USER")
    @PostMapping(params = "editMatch", value = "/{id}/edit")
    String editMatch(@ModelAttribute("match") @Valid Match toUpdate,
                    @PathVariable Integer id,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "match-add";
        }
        Match match = repository.findById(id).orElseThrow(()-> new IllegalStateException("Not found this id"));
        List<Goal> goals = goalRepository.findAllByMatch_Id(id);
        Integer goalsHomeCount = 0;
        Integer goalsAwayCount = 0;
        for(Goal goal : goals) {
            if(goal.getPlayer().getTeam().getId() == match.getHomeTeam().getId()) {
                goalsHomeCount++;
            }
            else goalsAwayCount++;
        }
        if(goalsHomeCount > toUpdate.getHomeScore() || goalsAwayCount > toUpdate.getAwayScore()) {
            redirectAttributes.addFlashAttribute("messageBad", "Too many goals, delete some!");
            return "redirect:/matches/{id}/see";
        }

        repository.save(toUpdate);
        model.addAttribute("teamsAll", teamRepository.findAll());
        redirectAttributes.addFlashAttribute("messageGood", "Match edited!");
        return "redirect:/matches/{id}/see";
    }

    //GET ADD ONE MATCH
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/add")
    String showMatch(Model model) {
        model.addAttribute("match", new Match());
        model.addAttribute("teamsAll", teamRepository.findAll());
        model.addAttribute("modeType", "add");
        return "match-add";
    }

    //GET EDIT ONE MATCH
    @Secured("ROLE_USER")
    @GetMapping(value = "{id}/edit")
    String editMatch(Model model, @PathVariable int id) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        model.addAttribute("match", match);
        model.addAttribute("teamHome", match.getHomeTeam());
        model.addAttribute("teamAway", match.getAwayTeam());
        model.addAttribute("modeType", "edit");
        return "match-add";
    }

    //POST EDIT MATCH GOAL
    @Secured("ROLE_USER")
    @PostMapping(params = "editGoalU", value = "/{id}/goals/{idGoal}/edit")
    String editGoalA(@ModelAttribute("goal") @Valid Goal current,
                     @PathVariable int id,
                     @PathVariable int idGoal,
                     BindingResult bindingResult,
                     Model model,
                     RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "goal-add";
        }
        Goal goal = goalRepository.findById(idGoal)
                .orElseThrow(()-> new IllegalStateException("Not found this goal id"));
        goal.setPlayer(current.getPlayer());
        goal.setMinute(current.getMinute());

        goalRepository.save(goal);

        redirectAttributes.addFlashAttribute("messageGood", "Goal edited! ");
        return "redirect:/matches/{id}/see";
    }

    //GET EDIT MATCH GOAL
    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/goals/{idGoal}/edit")
    String editGoalAway(Model model, @PathVariable int id, @PathVariable int idGoal) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        Goal goal = goalRepository.findById(idGoal)
                .orElseThrow(()-> new IllegalStateException("Not found this goal id"));

        if(goal.getPlayer().getTeam().getId() == match.getHomeTeam().getId()) {
            model.addAttribute("players", playerRepository.findAllByTeam_Id(match.getHomeTeam().getId()));
        }
        else {
            model.addAttribute("players", playerRepository.findAllByTeam_Id(match.getAwayTeam().getId()));
        }

        model.addAttribute("goal", goal);
        model.addAttribute("match", match);
        model.addAttribute("modeType", "editMatch");
        return "goal-add";
    }

    //GET ADD HOME GOAL
    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/home/add")
    String addGoalHome(Model model,
                       @PathVariable int id,
                       RedirectAttributes redirectAttributes
    ) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        List<Goal> goals = goalRepository.findAllByMatch_Id(id);
        Integer goalsHomeCount = 0;
        for(Goal goal : goals) {
            if(goal.getPlayer().getTeam().getId() == match.getHomeTeam().getId()) {
                goalsHomeCount++;
            }
        }
        if(goalsHomeCount >= match.getHomeScore()) {
            redirectAttributes.addFlashAttribute("messageBad", "Too many goals, delete some!");
            return "redirect:/matches/{id}/see";
        }

        model.addAttribute("goal", new Goal());
        model.addAttribute("match", match);
        model.addAttribute("playersHome", playerRepository.findAllByTeam_Id(match.getHomeTeam().getId()));
        model.addAttribute("modeType", "add");
        return "goal-add";
    }

    //GET ADD AWAY GOAL
    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/away/add")
    String addGoalAway(Model model,
                       @PathVariable int id,
                       RedirectAttributes redirectAttributes
    ) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));
        List<Goal> goals = goalRepository.findAllByMatch_Id(id);

        Integer goalsAwayCount = 0;
        for(Goal goal : goals) {
            if(goal.getPlayer().getTeam().getId() == match.getAwayTeam().getId()) {
                goalsAwayCount++;
            }
        }
        if(goalsAwayCount >= match.getAwayScore()) {
            redirectAttributes.addFlashAttribute("messageBad", "Too many goals, delete some!");
            return "redirect:/matches/{id}/see";
        }

        model.addAttribute("goal", new Goal());
        model.addAttribute("match", match);
        model.addAttribute("playersAway", playerRepository.findAllByTeam_Id(match.getAwayTeam().getId()));
        model.addAttribute("modeType", "add");
        return "goal-add";
    }

    //GET SEE ONE
    @GetMapping(value = "/{id}/see")
    String showMatch(Model model, @PathVariable int id) {
        Match match = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        List<Goal> goals = goalRepository.findByMatchIdOrderByMinuteAsc(match.getId());
        Integer goalsHomeCount = 0;
        Integer goalsAwayCount = 0;
        for(Goal goal : goals) {
            if(goal.getPlayer().getTeam().getId() == match.getHomeTeam().getId()) {
                goalsHomeCount++;
            }
            else goalsAwayCount++;
        }
        if(goalsHomeCount == match.getHomeScore()) {
            model.addAttribute("modeType", "homeFull");
        }
        if(goalsAwayCount == match.getAwayScore()) {
            model.addAttribute("modeTypeAway", "awayFull");
        }

        model.addAttribute("match", match);
        model.addAttribute("goalsByMatchId", goalRepository.findByMatchIdOrderByMinuteAsc(id));
        return "match";
    }

    //GET SEE ALL
    @GetMapping
    String showAllMatches(Model model) {
        model.addAttribute("matchesAll", repository.findAll());
        return "matches";
    }

/////////////////////////////////////////////////////////  TABLES
    //GET SEE ALL TABLES
    @GetMapping(value = "/table/{kind}")
    String showAllMatchesTable(Model model, @PathVariable String kind) {
        String modeType = kind;
        if(!modeType.equals("all") && !modeType.equals("home") && !modeType.equals("away")) {
            modeType = "all";
        }

        List<String[]> tableQuery;
        if (modeType.equals("home")) {
            tableQuery = repository.getTableHome();
        }
        else if (modeType.equals("away")) {
            tableQuery =  repository.getTableAway();
        }
        else {
            tableQuery =  repository.getFullTable();
        }

        model.addAttribute("matchFullTable", tableQuery);
        model.addAttribute("modeType", modeType);
        return "tables";
    }
}
