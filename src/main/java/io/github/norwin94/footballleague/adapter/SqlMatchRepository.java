package io.github.norwin94.footballleague.adapter;

import io.github.norwin94.footballleague.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SqlMatchRepository extends MatchRepository, JpaRepository<Match, Integer> {
    @Override
    @Query(nativeQuery = true, value = "select count(*) > 0 from match where id=:id")
    boolean existsById(@Param("id") Integer id);

}
