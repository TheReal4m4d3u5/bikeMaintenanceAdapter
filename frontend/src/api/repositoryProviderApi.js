import { graphqlRequest } from "./graphqlClient.js";

export async function getRepositoryProvider() {
  const query = `
    query GetRepositoryProvider {
      repositoryProvider
    }
  `;

  const data = await graphqlRequest(query);

  return data.repositoryProvider;
}
