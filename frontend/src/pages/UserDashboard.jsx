import { useEffect, useState } from "react";

import AccountBar from "../components/AccountBar.jsx";

import { getBikes } from "../api/bikeApi.js";

import {
    createMaintenanceIssue,
    getMyMaintenanceIssues,
} from "../api/maintenanceIssueApi.js";

import "../App.css";

const emptyIssueForm = {
    bikeId: "",
    sourceType: "USER_COMPLAINT",
    description: "",
    severity: "MEDIUM",
};

function UserDashboard() {
    const [bikes, setBikes] = useState([]);
    const [myIssues, setMyIssues] = useState([]);
    const [issueForm, setIssueForm] =
        useState(emptyIssueForm);

    const [loading, setLoading] = useState(true);
    const [issuesLoading, setIssuesLoading] =
        useState(true);
    const [submitting, setSubmitting] =
        useState(false);

    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] =
        useState("");

    useEffect(() => {
        loadBikes();
        loadMyIssues();
    }, []);

    async function loadBikes() {
        try {
            setLoading(true);
            setError("");

            const storedBikes = await getBikes();
            setBikes(storedBikes);
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    async function loadMyIssues() {
        try {
            setIssuesLoading(true);
            setError("");

            const storedIssues =
                await getMyMaintenanceIssues();

            setMyIssues(storedIssues);
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setIssuesLoading(false);
        }
    }

    function handleIssueChange(event) {
        const { name, value } = event.target;

        setIssueForm((currentForm) => ({
            ...currentForm,
            [name]: value,
        }));
    }

    async function handleIssueSubmit(event) {
        event.preventDefault();

        try {
            setSubmitting(true);
            setError("");
            setSuccessMessage("");

            const savedIssue =
                await createMaintenanceIssue({
                    bikeId: issueForm.bikeId.trim(),
                    sourceType: issueForm.sourceType,
                    description:
                        issueForm.description.trim(),
                    severity: issueForm.severity,
                });

            setMyIssues((currentIssues) => [
                savedIssue,
                ...currentIssues.filter(
                    (issue) =>
                        issue.maintenanceIssueId !==
                        savedIssue.maintenanceIssueId
                ),
            ]);

            setSuccessMessage(
                `Maintenance issue ${savedIssue.maintenanceIssueId} was submitted.`
            );

            setIssueForm(emptyIssueForm);
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setSubmitting(false);
        }
    }

	const availableBikes = bikes.filter(
	    (bike) =>
            bike.condition === "AVAILABLE" ||
            bike.condition ===
                "DUE_FOR_SCHEDULED_MAINTENANCE"
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

                        <h1>User Dashboard</h1>

                        <p className="subtitle">
                            View fleet bikes and report a
                            maintenance concern.
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

            {successMessage && (
                <div role="status">
                    {successMessage}
                </div>
            )}

            <section className="user-dashboard-grid">

                <article className="panel user-fleet-panel">
                    <div className="panel-header">
                        <div>
						<h2>Available bikes</h2>

						<p className="panel-description">
						    Bikes currently available for use.
						</p>
                        </div>

                        <button
                            className="secondary-button"
                            type="button"
                            onClick={loadBikes}
                        >
                            Refresh
                        </button>
                    </div>

                    {loading ? (
                        <p className="empty-state">
                            Loading bikes...
                        </p>
                    ) : availableBikes.length === 0 ? (
                        <p className="empty-state">
                            No bikes are available.
                        </p>
                    ) : (
                        <div className="bike-list">
                            {availableBikes.map((bike) => (
                                <article
                                    className="bike-card"
                                    key={bike.bikeId}
                                >
                                    <div className="bike-card-header">
                                        <div>
                                            <h3>{bike.bikeId}</h3>

                                            <p className="bike-card-model">
                                                {bike.model}
                                            </p>
                                        </div>

                                        <span className="condition-badge">
                                            {bike.condition.replaceAll(
                                                "_",
                                                " "
                                            )}
                                        </span>
                                    </div>

                                    <dl className="bike-metrics">
                                        <div>
                                            <dt>Ride count</dt>
                                            <dd>{bike.rideCount}</dd>
                                        </div>

                                        <div>
                                            <dt>Mileage</dt>
                                            <dd>
                                                {bike.mileage.toLocaleString()}
                                            </dd>
                                        </div>
                                    </dl>
                                </article>
                            ))}
                        </div>
                    )}
                </article>
          

            <article className="panel user-report-panel">
                <div className="panel-header">
                    <div>
                        <h2>Report a maintenance issue</h2>

                        <p className="panel-description">
                            Select a bike and describe the
                            problem you observed.
                        </p>
                    </div>
                </div>

                <form
                    className="bike-form"
                    onSubmit={handleIssueSubmit}
                >
                    <label>
                        Bike
                        <select
                            name="bikeId"
                            value={issueForm.bikeId}
                            onChange={handleIssueChange}
                            required
                        >
                            <option value="">
                                Select a bike
                            </option>

                            {availableBikes.map((bike) => (
                                <option
                                    key={bike.bikeId}
                                    value={bike.bikeId}
                                >
                                    {bike.bikeId} —{" "}
                                    {bike.model}
                                </option>
                            ))}
                        </select>
                    </label>

                    <label>
                        Issue type
                        <select
                            name="sourceType"
                            value={issueForm.sourceType}
                            onChange={handleIssueChange}
                        >
                            <option value="USER_COMPLAINT">
                                User complaint
                            </option>

                            <option value="DETECTED_FAULT">
                                Detected fault
                            </option>
                        </select>
                    </label>

                    <label>
                        Description
                        <textarea
                            name="description"
                            value={issueForm.description}
                            onChange={handleIssueChange}
                            placeholder="Describe the problem"
                            required
                        />
                    </label>

                    <label>
                        Severity
                        <select
                            name="severity"
                            value={issueForm.severity}
                            onChange={handleIssueChange}
                        >
                            <option value="LOW">
                                Low
                            </option>

                            <option value="MEDIUM">
                                Medium
                            </option>

                            <option value="HIGH">
                                High
                            </option>

                            <option value="CRITICAL">
                                Critical
                            </option>
                        </select>
                    </label>

                    <button
                        className="primary-button"
                        type="submit"
                        disabled={submitting}
                    >
                        {submitting
                            ? "Submitting..."
                            : "Submit issue"}
                    </button>
                </form>
            </article>
            <article className="panel user-issues-panel">
                <div className="panel-header">
                    <div>
                        <h2>My reported issues</h2>

                        <p className="panel-description">
                            Review maintenance issues submitted
                            by your account.
                        </p>
                    </div>

                    <button
                        className="secondary-button"
                        type="button"
                        onClick={loadMyIssues}
                    >
                        Refresh
                    </button>
                </div>

                {issuesLoading ? (
                    <p className="empty-state">
                        Loading your maintenance issues...
                    </p>
                ) : myIssues.length === 0 ? (
                    <p className="empty-state">
                        You have not reported any issues yet.
                    </p>
                ) : (
                    <div className="maintenance-issue-list">
                        {myIssues.map((issue) => (
                            <article
                                className="maintenance-issue-card"
                                key={issue.maintenanceIssueId}
                            >
                                <div className="bike-card-header">
                                    <div>
                                        <h3>
                                            {issue.maintenanceIssueId}
                                        </h3>

                                        <p className="bike-card-model">
                                            Bike: {issue.bikeId}
                                        </p>
                                    </div>

                                    <span className="condition-badge">
                                        {issue.status.replaceAll(
                                            "_",
                                            " "
                                        )}
                                    </span>
                                </div>

                                <p>{issue.description}</p>

                                <div className="issue-details">
                                    <p>
                                        <strong>Type:</strong>{" "}
                                        {issue.sourceType.replaceAll(
                                            "_",
                                            " "
                                        )}
                                    </p>

                                    <p>
                                        <strong>Severity:</strong>{" "}
                                        {issue.severity}
                                    </p>
                                </div>
                            </article>
                        ))}
                    </div>
                )}
            </article>
			</section>
        </main>
    );
}

export default UserDashboard;