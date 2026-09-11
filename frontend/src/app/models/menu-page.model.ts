import { Menu } from "./menu.model";
import { Page } from "./page.model";

export interface MenuPage extends Page {
    menus:Menu[];
}