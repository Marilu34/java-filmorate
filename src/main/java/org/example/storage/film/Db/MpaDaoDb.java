package org.example.storage.film.Db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Mpa;
import org.example.exceptions.NotFoundException;
import org.example.storage.film.storage.MpaDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaDaoDb implements MpaDao {
    private final JdbcTemplate jdbcTemplate;


    private boolean noExists(int mpaId) {
        String sql = "select count (*) from mpa where MPA_ID = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, mpaId);
        return result == 0;
    }

    @Override
    public Mpa getMpaFromDb(int mpaId) {
        if (noExists(mpaId)) {
            log.debug("Ошибка при получении MPA {}", mpaId);
            throw new NotFoundException(String.format("Не обнаружен MPA с id: {}", mpaId));
        }
        String sql = "select MPA_ID, rating from mpa where MPA_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::makeMpa, mpaId);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "select * from MPA";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("rating"))
                .build();
    }
}
