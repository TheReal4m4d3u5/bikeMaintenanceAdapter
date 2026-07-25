package com.avery.bikemaintenance.adapter.outbound.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.avery.bikemaintenance.application.exception.RepositoryException;
import com.avery.bikemaintenance.application.port.outbound.WorkOrderRepository;
import com.avery.bikemaintenance.domain.model.WorkOrder;

public class SqlWorkOrderRepositoryAdapter
        implements WorkOrderRepository {

    private static final String SELECT_COLUMNS = """
            SELECT work_order_id,
                   bike_id,
                   maintenance_issue_id,
                   description,
                   assigned_technician_id,
                   status,
                   created_date
            FROM work_orders
            """;

    private final JdbcTemplate jdbcTemplate;

    public SqlWorkOrderRepositoryAdapter(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<WorkOrder> findById(
            String workOrderId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE work_order_id = ?",
                workOrderId)
                .stream()
                .findFirst();
    }

    @Override
    public List<WorkOrder> findAll() {
        return query(
                SELECT_COLUMNS
                        + " ORDER BY work_order_id");
    }

    @Override
    public List<WorkOrder> findByBikeId(
            String bikeId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE bike_id = ?"
                        + " ORDER BY work_order_id",
                bikeId);
    }

    @Override
    public List<WorkOrder>
            findByAssignedTechnicianId(
                    String assignedTechnicianId) {

        return query(
                SELECT_COLUMNS
                        + " WHERE assigned_technician_id = ?"
                        + " ORDER BY work_order_id",
                assignedTechnicianId);
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        try {
            int updated = jdbcTemplate.update(
                    """
                    UPDATE work_orders
                    SET bike_id = ?,
                        maintenance_issue_id = ?,
                        description = ?,
                        assigned_technician_id = ?,
                        status = ?,
                        created_date = ?
                    WHERE work_order_id = ?
                    """,
                    workOrder.getBikeId(),
                    workOrder.getMaintenanceIssueId(),
                    workOrder.getDescription(),
                    workOrder.getAssignedTechnicianId(),
                    workOrder.getStatus(),
                    workOrder.getCreatedDate(),
                    workOrder.getWorkOrderId());

            if (updated == 0) {
                jdbcTemplate.update(
                        """
                        INSERT INTO work_orders (
                            work_order_id,
                            bike_id,
                            maintenance_issue_id,
                            description,
                            assigned_technician_id,
                            status,
                            created_date
                        )
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """,
                        workOrder.getWorkOrderId(),
                        workOrder.getBikeId(),
                        workOrder.getMaintenanceIssueId(),
                        workOrder.getDescription(),
                        workOrder.getAssignedTechnicianId(),
                        workOrder.getStatus(),
                        workOrder.getCreatedDate());
            }

            return workOrder;
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to save work order "
                            + workOrder.getWorkOrderId(),
                    exception);
        }
    }

    @Override
    public boolean existsById(String workOrderId) {
        try {
            Integer count =
                    jdbcTemplate.queryForObject(
                            """
                            SELECT COUNT(*)
                            FROM work_orders
                            WHERE work_order_id = ?
                            """,
                            Integer.class,
                            workOrderId);

            return count != null && count > 0;
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to check work order "
                            + workOrderId,
                    exception);
        }
    }

    private List<WorkOrder> query(
            String sql,
            Object... arguments) {

        try {
            return jdbcTemplate.query(
                    sql,
                    this::mapWorkOrder,
                    arguments);
        } catch (DataAccessException exception) {
            throw failure(
                    "Unable to retrieve work orders.",
                    exception);
        }
    }

    private WorkOrder mapWorkOrder(
            ResultSet resultSet,
            int rowNumber)
            throws SQLException {

        return new WorkOrder(
                resultSet.getString("work_order_id"),
                resultSet.getString("bike_id"),
                resultSet.getString(
                        "maintenance_issue_id"),
                resultSet.getString("description"),
                resultSet.getString(
                        "assigned_technician_id"),
                resultSet.getString("status"),
                resultSet.getDate(
                        "created_date")
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
