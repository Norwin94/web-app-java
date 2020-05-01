package io.github.norwin94.footballleague.logic;

import io.github.norwin94.footballleague.model.Team;
import io.github.norwin94.footballleague.model.TeamRepository;
import io.github.norwin94.footballleague.model.projection.TeamReadModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TeamService {

    private TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public List<TeamReadModel> readAll() {
        return repository.findAll().stream()
                .map(TeamReadModel::new)
                .collect(Collectors.toList());
    }
}
