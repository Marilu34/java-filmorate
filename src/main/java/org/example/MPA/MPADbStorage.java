package org.example.MPA;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Data
public class MPADbStorage {
    JdbcTemplate jdbcTemplate;
    public MPA getMpa(int mpaId) {
        String sql = "SELECT MPA_NAME FROM RATES_MPA WHERE MPA_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, mpaId);
        if (userRows.next()) {
            return new MPA(mpaId,
                    userRows.getString("mpa_name"));
        }
        else return null;
    }
}
