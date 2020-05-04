package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.Goal;
import io.github.norwin94.footballleague.model.GoalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlGoalRepository extends GoalRepository, JpaRepository<Goal, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from goal where id=:id")
    boolean existsById(@Param("id") Integer id);

}
