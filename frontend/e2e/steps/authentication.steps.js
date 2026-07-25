import assert from "node:assert/strict";

import {
    Given,
    Then,
    When,
} from "@cucumber/cucumber";

const baseUrl =
    process.env.E2E_BASE_URL ??
    "http://localhost:5173";

Given(
    "I am on the login page",
    async function () {
        await this.page.goto(baseUrl, {
            waitUntil: "domcontentloaded",
        });

        const heading = this.page.getByRole(
            "heading",
            {
                name: "Welcome back",
                exact: true,
            },
        );

        await heading.waitFor({
            state: "visible",
        });

        assert.equal(
            await heading.isVisible(),
            true,
        );
    },
);

When(
    "I log in with email {string} and password {string}",
    async function (email, password) {
        await this.page
            .getByLabel("Email")
            .fill(email);

        await this.page
            .getByLabel("Password")
            .fill(password);

        await this.page
            .getByRole("button", {
                name: "Log in",
                exact: true,
            })
            .click();
    },
);

Then(
    "I should see the administrator dashboard",
    async function () {
        await this.page
            .getByRole("heading", {
                name: "Bike Maintenance Adapter",
                exact: true,
            })
            .waitFor({
                state: "visible",
            });
    },
);

Then(
    "I should be signed in as {string}",
    async function (displayName) {
        const accountName =
            this.page.getByText(
                displayName,
                { exact: true },
            );

        await accountName.waitFor({
            state: "visible",
        });

        assert.equal(
            await accountName.isVisible(),
            true,
        );
    },
);