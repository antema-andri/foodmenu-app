import { BaseModel } from "./base-model.models.";
import { Customer } from "./customer.model";
import { Meal } from "./meal.model";
import { Menu } from "./menu.model";

export interface Order extends BaseModel {
  id:string;
  customer:Customer;
  meal:Meal;
  menu:Menu;
}