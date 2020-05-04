
package io.github.norwin94.footballleague.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface GoalRepository {
    List<Goal> findAll();

    List<Goal> findByMatchId(Integer i);

    Long countByMatchId(Integer i);

    Page<Goal> findAll(Pageable page);

    Optional<Goal> findById(Integer i);

    Goal save(@Valid Goal entity);

    void deleteById(Integer id);
}
