import { Meal } from "./meal.model";
import { Page } from "./page.model";

export interface MealPage extends Page {
    meals:Meal[];
}