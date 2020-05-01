package io.github.norwin94.footballleague.logic;

import io.github.norwin94.footballleague.model.Player;
import io.github.norwin94.footballleague.model.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
public class PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);
    private final PlayerRepository repository;


    public PlayerService(final PlayerRepository repository) {
        this.repository = repository;
    }

    @Async
    public CompletableFuture<List<Player>> findAllAsync() {
        logger.info("Supply find!");
        return CompletableFuture.supplyAsync(repository::findAll);
    }
}
