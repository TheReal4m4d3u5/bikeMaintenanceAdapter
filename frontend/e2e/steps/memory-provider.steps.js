import assert from "node:assert/strict";

import { Then } from "@cucumber/cucumber";

Then(
    "the active repository provider should be {string}",
    async function (providerText) {
        await this.page
            .getByText(providerText, {
                exact: true,
            })
            .waitFor({
                state: "visible",
            });
    },
);

Then(
    "I should see at least one fleet bike",
    async function () {
        const bikeCards = this.page.locator(
            ".bike-card"
        );

        await bikeCards.first().waitFor({
            state: "visible",
        });

        const bikeCount = await bikeCards.count();

        assert.ok(
            bikeCount > 0,
            "Expected at least one seeded fleet bike."
        );
    },
);