//tutaj mozemy tworzyc zapytania SQL dzieki nazwom metod (a'la DSL)

package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.Team;
import io.github.norwin94.footballleague.model.TeamRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 interface SqlTeamRepository extends TeamRepository, JpaRepository<Team, Integer> {
     @Override
     @Query(nativeQuery = true, value = "select count(*) > 0 from team where id=:id")
     boolean existsById(@Param("id") Integer id);

     @Override
     @Query("select t.teamName, p.firstName from Team t join t.players p")
     List<Player> findPlayersById(Integer id);
}
