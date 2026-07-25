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

export async function getMaintenanceIssues() {
  const query = `
    query GetMaintenanceIssues {
      maintenanceIssues {
        maintenanceIssueId
        bikeId
        sourceType
        description
        severity
        status
      }
    }
  `;

  const data = await executeGraphQl(query);

  return data.maintenanceIssues;
}

export async function createMaintenanceIssue(input) {
  const mutation = `
    mutation CreateMaintenanceIssue(
      $input: MaintenanceIssueInput!
    ) {
      createMaintenanceIssue(input: $input) {
        maintenanceIssueId
        bikeId
        sourceType
        description
        severity
        status
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    input,
  });

  return data.createMaintenanceIssue;
}

export async function resolveMaintenanceIssue(
  maintenanceIssueId
) {
  const mutation = `
    mutation ResolveMaintenanceIssue(
      $maintenanceIssueId: ID!
    ) {
      resolveMaintenanceIssue(
        maintenanceIssueId: $maintenanceIssueId
      ) {
        maintenanceIssueId
        bikeId
        sourceType
        description
        severity
        status
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    maintenanceIssueId,
  });

  return data.resolveMaintenanceIssue;
}