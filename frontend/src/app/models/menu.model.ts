import { Meal } from "./meal.model";

export interface Menu {
  id:string;
  date?:string;
  title:string;
  active?:boolean;
  createdAt?:string;

  meals:Meal[];
}