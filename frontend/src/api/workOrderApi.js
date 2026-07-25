const GRAPHQL_URL = "/graphql";

async function executeGraphQl(query, variables = {}) {
  const response = await fetch(GRAPHQL_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      query,
      variables,
    }),
  });

  if (!response.ok) {
    throw new Error(
      `GraphQL request failed with status ${response.status}`
    );
  }

  const result = await response.json();

  if (result.errors?.length) {
    throw new Error(
      result.errors
        .map((error) => error.message)
        .join(", ")
    );
  }

  return result.data;
}

export async function getWorkOrders() {
  const query = `
    query GetWorkOrders {
      workOrders {
        workOrderId
        bikeId
        maintenanceIssueId
        description
        assignedTechnician
        status
      }
    }
  `;

  const data = await executeGraphQl(query);

  return data.workOrders;
}
export async function createWorkOrder(input) {
  const mutation = `
    mutation CreateWorkOrder($input: WorkOrderInput!) {
      createWorkOrder(input: $input) {
        workOrderId
        bikeId
        maintenanceIssueId
        description
        assignedTechnician
        status
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    input,
  });

  return data.createWorkOrder;
}

export async function startWork(workOrderId) {
  const mutation = `
    mutation StartWork($workOrderId: ID!) {
      startWork(workOrderId: $workOrderId) {
		workOrderId
		bikeId
		maintenanceIssueId
		description
		assignedTechnician
		status
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    workOrderId,
  });

  return data.startWork;
}

export async function closeWorkOrder(workOrderId) {
  const mutation = `
    mutation CloseWorkOrder($workOrderId: ID!) {
      closeWorkOrder(workOrderId: $workOrderId) {
		workOrderId
		bikeId
		maintenanceIssueId
		description
		assignedTechnician
		status
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    workOrderId,
  });

  return data.closeWorkOrder;
}