package com.skilluser.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class AssessmentCheckRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean hasGivenAnyAssessment(Long studentId) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM Skillpilots_BasicAssessment
                WHERE student_id = ?
            )
        """;

        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(sql, Boolean.class, studentId)
        );
    }
}
