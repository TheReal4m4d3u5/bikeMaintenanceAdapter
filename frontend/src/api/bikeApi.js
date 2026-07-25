import { graphqlRequest } from "./graphqlClient";

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

  const data = await graphqlRequest(query);

  return data.bikes;
}

export async function createBike(input) {
  const mutation = `
    mutation CreateBike($input: CreateBikeInput!) {
      createBike(input: $input) {
        bikeId
        model
        condition
        rideCount
        mileage
      }
    }
  `;

  const data = await graphqlRequest(mutation, {
    input,
  });

  return data.createBike;
}

export async function updateBike(input) {
  const mutation = `
    mutation UpdateBike($input: BikeInput!) {
      updateBike(input: $input) {
        bikeId
        model
        condition
        rideCount
        mileage
      }
    }
  `;

  const data = await graphqlRequest(mutation, {
    input,
  });

  return data.updateBike;
}