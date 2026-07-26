import {
  Then,
  When,
} from "@cucumber/cucumber";

When(
  "I create a uniquely named bike",
  async function () {
    this.createdBikeModel =
      `GraphQL Smoke Bike ${Date.now()}`;

	  await this.page
	    .locator('[name="model"]')
	    .fill(this.createdBikeModel);

	  await this.page
	    .locator('[name="condition"]')
	    .selectOption("AVAILABLE");

	  await this.page
	    .locator('[name="rideCount"]')
	    .fill("1");

	  await this.page
	    .locator('[name="mileage"]')
	    .fill("1.5");

	  await this.page
	    .getByRole("button", {
	      name: /save bike/i,
	    })
	    .click();
  }
);

When(
  "I reload the application",
  async function () {
    await this.page.reload({
      waitUntil: "domcontentloaded",
    });

    const loginButton = this.page.getByRole(
      "button",
      {
        name: /log in|login|sign in/i,
      }
    );

    const loginIsVisible =
      await loginButton
        .isVisible()
        .catch(() => false);

    if (loginIsVisible) {
      await this.page
        .locator('[name="email"]')
        .fill("admin@example.com");

      await this.page
        .locator('[name="password"]')
        .fill("Admin123!");

      await loginButton.click();
    }

    await this.page
      .getByText(
        "Repository: Remote GraphQL",
        {
          exact: true,
        }
      )
      .waitFor({
        state: "visible",
        timeout: 10000,
      });
  }
);

Then(
  "the created bike should appear in the fleet",
  async function () {
    if (!this.createdBikeModel) {
      throw new Error(
        "The scenario did not create a bike model."
      );
    }

    await this.page
      .getByText(this.createdBikeModel, {
        exact: true,
      })
      .first()
      .waitFor({
        state: "visible",
        timeout: 10000,
      });
  }
);