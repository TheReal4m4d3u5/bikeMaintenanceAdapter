import { useEffect, useState } from "react";

import AccountBar from "../components/AccountBar.jsx";

import {
    closeWorkOrder,
    getMyWorkOrders,
    startWork,
} from "../api/workOrderApi.js";

import {
    getMaintenanceIssues,
    resolveMaintenanceIssue,
} from "../api/maintenanceIssueApi.js";

import "../App.css";

function TechnicianDashboard() {
    const [workOrders, setWorkOrders] = useState([]);
    const [maintenanceIssues, setMaintenanceIssues] =
        useState([]);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadDashboard();
    }, []);

    async function loadDashboard() {
        try {
            setLoading(true);
            setError("");

            const [
                storedWorkOrders,
                storedMaintenanceIssues,
            ] = await Promise.all([
                getMyWorkOrders(),
                getMaintenanceIssues(),
            ]);

            setWorkOrders(storedWorkOrders);
            setMaintenanceIssues(
                storedMaintenanceIssues
            );
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    function replaceWorkOrder(updatedWorkOrder) {
        setWorkOrders((currentWorkOrders) =>
            currentWorkOrders.map((workOrder) =>
                workOrder.workOrderId ===
                updatedWorkOrder.workOrderId
                    ? updatedWorkOrder
                    : workOrder
            )
        );
    }

    function replaceMaintenanceIssue(updatedIssue) {
        setMaintenanceIssues((currentIssues) =>
            currentIssues.map((issue) =>
                issue.maintenanceIssueId ===
                updatedIssue.maintenanceIssueId
                    ? updatedIssue
                    : issue
            )
        );
    }

    async function handleStartWork(workOrderId) {
        try {
            setError("");

            const updatedWorkOrder =
                await startWork(workOrderId);

            replaceWorkOrder(updatedWorkOrder);
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    async function handleCloseWorkOrder(workOrderId) {
        try {
            setError("");

            const updatedWorkOrder =
                await closeWorkOrder(workOrderId);

            replaceWorkOrder(updatedWorkOrder);
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    async function handleResolveIssue(
        maintenanceIssueId
    ) {
        try {
            setError("");

            const updatedIssue =
                await resolveMaintenanceIssue(
                    maintenanceIssueId
                );

            replaceMaintenanceIssue(updatedIssue);
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    const activeIssues = maintenanceIssues.filter(
        (issue) => issue.status !== "RESOLVED"
    );

    return (
        <main className="app">
            <AccountBar />

            <header className="page-header">
                <div className="page-header-content">
                    <div>
                        <p className="eyebrow">
                            Bike Maintenance Portal
                        </p>

                        <h1>Technician Dashboard</h1>

                        <p className="subtitle">
                            Review maintenance work, begin
                            repairs, and close completed work
                            orders.
                        </p>
                    </div>
                </div>
            </header>

            {error && (
                <div
                    className="error-message"
                    role="alert"
                >
                    {error}
                </div>
            )}

            {loading ? (
                <p className="empty-state">
                    Loading technician dashboard...
                </p>
            ) : (
                <>
                    <section className="work-order-section">
                        <article className="panel">
                            <div className="panel-header">
                                <div>
                                    <h2>Maintenance work orders</h2>

                                    <p className="panel-description">
                                        Start assigned work and
                                        close completed repairs.
                                    </p>
                                </div>

                                <button
                                    className="secondary-button"
                                    type="button"
                                    onClick={loadDashboard}
                                >
                                    Refresh
                                </button>
                            </div>

                            {workOrders.length === 0 ? (
                                <p className="empty-state">
                                    No work orders are available.
                                </p>
                            ) : (
                                <div className="work-order-list">
                                    {workOrders.map(
                                        (workOrder) => (
                                            <article
                                                className="work-order-card"
                                                key={
                                                    workOrder.workOrderId
                                                }
                                            >
                                                <div className="bike-card-header">
                                                    <div>
                                                        <h3>
                                                            {
                                                                workOrder.workOrderId
                                                            }
                                                        </h3>

                                                        <p className="bike-card-model">
                                                            Bike:{" "}
                                                            {
                                                                workOrder.bikeId
                                                            }
                                                        </p>
                                                    </div>

                                                    <span className="condition-badge">
                                                        {workOrder.status.replaceAll(
                                                            "_",
                                                            " "
                                                        )}
                                                    </span>
                                                </div>

                                                <p>
                                                    {
                                                        workOrder.description
                                                    }
                                                </p>

												<p>
												    <strong>Technician ID:</strong>{" "}
												    {workOrder.assignedTechnicianId ||
												        "Not assigned"}
												</p>

                                                <div className="work-order-actions">
                                                    {workOrder.status !==
                                                        "IN_PROGRESS" &&
                                                        workOrder.status !==
                                                            "CLOSED" && (
                                                            <button
                                                                className="secondary-button"
                                                                type="button"
                                                                onClick={() =>
                                                                    handleStartWork(
                                                                        workOrder.workOrderId
                                                                    )
                                                                }
                                                            >
                                                                Start work
                                                            </button>
                                                        )}

                                                    {workOrder.status !==
                                                        "CLOSED" && (
                                                        <button
                                                            className="primary-button"
                                                            type="button"
                                                            onClick={() =>
                                                                handleCloseWorkOrder(
                                                                    workOrder.workOrderId
                                                                )
                                                            }
                                                        >
                                                            Close work order
                                                        </button>
                                                    )}
                                                </div>
                                            </article>
                                        )
                                    )}
                                </div>
                            )}
                        </article>
                    </section>

                    <section className="maintenance-issue-section">
                        <article className="panel">
                            <div className="panel-header">
                                <div>
                                    <h2>Open maintenance issues</h2>

                                    <p className="panel-description">
                                        Review unresolved fleet
                                        maintenance reports.
                                    </p>
                                </div>
                            </div>

                            {activeIssues.length === 0 ? (
                                <p className="empty-state">
                                    No open maintenance issues.
                                </p>
                            ) : (
                                <div className="maintenance-issue-list">
                                    {activeIssues.map((issue) => (
                                        <article
                                            className="maintenance-issue-card"
                                            key={
                                                issue.maintenanceIssueId
                                            }
                                        >
                                            <div className="bike-card-header">
                                                <div>
                                                    <h3>
                                                        {
                                                            issue.maintenanceIssueId
                                                        }
                                                    </h3>

                                                    <p className="bike-card-model">
                                                        Bike:{" "}
                                                        {issue.bikeId}
                                                    </p>
                                                </div>

                                                <span className="condition-badge">
                                                    {issue.status.replaceAll(
                                                        "_",
                                                        " "
                                                    )}
                                                </span>
                                            </div>

                                            <p>
                                                {issue.description}
                                            </p>

                                            <div className="issue-details">
                                                <p>
                                                    <strong>
                                                        Source:
                                                    </strong>{" "}
                                                    {issue.sourceType.replaceAll(
                                                        "_",
                                                        " "
                                                    )}
                                                </p>

                                                <p>
                                                    <strong>
                                                        Severity:
                                                    </strong>{" "}
                                                    {issue.severity}
                                                </p>
                                            </div>

                                            <button
                                                className="secondary-button"
                                                type="button"
                                                onClick={() =>
                                                    handleResolveIssue(
                                                        issue.maintenanceIssueId
                                                    )
                                                }
                                            >
                                                Resolve issue
                                            </button>
                                        </article>
                                    ))}
                                </div>
                            )}
                        </article>
                    </section>
                </>
            )}
        </main>
    );
}

export default TechnicianDashboard;