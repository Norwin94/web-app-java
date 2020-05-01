package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.PlayerRepository;
import io.github.norwin94.footballleague.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlPlayerRepository extends PlayerRepository, JpaRepository<Player, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from player where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    List<Player> findAllByTeam_Id(Integer teamId);

    @Query("select u.team from Player u where u.id=:id")
    Optional<Team> findByTeam(@Param("id") Integer id);
}
