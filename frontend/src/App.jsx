import { useAuth } from "./auth/AuthContext.jsx";
import AccountBar from "./components/AccountBar.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";

import { useEffect, useState } from "react";

import { getBikes, saveBike } from "./api/bikeApi";


import {
  closeWorkOrder,
  createWorkOrder,
  getWorkOrders,
  startWork,
} from "./api/workOrderApi";


import {
  createMaintenanceIssue,
  getMaintenanceIssues,
  resolveMaintenanceIssue,
} from "./api/maintenanceIssueApi";

import "./App.css";




const emptyBike = {
  bikeId: "",
  model: "",
  condition: "AVAILABLE",
  rideCount: 0,
  mileage: 0,
};


const emptyWorkOrder = {
  workOrderId: "",
  bikeId: "",
  maintenanceIssueId: "",
  description: "",
  assignedTechnician: "",
};


const emptyMaintenanceIssue = {
  maintenanceIssueId: "",
  bikeId: "",
  sourceType: "DETECTED_FAULT",
  description: "",
  severity: "MEDIUM",
};




function MaintenanceDashboard() {
  const [bikes, setBikes] = useState([]);
  const [form, setForm] = useState(emptyBike);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const [workOrders, setWorkOrders] = useState([]);
  const [workOrderForm, setWorkOrderForm] =
    useState(emptyWorkOrder);
  const [workOrdersLoading, setWorkOrdersLoading] =
    useState(true);

	const [maintenanceIssues, setMaintenanceIssues] =
	  useState([]);

	const [maintenanceIssueForm, setMaintenanceIssueForm] =
	  useState(emptyMaintenanceIssue);

	const [
	  maintenanceIssuesLoading,
	  setMaintenanceIssuesLoading,
	] = useState(true);	
	
	
	
	
	useEffect(() => {
	  loadBikes();
	  loadMaintenanceIssues();
	  loadWorkOrders();
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
  
  async function loadMaintenanceIssues() {
    try {
      setMaintenanceIssuesLoading(true);
      setError("");

      const storedIssues =
        await getMaintenanceIssues();

      setMaintenanceIssues(storedIssues);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setMaintenanceIssuesLoading(false);
    }
  }
  
  function handleMaintenanceIssueChange(event) {
    const { name, value } = event.target;

    setMaintenanceIssueForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));
  }

  async function handleMaintenanceIssueSubmit(event) {
    event.preventDefault();

    try {
      setError("");

      const savedIssue = await createMaintenanceIssue({
        maintenanceIssueId:
          maintenanceIssueForm.maintenanceIssueId.trim(),
        bikeId: maintenanceIssueForm.bikeId.trim(),
        sourceType: maintenanceIssueForm.sourceType,
        description:
          maintenanceIssueForm.description.trim(),
        severity: maintenanceIssueForm.severity,
      });

      setMaintenanceIssues((currentIssues) => [
        ...currentIssues.filter(
          (issue) =>
            issue.maintenanceIssueId !==
            savedIssue.maintenanceIssueId
        ),
        savedIssue,
      ]);

      setMaintenanceIssueForm(emptyMaintenanceIssue);
    } catch (requestError) {
      setError(requestError.message);
    }
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

  async function handleResolveMaintenanceIssue(
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
  
  
  
  async function loadWorkOrders() {
    try {
      setWorkOrdersLoading(true);
      setError("");

      const storedWorkOrders = await getWorkOrders();
      setWorkOrders(storedWorkOrders);
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setWorkOrdersLoading(false);
    }
  }
 
  function handleWorkOrderChange(event) {
    const { name, value } = event.target;

    setWorkOrderForm((currentForm) => ({
      ...currentForm,
      [name]: value,

      ...(name === "bikeId"
        ? { maintenanceIssueId: "" }
        : {}),
    }));
  }

  async function handleWorkOrderSubmit(event) {
    event.preventDefault();

    try {
      setError("");

	  const savedWorkOrder = await createWorkOrder({
	    workOrderId: workOrderForm.workOrderId.trim(),
	    bikeId: workOrderForm.bikeId.trim(),
	    maintenanceIssueId:
	      workOrderForm.maintenanceIssueId.trim(),
	    description: workOrderForm.description.trim(),
	    assignedTechnician:
	      workOrderForm.assignedTechnician.trim() || null,
	  });

      setWorkOrders((currentWorkOrders) => [
        ...currentWorkOrders.filter(
          (workOrder) =>
            workOrder.workOrderId !==
            savedWorkOrder.workOrderId
        ),
        savedWorkOrder,
      ]);
	  
	  await loadMaintenanceIssues();

      setWorkOrderForm(emptyWorkOrder);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  function replaceWorkOrder(updatedWorkOrder) {
    setWorkOrders((currentWorkOrders) =>
      currentWorkOrders.map((workOrder) =>
        workOrder.workOrderId === updatedWorkOrder.workOrderId
          ? updatedWorkOrder
          : workOrder
      )
    );
  }

  async function handleStartWork(workOrderId) {
    try {
      setError("");

      const updatedWorkOrder = await startWork(workOrderId);

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

  function handleChange(event) {
    const { name, value } = event.target;

    setForm((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    try {
      setError("");

      const savedBike = await saveBike({
        bikeId: form.bikeId.trim(),
        model: form.model.trim(),
        condition: form.condition,
        rideCount: Number(form.rideCount),
        mileage: Number(form.mileage),
      });

      setBikes((currentBikes) => {
        const remainingBikes = currentBikes.filter(
          (bike) => bike.bikeId !== savedBike.bikeId
        );

        return [...remainingBikes, savedBike];
      });

      setForm(emptyBike);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  return (
    <main className="app">
      <AccountBar />

      <header className="page-header">
        <div className="page-header-content">
          <div>
            <p className="eyebrow">
              Multi-Database Adapter Showcase
            </p>

            <h1>Bike Maintenance Adapter</h1>

            <p className="subtitle">
              React and GraphQL using a swappable repository
              implementation.
            </p>
          </div>

          <div className="repository-badge">
            Repository: In Memory
          </div>
        </div>
      </header>

      {error && (
        <div className="error-message" role="alert">
          {error}
        </div>
      )}

      <section className="dashboard">
        <article className="panel">
          <div className="panel-header">
            <div>
              <h2>Add or update a bike</h2>

              <p className="panel-description">
                Enter the operational information for a fleet bike.
              </p>
            </div>
          </div>

          <form className="bike-form" onSubmit={handleSubmit}>
            <label>
              Bike ID
              <input
                name="bikeId"
                value={form.bikeId}
                onChange={handleChange}
                placeholder="BIKE-1001"
                required
              />
            </label>

            <label>
              Model
              <input
                name="model"
                value={form.model}
                onChange={handleChange}
                placeholder="Metro Commuter"
                required
              />
            </label>

            <label>
              Condition
              <select
                name="condition"
                value={form.condition}
                onChange={handleChange}
              >
                <option value="AVAILABLE">
                  Available
                </option>

                <option value="DUE_FOR_SCHEDULED_MAINTENANCE">
                  Due for scheduled maintenance
                </option>

                <option value="OUT_OF_SERVICE">
                  Out of service
                </option>

                <option value="UNDER_REPAIR">
                  Under repair
                </option>

                <option value="RETIRED">
                  Retired
                </option>
              </select>
            </label>

            <div className="form-row">
              <label>
                Ride count
                <input
                  type="number"
                  name="rideCount"
                  min="0"
                  value={form.rideCount}
                  onChange={handleChange}
                  required
                />
              </label>

              <label>
                Mileage
                <input
                  type="number"
                  name="mileage"
                  min="0"
                  step="0.1"
                  value={form.mileage}
                  onChange={handleChange}
                  required
                />
              </label>
            </div>

            <button
              className="primary-button"
              type="submit"
            >
              Save bike
            </button>
          </form>
        </article>

        <article className="panel">
          <div className="panel-header">
            <div>
              <h2>Fleet bikes</h2>

              <p className="panel-description">
                Bikes currently stored by the selected repository.
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
          ) : bikes.length === 0 ? (
            <p className="empty-state">
              No bikes are stored yet.
            </p>
          ) : (
            <div className="bike-list">
              {bikes.map((bike) => (
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
                      {bike.condition.replaceAll("_", " ")}
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
      </section>
	  
	  
	  
	  
	  <section className="maintenance-issue-section">
	    <article className="panel">
	      <div className="panel-header">
	        <div>
	          <h2>Create maintenance issue</h2>

	          <p className="panel-description">
	            Record a scheduled need, detected fault, or user complaint.
	          </p>
	        </div>
	      </div>

	      <form
	        className="bike-form"
	        onSubmit={handleMaintenanceIssueSubmit}
	      >
	        <label>
	          Maintenance issue ID
	          <input
	            name="maintenanceIssueId"
	            value={maintenanceIssueForm.maintenanceIssueId}
	            onChange={handleMaintenanceIssueChange}
	            placeholder="ISSUE-1001"
	            required
	          />
	        </label>

	        <label>
	          Bike
	          <select
	            name="bikeId"
	            value={maintenanceIssueForm.bikeId}
	            onChange={handleMaintenanceIssueChange}
	            required
	          >
	            <option value="">Select a bike</option>

	            {bikes.map((bike) => (
	              <option
	                key={bike.bikeId}
	                value={bike.bikeId}
	              >
	                {bike.bikeId} — {bike.model}
	              </option>
	            ))}
	          </select>
	        </label>

	        <label>
	          Source type
	          <select
	            name="sourceType"
	            value={maintenanceIssueForm.sourceType}
	            onChange={handleMaintenanceIssueChange}
	          >
	            <option value="DETECTED_FAULT">
	              Detected fault
	            </option>

	            <option value="USER_COMPLAINT">
	              User complaint
	            </option>

	            <option value="SCHEDULED_MAINTENANCE">
	              Scheduled maintenance
	            </option>
	          </select>
	        </label>

	        <label>
	          Description
	          <textarea
	            name="description"
	            value={maintenanceIssueForm.description}
	            onChange={handleMaintenanceIssueChange}
	            placeholder="Describe the maintenance problem"
	            required
	          />
	        </label>

	        <label>
	          Severity
	          <select
	            name="severity"
	            value={maintenanceIssueForm.severity}
	            onChange={handleMaintenanceIssueChange}
	          >
	            <option value="LOW">Low</option>
	            <option value="MEDIUM">Medium</option>
	            <option value="HIGH">High</option>
	            <option value="CRITICAL">Critical</option>
	          </select>
	        </label>

	        <button
	          className="primary-button"
	          type="submit"
	        >
	          Create maintenance issue
	        </button>
	      </form>
	    </article>

	    <article className="panel">
	      <div className="panel-header">
	        <div>
	          <h2>Maintenance issues</h2>

	          <p className="panel-description">
	            Review reported maintenance needs and their current status.
	          </p>
	        </div>

	        <button
	          className="secondary-button"
	          type="button"
	          onClick={loadMaintenanceIssues}
	        >
	          Refresh
	        </button>
	      </div>

	      {maintenanceIssuesLoading ? (
	        <p className="empty-state">
	          Loading maintenance issues...
	        </p>
	      ) : maintenanceIssues.length === 0 ? (
	        <p className="empty-state">
	          No maintenance issues are stored yet.
	        </p>
	      ) : (
	        <div className="maintenance-issue-list">
	          {maintenanceIssues.map((issue) => (
	            <article
	              className="maintenance-issue-card"
	              key={issue.maintenanceIssueId}
	            >
	              <div className="bike-card-header">
	                <div>
	                  <h3>{issue.maintenanceIssueId}</h3>

	                  <p className="bike-card-model">
	                    Bike: {issue.bikeId}
	                  </p>
	                </div>

	                <span className="condition-badge">
	                  {issue.status.replaceAll("_", " ")}
	                </span>
	              </div>

	              <p>{issue.description}</p>

	              <div className="issue-details">
	                <p>
	                  <strong>Source:</strong>{" "}
	                  {issue.sourceType.replaceAll("_", " ")}
	                </p>

	                <p>
	                  <strong>Severity:</strong>{" "}
	                  {issue.severity}
	                </p>
	              </div>

	              {issue.status !== "RESOLVED" && (
	                <button
	                  className="secondary-button"
	                  type="button"
	                  onClick={() =>
	                    handleResolveMaintenanceIssue(
	                      issue.maintenanceIssueId
	                    )
	                  }
	                >
	                  Resolve issue
	                </button>
	              )}
	            </article>
	          ))}
	        </div>
	      )}
	    </article>
	  </section>
	  
	  
	  
	  <section className="work-order-section">
	    <article className="panel">
	      <div className="panel-header">
	        <div>
	          <h2>Create work order</h2>

	          <p className="panel-description">
	            Create a maintenance work order for an existing bike.
	          </p>
	        </div>
	      </div>

	      <form
	        className="bike-form"
	        onSubmit={handleWorkOrderSubmit}
	      >
	        <label>
	          Work order ID
	          <input
	            name="workOrderId"
	            value={workOrderForm.workOrderId}
	            onChange={handleWorkOrderChange}
	            placeholder="WO-1001"
	            required
	          />
	        </label>

	        <label>
	          Bike
	          <select
	            name="bikeId"
	            value={workOrderForm.bikeId}
	            onChange={handleWorkOrderChange}
	            required
	          >
	            <option value="">Select a bike</option>

	            {bikes.map((bike) => (
	              <option
	                key={bike.bikeId}
	                value={bike.bikeId}
	              >
	                {bike.bikeId} — {bike.model}
	              </option>
	            ))}
	          </select>
	        </label>

			
			<label>
			  Maintenance issue
			  <select
			    name="maintenanceIssueId"
			    value={workOrderForm.maintenanceIssueId}
			    onChange={handleWorkOrderChange}
			    disabled={!workOrderForm.bikeId}
			    required
			  >
			    <option value="">
			      {workOrderForm.bikeId
			        ? "Select an open maintenance issue"
			        : "Select a bike first"}
			    </option>

			    {maintenanceIssues
			      .filter(
			        (issue) =>
			          issue.status === "OPEN" &&
			          issue.bikeId === workOrderForm.bikeId
			      )
			      .map((issue) => (
			        <option
			          key={issue.maintenanceIssueId}
			          value={issue.maintenanceIssueId}
			        >
			          {issue.maintenanceIssueId} — {issue.description}
			        </option>
			      ))}
			  </select>
			</label>
			
			
	        <label>
	          Description
	          <textarea
	            name="description"
	            value={workOrderForm.description}
	            onChange={handleWorkOrderChange}
	            placeholder="Inspect rear brake assembly"
	            required
	          />
	        </label>

	        <label>
	          Assigned technician
	          <input
	            name="assignedTechnician"
	            value={workOrderForm.assignedTechnician}
	            onChange={handleWorkOrderChange}
	            placeholder="Jordan Lee"
	          />
	        </label>

	        <button className="primary-button" type="submit">
	          Create work order
	        </button>
	      </form>
	    </article>

	    <article className="panel">
	      <div className="panel-header">
	        <div>
	          <h2>Maintenance work orders</h2>

	          <p className="panel-description">
	            Track assigned, active, and completed maintenance.
	          </p>
	        </div>

	        <button
	          className="secondary-button"
	          type="button"
	          onClick={loadWorkOrders}
	        >
	          Refresh
	        </button>
	      </div>

	      {workOrdersLoading ? (
	        <p className="empty-state">
	          Loading work orders...
	        </p>
	      ) : workOrders.length === 0 ? (
	        <p className="empty-state">
	          No work orders are stored yet.
	        </p>
	      ) : (
	        <div className="work-order-list">
	          {workOrders.map((workOrder) => (
	            <article
	              className="work-order-card"
	              key={workOrder.workOrderId}
	            >
	              <div className="bike-card-header">
	                <div>
	                  <h3>{workOrder.workOrderId}</h3>
	                  <p className="bike-card-model">
	                    Bike: {workOrder.bikeId}
	                  </p>
	                </div>

	                <span className="condition-badge">
	                  {workOrder.status.replaceAll("_", " ")}
	                </span>
	              </div>

	              <p>{workOrder.description}</p>

	              <p>
	                <strong>Technician:</strong>{" "}
	                {workOrder.assignedTechnician ||
	                  "Not assigned"}
	              </p>

	              <div className="work-order-actions">
	                {workOrder.status !== "IN_PROGRESS" &&
	                  workOrder.status !== "CLOSED" && (
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

	                {workOrder.status !== "CLOSED" && (
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
	          ))}
	        </div>
	      )}
	    </article>
	  </section>
    </main>
  );
}

function App() {
  const { user, authLoading } = useAuth();

  const [authView, setAuthView] = useState("login");

  if (authLoading) {
    return (
      <main className="login-page">
        <section className="login-card">
          <h1>Loading account...</h1>
        </section>
      </main>
    );
  }

  if (!user) {
    if (authView === "register") {
      return (
        <RegisterPage
          onShowLogin={() => setAuthView("login")}
        />
      );
    }

    return (
      <LoginPage
        onShowRegister={() => setAuthView("register")}
      />
    );
  }

  return <MaintenanceDashboard />;
}

export default App;