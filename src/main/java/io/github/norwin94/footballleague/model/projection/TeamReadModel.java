package io.github.norwin94.footballleague.model.projection;

import io.github.norwin94.footballleague.model.Team;

import java.sql.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class TeamReadModel {
    private String teamName;
    private Date founded;

    private Set<TeamPlayerReadModel> players;

    public TeamReadModel(Team source) {
        teamName = source.getTeamName();
        founded = source.getFounded();
        players = source.getPlayers().stream()
                    .map(TeamPlayerReadModel::new)
                    .collect(Collectors.toSet());
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

    public Set<TeamPlayerReadModel> getPlayers() {
        return players;
    }

    public void setPlayers(Set<TeamPlayerReadModel> players) {
        this.players = players;
    }
}
