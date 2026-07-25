import { graphqlRequest } from "./graphqlClient.js";

export async function getTechnicians() {
    const query = `
        query GetTechnicians {
            technicians {
                userId
                displayName
                email
            }
        }
    `;

    const data = await graphqlRequest(query);

    return data.technicians;
}