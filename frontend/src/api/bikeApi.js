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
    const message = result.errors
      .map((error) => error.message)
      .join(", ");

    throw new Error(message);
  }

  return result.data;
}

export async function getBikes() {
  const query = `
    query GetBikes {
      bikes {
        bikeId
        model
        condition
        rideCount
        mileage
      }
    }
  `;

  const data = await executeGraphQl(query);

  return data.bikes;
}

export async function saveBike(input) {
  const mutation = `
    mutation SaveBike($input: BikeInput!) {
      saveBike(input: $input) {
        bikeId
        model
        condition
        rideCount
        mileage
      }
    }
  `;

  const data = await executeGraphQl(mutation, {
    input,
  });

  return data.saveBike;
}