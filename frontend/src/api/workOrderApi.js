import { graphqlRequest } from "./graphqlClient";

const workOrderFields = `
  workOrderId
  bikeId
  maintenanceIssueId
  description
  assignedTechnicianId
  status
`;

export async function getWorkOrders() {
  const query = `
    query GetWorkOrders {
      workOrders {
        ${workOrderFields}
      }
    }
  `;

  const data = await graphqlRequest(query);

  return data.workOrders;
}

export async function getMyWorkOrders() {
    const query = `
        query GetMyWorkOrders {
            myWorkOrders {
                ${workOrderFields}
            }
        }
    `;

    const data = await graphqlRequest(query);

    return data.myWorkOrders;
}

export async function createWorkOrder(input) {
  const mutation = `
    mutation CreateWorkOrder($input: WorkOrderInput!) {
      createWorkOrder(input: $input) {
        ${workOrderFields}
      }
    }
  `;

  const data = await graphqlRequest(mutation, {
    input,
  });

  return data.createWorkOrder;
}

export async function startWork(workOrderId) {
  const mutation = `
    mutation StartWork($workOrderId: ID!) {
      startWork(workOrderId: $workOrderId) {
        ${workOrderFields}
      }
    }
  `;

  const data = await graphqlRequest(mutation, {
    workOrderId,
  });

  return data.startWork;
}

export async function closeWorkOrder(
  workOrderId,
  resultingBikeCondition
) {
  const mutation = `
    mutation CloseWorkOrder(
      $input: CloseWorkOrderInput!
    ) {
      closeWorkOrder(input: $input) {
        ${workOrderFields}
      }
    }
  `;

  const data = await graphqlRequest(mutation, {
    input: {
      workOrderId,
      resultingBikeCondition,
    },
  });

  return data.closeWorkOrder;
}