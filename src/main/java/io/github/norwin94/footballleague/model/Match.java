

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
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   // @NotBlank(message = "Match must have two teams!")
    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "id")
    private Team homeTeam;
    @ManyToOne
    @JoinColumn(name = "away_team_id", referencedColumnName = "id")
    private Team awayTeam;
    private Date matchDate;

    public Match() {

    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }
}

