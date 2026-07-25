package com.avery.bikemaintenance.adapter.outbound.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.avery.bikemaintenance.application.exception.RepositoryException;
import com.avery.bikemaintenance.application.port.outbound.MaintenanceIssueRepository;
import com.avery.bikemaintenance.domain.model.MaintenanceIssue;

public class SqlMaintenanceIssueRepositoryAdapter
        implements MaintenanceIssueRepository {

    private static final String SELECT_COLUMNS = """
            SELECT maintenance_issue_id,
                   bike_id,
                   reported_by_user_id,
                   source_type,
                   description,
                   severity,
                   status,
                   reported_date
            FROM maintenance_issues
            """;

    private final JdbcTemplate jdbcTemplate;

    public SqlMaintenanceIssueRepositoryAdapter(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<MaintenanceIssue> findById(
            String maintenanceIssueId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE maintenance_issue_id = ?",
                maintenanceIssueId)
                .stream()
                .findFirst();
    }

    @Override
    public List<MaintenanceIssue> findAll() {
        return query(
                SELECT_COLUMNS
                        + " ORDER BY maintenance_issue_id");
    }

    @Override
    public List<MaintenanceIssue> findByBikeId(
            String bikeId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE bike_id = ?"
                        + " ORDER BY maintenance_issue_id",
                bikeId);
    }

    @Override
    public List<MaintenanceIssue>
            findByReportedByUserId(
                    String reportedByUserId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE reported_by_user_id = ?"
                        + " ORDER BY maintenance_issue_id",
                reportedByUserId);
    }

    @Override
    public MaintenanceIssue save(
            MaintenanceIssue issue) {

        try {
            int updated = jdbcTemplate.update(
                    """
                    UPDATE maintenance_issues
                    SET bike_id = ?,
                        reported_by_user_id = ?,
                        source_type = ?,
                        description = ?,
                        severity = ?,
                        status = ?,
                        reported_date = ?
                    WHERE maintenance_issue_id = ?
                    """,
                    issue.getBikeId(),
                    issue.getReportedByUserId(),
                    issue.getSourceType(),
                    issue.getDescription(),
                    issue.getSeverity(),
                    issue.getStatus(),
                    issue.getReportedDate(),
                    issue.getMaintenanceIssueId());

            if (updated == 0) {
                jdbcTemplate.update(
                        """
                        INSERT INTO maintenance_issues (
                            maintenance_issue_id,
                            bike_id,
                            reported_by_user_id,
                            source_type,
                            description,
                            severity,
                            status,
                            reported_date
                        )
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                        issue.getMaintenanceIssueId(),
                        issue.getBikeId(),
                        issue.getReportedByUserId(),
                        issue.getSourceType(),
                        issue.getDescription(),
                        issue.getSeverity(),
                        issue.getStatus(),
                        issue.getReportedDate());
            }

            return issue;
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to save maintenance issue "
                            + issue.getMaintenanceIssueId(),
                    exception);
        }
    }

    @Override
    public boolean existsById(
            String maintenanceIssueId) {

        try {
            Integer count =
                    jdbcTemplate.queryForObject(
                            """
                            SELECT COUNT(*)
                            FROM maintenance_issues
                            WHERE maintenance_issue_id = ?
                            """,
                            Integer.class,
                            maintenanceIssueId);

            return count != null && count > 0;
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to check maintenance issue "
                            + maintenanceIssueId,
                    exception);
        }
    }

    private List<MaintenanceIssue> query(
            String sql,
            Object... arguments) {

        try {
            return jdbcTemplate.query(
                    sql,
                    this::mapIssue,
                    arguments);
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to retrieve maintenance issues.",
                    exception);
        }
    }

    private MaintenanceIssue mapIssue(
            ResultSet resultSet,
            int rowNumber)
            throws SQLException {

        return new MaintenanceIssue(
                resultSet.getString(
                        "maintenance_issue_id"),
                resultSet.getString("bike_id"),
                resultSet.getString(
                        "reported_by_user_id"),
                resultSet.getString("source_type"),
                resultSet.getString("description"),
                resultSet.getString("severity"),
                resultSet.getString("status"),
                resultSet.getDate(
                        "reported_date")
                        .toLocalDate());
    }

    private static RepositoryException failure(
            String message,
            DataAccessException cause) {

        return new RepositoryException(
                message,
                cause);
    }
}
