import { inject, Injectable } from '@angular/core';
import { MealPage } from '../models/meal-page.model';
import { ApiService } from './api-service';
import { Meal } from '../models/meal.model';

@Injectable({
  providedIn: 'root',
})
export class MealService {
  private api=inject(ApiService);

  async getMeals(keyword: string, page: number, size: number): Promise<MealPage> {
    try {
      const response = await this.api.get<MealPage>(
        `/meals?keyword=${keyword}&page=${page}&size=${size}`
      );

      return (
        response.body ?? {
          meals: [],
          currentPage: 0,
          totalPage: 0,
          pageSize: size
        }
      );
    } catch (error) {
      console.error('Error fetching mealPage:', error);
      throw error;
    }
  }

  async create(meal: Meal): Promise<Meal> {
    try {
      const response = await this.api.post<Meal>('/meals', meal);
      return response.body ?? meal;
    } catch (error) {
      console.error('Error creating Meal:', error);
      throw error;
    }
  }
}
