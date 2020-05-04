package io.github.norwin94.footballleague.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Player must have first name!")
    private String firstName;
    @NotBlank(message = "Player must have last name!")
    private String lastName;
    private Date birthDate;
    //@Embedded
    //private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"players", "awayTeamMatches", "homeTeamMatches"})
    private Team team;


    public Player() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void updateFrom(Player source) {
        firstName = source.firstName;
        lastName = source.lastName;
        birthDate = source.birthDate;
        team = source.team;
    }
}
