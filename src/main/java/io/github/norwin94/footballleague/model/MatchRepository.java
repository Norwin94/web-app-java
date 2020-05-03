
package io.github.norwin94.footballleague.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    List<Match> findAll();

    Page<Match> findAll(Pageable page);

    Optional<Match> findById(Integer i);

    Match save(Match entity);

    void deleteById(Integer id);
}
