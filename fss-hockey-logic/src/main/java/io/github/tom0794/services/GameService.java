package io.github.tom0794.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tom0794.database.DbOperations;
import io.github.tom0794.objects.Game;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.github.tom0794.ObjectMapperUtils.createDatabaseMapper;

@Service
public class GameService {

    private final ObjectMapper dbMapper;
    private final ObjectMapper responseMapper;

    public GameService(@Qualifier("dbMapper") ObjectMapper dbMapper,
                       @Qualifier("responseMapper") ObjectMapper responseMapper) {
        this.dbMapper = dbMapper;
        this.responseMapper = responseMapper;
    }

    public Map<String, Object> prepareForDb(Game game) {
        return dbMapper.convertValue(game, new TypeReference<Map<String, Object>>() {});
    }

    public String prepareForResponse(Object entity) throws JsonProcessingException {
        return responseMapper.writeValueAsString(entity);
    }

    public void createGame(Game game) {
        Map<String, Object> mapObj = dbMapper.convertValue(game, new TypeReference<Map<String, Object>>() {});
        game.setGameId(DbOperations.insert(game.getClass().getSimpleName(), (HashMap<String, Object>) mapObj));
    }

    public void updateGame(Game game) {
        Map<String, Object> mapObj = dbMapper.convertValue(game, new TypeReference<Map<String, Object>>() {});
        DbOperations.update("game", "gameId", (HashMap<String, Object>) mapObj);
    }
}

