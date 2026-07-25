import {
    setDefaultTimeout,
    setWorldConstructor,
} from "@cucumber/cucumber";

setDefaultTimeout(30_000);

class BikeMaintenanceWorld {
    constructor() {
        this.context = null;
        this.page = null;
    }
}

setWorldConstructor(BikeMaintenanceWorld);