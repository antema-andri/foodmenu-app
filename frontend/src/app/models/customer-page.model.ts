import { Customer } from "./customer.model";
import { Page } from "./page.model";

export interface CustomerPage extends Page {
    customers:Customer[];
}