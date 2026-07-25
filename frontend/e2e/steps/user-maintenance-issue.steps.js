import assert from "node:assert/strict";

import {
    Then,
    When,
} from "@cucumber/cucumber";

Then(
    "I should see the user dashboard",
    async function () {
        await this.page
            .getByRole("heading", {
                name: "User Dashboard",
                exact: true,
            })
            .waitFor({
                state: "visible",
            });
    },
);

Then(
    "I should see at least one available bike",
    async function () {
        const availableBikeCards =
            this.page.locator(
                ".user-fleet-panel .bike-card"
            );

        await availableBikeCards
            .first()
            .waitFor({
                state: "visible",
            });

        const bikeCount =
            await availableBikeCards.count();

        assert.ok(
            bikeCount > 0,
            "Expected at least one bike available for use."
        );
    },
);

When(
    "I report a medium user complaint for the first available bike",
    async function () {
        this.reportedIssueDescription =
            `BDD brake concern ${Date.now()}`;

        const bikeSelect =
            this.page.locator(
                'select[name="bikeId"]'
            );

        const bikeOptions =
            bikeSelect.locator("option");

        await bikeOptions
            .nth(1)
            .waitFor({
                state: "attached",
            });

        const optionCount =
            await bikeOptions.count();

        assert.ok(
            optionCount > 1,
            "Expected the bike dropdown to contain an available bike."
        );

        await bikeSelect.selectOption({
            index: 1,
        });

        await this.page
            .locator(
                'select[name="sourceType"]'
            )
            .selectOption("USER_COMPLAINT");

        await this.page
            .getByLabel("Description")
            .fill(
                this.reportedIssueDescription
            );

        await this.page
            .locator(
                'select[name="severity"]'
            )
            .selectOption("MEDIUM");

        await this.page
            .getByRole("button", {
                name: "Submit issue",
                exact: true,
            })
            .click();
    },
);

Then(
    "the submitted maintenance issue should appear in my reported issues",
    async function () {
        const reportedIssuesPanel =
            this.page.locator(
                ".user-issues-panel"
            );

        await reportedIssuesPanel
            .getByText(
                this.reportedIssueDescription,
                {
                    exact: true,
                }
            )
            .waitFor({
                state: "visible",
            });

        await reportedIssuesPanel
            .getByText("OPEN", {
                exact: true,
            })
            .first()
            .waitFor({
                state: "visible",
            });
    },
);