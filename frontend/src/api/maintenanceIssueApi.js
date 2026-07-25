import { graphqlRequest } from "./graphqlClient";

const maintenanceIssueFields = `
	maintenanceIssueId
	bikeId
	reportedByUserId
	sourceType
	description
	severity
	status
`;

export async function getMaintenanceIssues() {
    const query = `
    query GetMaintenanceIssues {
      maintenanceIssues {
        ${maintenanceIssueFields}
      }
    }
  `;

    const data = await graphqlRequest(query);

    return data.maintenanceIssues;
}

export async function getMyMaintenanceIssues() {
    const query = `
        query GetMyMaintenanceIssues {
            myMaintenanceIssues {
                ${maintenanceIssueFields}
            }
        }
    `;

    const data = await graphqlRequest(query);

    return data.myMaintenanceIssues;
}



export async function createMaintenanceIssue(input) {
    const mutation = `
    mutation CreateMaintenanceIssue(
      $input: MaintenanceIssueInput!
    ) {
      createMaintenanceIssue(input: $input) {
        ${maintenanceIssueFields}
      }
    }
  `;

    const data = await graphqlRequest(mutation, {
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
        ${maintenanceIssueFields}
      }
    }
  `;

    const data = await graphqlRequest(mutation, {
        maintenanceIssueId,
    });

    return data.resolveMaintenanceIssue;
}