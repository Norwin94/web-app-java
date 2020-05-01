package io.github.norwin94.footballleague.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    List<Team> findAll();

    Page<Team> findAll(Pageable page);

    Optional<Team> findById(Integer i);

    boolean existsById(Integer id);

    Team save(Team entity);

    List<Player> findPlayersById(Integer id);

    void deleteById(Integer id);
}
