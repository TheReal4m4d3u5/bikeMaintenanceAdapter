import {
    After,
    AfterAll,
    Before,
    BeforeAll,
    Status,
} from "@cucumber/cucumber";

import { chromium } from "playwright";

import { mkdir } from "node:fs/promises";

let browser;

BeforeAll(async function () {
    await mkdir(
        "e2e/reports/screenshots",
        { recursive: true },
    );

    browser = await chromium.launch({
        headless:
            process.env.BDD_HEADLESS !== "false",
    });
});

Before(async function () {
    this.context = await browser.newContext();

    // Prevent saved login tokens from affecting a test.
    await this.context.addInitScript(() => {
        window.localStorage.clear();
    });

    this.page = await this.context.newPage();
});

After(async function ({ pickle, result }) {
    if (
        result?.status === Status.FAILED &&
        this.page
    ) {
        const screenshotName = pickle.name
            .replace(/[^a-z0-9]+/gi, "-")
            .toLowerCase();

        await this.page.screenshot({
            path:
                `e2e/reports/screenshots/` +
                `${screenshotName}.png`,
            fullPage: true,
        });
    }

    await this.context?.close();
});

AfterAll(async function () {
    await browser?.close();
});