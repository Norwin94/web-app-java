package io.github.norwin94.footballleague.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    List<Player> findAll();

    boolean existsById(Integer id);

    Page<Player> findAll(Pageable page);

    Optional<Player> findById(Integer id);

    Player save(Player entity);

    List<Player> findAllByTeam_Id(Integer teamId);

    Optional<Team>  findByTeam(Integer id);

    void deleteById(Integer id);
}
