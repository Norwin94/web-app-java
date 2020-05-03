package io.github.norwin94.footballleague.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Team must have name!")
    private String teamName;
    private Date founded;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}) //(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
    @OneToMany(mappedBy="awayTeam")
    private List<Match> awayTeamMatches = new ArrayList<>();
    @OneToMany(mappedBy="homeTeam")
    private List<Match> homeTeamMatches = new ArrayList<>();


    public List<Match> getAwayTeamMatches() {
        return awayTeamMatches;
    }

    public void setAwayTeamMatches(List<Match> awayTeamMatches) {
        this.awayTeamMatches = awayTeamMatches;
    }



    public List<Match> getHomeTeamMatches() {
        return homeTeamMatches;
    }

    public void setHomeTeamMatches(List<Match> homeTeamMatches) {
        this.homeTeamMatches = homeTeamMatches;
    }

    public Team() {

    }

    public List<Player> getPlayers() {
        return  players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Date getFounded() {
        return founded;
    }

    public void setFounded(Date founded) {
        this.founded = founded;
    }

    public void updateFrom(Team source) {
        teamName = source.teamName;
        founded = source.founded;
        players = source.players;
    }

    public Team addPlayer(Player player) {
        this.players.add(player);
        player.setTeam(this);
        return this;
    }
}