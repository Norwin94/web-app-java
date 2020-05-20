package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.Goal;
import io.github.norwin94.footballleague.model.GoalRepository;
import io.github.norwin94.footballleague.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlGoalRepository extends GoalRepository, JpaRepository<Goal, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from goal where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    List<Goal> findAllByMatch_Id(Integer matchId);

    @Override
    List<Goal> findAllByPlayer_IdOrderByMatch_IdAscMinuteAsc(Integer matchId);

    //@Override
    //List<Goal> findAllOrderByMatch_IdAscMinuteAsc();
}
