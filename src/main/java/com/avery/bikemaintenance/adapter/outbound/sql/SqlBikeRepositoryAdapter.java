package com.avery.bikemaintenance.adapter.outbound.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.avery.bikemaintenance.application.exception.RepositoryException;
import com.avery.bikemaintenance.application.port.outbound.BikeRepository;
import com.avery.bikemaintenance.domain.model.Bike;

public class SqlBikeRepositoryAdapter
        implements BikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public SqlBikeRepositoryAdapter(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Bike> findById(String bikeId) {
        try {
            return jdbcTemplate.query(
                    """
                    SELECT bike_id, model, condition,
                           ride_count, mileage
                    FROM bikes
                    WHERE bike_id = ?
                    """,
                    this::mapBike,
                    bikeId)
                    .stream()
                    .findFirst();
        } catch (DataAccessException exception) {
            throw repositoryFailure(
                    "Unable to find bike " + bikeId,
                    exception);
        }
    }

    @Override
    public List<Bike> findAll() {
        try {
            return jdbcTemplate.query(
                    """
                    SELECT bike_id, model, condition,
                           ride_count, mileage
                    FROM bikes
                    ORDER BY bike_id
                    """,
                    this::mapBike);
        } catch (DataAccessException exception) {
            throw repositoryFailure(
                    "Unable to retrieve bikes.",
                    exception);
        }
    }

    @Override
    public Bike save(Bike bike) {
        try {
            int updated = jdbcTemplate.update(
                    """
                    UPDATE bikes
                    SET model = ?,
                        condition = ?,
                        ride_count = ?,
                        mileage = ?
                    WHERE bike_id = ?
                    """,
                    bike.getModel(),
                    bike.getCondition(),
                    bike.getRideCount(),
                    bike.getMileage(),
                    bike.getBikeId());

            if (updated == 0) {
                jdbcTemplate.update(
                        """
                        INSERT INTO bikes (
                            bike_id,
                            model,
                            condition,
                            ride_count,
                            mileage
                        )
                        VALUES (?, ?, ?, ?, ?)
                        """,
                        bike.getBikeId(),
                        bike.getModel(),
                        bike.getCondition(),
                        bike.getRideCount(),
                        bike.getMileage());
            }

            return bike;
        } catch (DataAccessException exception) {
            throw repositoryFailure(
                    "Unable to save bike "
                            + bike.getBikeId(),
                    exception);
        }
    }

    @Override
    public boolean existsById(String bikeId) {
        try {
            Integer count =
                    jdbcTemplate.queryForObject(
                            """
                            SELECT COUNT(*)
                            FROM bikes
                            WHERE bike_id = ?
                            """,
                            Integer.class,
                            bikeId);

            return count != null && count > 0;
        } catch (DataAccessException exception) {
            throw repositoryFailure(
                    "Unable to check bike "
                            + bikeId,
                    exception);
        }
    }

    private Bike mapBike(
            ResultSet resultSet,
            int rowNumber)
            throws SQLException {

        return new Bike(
                resultSet.getString("bike_id"),
                resultSet.getString("model"),
                resultSet.getString("condition"),
                resultSet.getInt("ride_count"),
                resultSet.getDouble("mileage"));
    }

    private static RepositoryException
            repositoryFailure(
                    String message,
                    DataAccessException cause) {

        return new RepositoryException(
                message,
                cause);
    }
}
