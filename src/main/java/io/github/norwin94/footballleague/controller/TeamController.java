package io.github.norwin94.footballleague.controller;

import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.PlayerRepository;
import io.github.norwin94.footballleague.model.Team;
import io.github.norwin94.footballleague.model.TeamRepository;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    private final TeamRepository repository;
    private final PlayerRepository playerRepository;

     TeamController(final TeamRepository repository , PlayerRepository playerRepository) {
        this.repository = repository;
         this.playerRepository = playerRepository;
     }

    //POST ADD TEAM
    @Secured("ROLE_ADMIN")
    @PostMapping(params = "addTeam", value = "/add")
    String addTeam(@ModelAttribute("team") @Valid Team current,
                   BindingResult bindingResult,
                   Model model,
                   RedirectAttributes redirectAttributes
    ) {
        if(bindingResult.hasErrors()) {
            return "redirect:/teams/add";
        }

        for (Player temp : current.getPlayers()) {
            temp.setTeam(current);
        }
        repository.save(current);
        model.addAttribute("team", new Team());
        model.addAttribute("teamsAll", repository.findAll());

        redirectAttributes.addFlashAttribute("message", "Team added!");
        return "redirect:/teams";
     }

    //POST ADD PLAYER TO TEAM
    @Secured("ROLE_ADMIN")
    @PostMapping(params = "addPlayer", value = "/add")
    String addPlayer(@ModelAttribute("team") Team current,
                     Model model,
                     BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "redirect:/teams/add";
        }
        Player player = new Player();
        player.setFirstName("Firstname");
        player.setLastName("Lastname");
        player.setBirthDate(Date.valueOf("2000-01-01"));
        player.setTeam(current);

        current.getPlayers().add(player);
        return "team-add";
    }

    //POST DELETE
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/{id}/del")
    String deleteTeam(@PathVariable Integer id,
                      Model model,
                      RedirectAttributes redirectAttributes
    ) {
        Team team = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        for (Player temp : team.getPlayers()) {
            temp.setTeam(null);
        }
        repository.deleteById(team.getId());

        model.addAttribute("teamsAll", repository.findAll());
        redirectAttributes.addFlashAttribute("message", "Team deleted!");
        return "redirect:/teams";
    }

    //POST EDIT
    @Secured("ROLE_USER")
    @PostMapping(params = "editTeam", value = "/{id}/edit")
    String editTeam(@ModelAttribute("team") @Valid Team toUpdate,
                    Model model,
                    @PathVariable Integer id,
                    RedirectAttributes redirectAttributes
    ) {
        // Team temp = repository.findById(id).get();
        // temp.updateFrom(toUpdate);
        repository.save(toUpdate);
        model.addAttribute("teamsAll", repository.findAll());
        redirectAttributes.addFlashAttribute("messageGood", "Team edited!");
        return "redirect:/teams";
    }

    //GET ADD
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/add")
    String showTeams(Model model) {
        model.addAttribute("playersAll", playerRepository.findAll());
        Team team = new Team();
        team.setFounded(Date.valueOf("2000-01-01"));
        team.setTeamName("default");
        model.addAttribute("team", team);
        return "team-add";
    }

    //GET EDIT
    @Secured("ROLE_USER")
    @GetMapping(value = "/{id}/edit")
    String showTeamToEdit(Model model, @PathVariable int id) {
        Team team = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        model.addAttribute("team", team);
        model.addAttribute("actualPage", "teams");
        model.addAttribute("modeType", "edit");
        return "team";
    }

    //GET SEE ONE
    @Timed(value = "teams.show.team", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @GetMapping(value = "/{id}/see")
    String showTeam(Model model, @PathVariable int id) {
        Team team = repository.findById(id)
                .orElseThrow(()-> new IllegalStateException("Not found this id"));

        model.addAttribute("team", team);
        model.addAttribute("playersAll", playerRepository.findAll());
        model.addAttribute("actualPage", "teams");
        model.addAttribute("modeType", "see");
        return "team";
    }

    //GET ALL
    @GetMapping
    String showAllTeams(Model model) {
        model.addAttribute("teamsAll", repository.findAll());
        return "teams";
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



/*
    @GetMapping(value = "/{id}/edit")
    String editTeam(@PathVariable int id, Model model) {
        //model.addAttribute("playersAll", playerRepository.findAll());
        model.addAttribute("team", repository.findById(id));
        return "add";
    }
*/

    //nie dziala, gdy teamName is null, bo teamName w Team ma @NotBlank
    //@Transactional tutaj dodal, rollbackuje gdy nie przejdzie cale
    @PatchMapping(value = "/{id}/edit")
    String editTeam(@PathVariable int id, @ModelAttribute("team") @Valid Team toUpdate,
                    BindingResult bindingResult,
                    Model model
    ) {
        if(!repository.existsById(id)){
            bindingResult.addError(new ObjectError("Teams error", "There is no team for this id"));
        }
        Team team = repository.findById(id).get();
        if(!toUpdate.getTeamName().isEmpty()) {
            team.setTeamName(toUpdate.getTeamName());
        }
        if(!toUpdate.getFounded().toString().isEmpty()) {
            team.setFounded(toUpdate.getFounded());
        }
        repository.save(team);

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
}
