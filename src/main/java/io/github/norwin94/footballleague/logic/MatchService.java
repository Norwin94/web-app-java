

package io.github.norwin94.footballleague.logic;

import io.github.norwin94.footballleague.model.MatchRepository;
import io.github.norwin94.footballleague.model.Match;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class MatchService {

    private MatchRepository repository;

    public MatchService(MatchRepository repository) {
        this.repository = repository;
    }
}
