package org.example.storage.film.Db;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Mpa;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.storage.MpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
public class MpaDaoDb implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean noExists(int mpaId) {
        String sql = "select count (*) from mpa_ratings where RATING_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, mpaId);
        return result == 0;
    }

    @Override
    public Mpa getMpaFromDb(int mpaId) {
        if (noExists(mpaId)) {
            log.debug("Ошибка при получении MPA {}", mpaId);
            throw new NotFoundException(String.format("Не обнаружен MPA с id: {}", mpaId));
        }
        String sql = "select rating_id, rating from mpa_ratings where rating_id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeMpa, mpaId);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "select * from MPA_RATINGS";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder().
                id(resultSet.getInt("rating_id")).
                name(resultSet.getString("rating"))
                .build();
    }
}
