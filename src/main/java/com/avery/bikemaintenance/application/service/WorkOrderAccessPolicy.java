package com.avery.bikemaintenance.application.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.avery.bikemaintenance.domain.model.AuthenticatedUser;
import com.avery.bikemaintenance.domain.model.UserRole;
import com.avery.bikemaintenance.domain.model.WorkOrder;

@Component
public class WorkOrderAccessPolicy {

    public void verifyCanModify(
            AuthenticatedUser user,
            WorkOrder workOrder) {

        if (user.role() == UserRole.ADMIN) {
            return;
        }

        boolean assignedTechnician =
                user.role() == UserRole.TECHNICIAN
                && user.userId().equals(
                        workOrder
                                .getAssignedTechnicianId());

        if (!assignedTechnician) {
            throw new AccessDeniedException(
                    "You are not assigned to this work order.");
        }
    }
}
