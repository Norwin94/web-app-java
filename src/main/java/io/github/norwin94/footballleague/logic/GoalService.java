

package io.github.norwin94.footballleague.logic;

import io.github.norwin94.footballleague.model.GoalRepository;
import io.github.norwin94.footballleague.model.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class GoalService {

    private GoalRepository repository;

    public GoalService(GoalRepository repository) {
        this.repository = repository;
    }
}
