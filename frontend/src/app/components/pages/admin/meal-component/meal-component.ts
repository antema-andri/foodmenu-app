import { Component, inject, OnInit } from '@angular/core';
import { MealService } from '../../../../services/meal-service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Meal } from '../../../../models/meal.model';
import { MealPage } from '../../../../models/meal-page.model';
import { PageChangeEvent, PaginationConfig } from '../../../../models/pagination.model';
import { PaginationUtils } from '../../../../utils/PaginationUtils';

@Component({
  selector: 'app-meal-component',
  standalone: false,
  templateUrl: './meal-component.html',
  styleUrl: './meal-component.css',
})
export class MealComponent implements OnInit {
  private mealService = inject(MealService);
  private fb = inject(FormBuilder);

  mealForm!: FormGroup;

  meals: Meal[] = [];
  activeMealPage!: MealPage;

  paginationConfig: PaginationConfig = {
    page: 1,
    pageSize: 6,
    total: 0,
    pageSizeOptions: [5, 10, 25, 50]
  };

  searchTerm: string = '';

  isCreatingMeal:boolean = false;

  ngOnInit() {
    this.mealForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(80)]]
    });

    this.loadMeals();
  }

  async submitMeal() {
    if (this.mealForm.invalid) return;
    this.isCreatingMeal = true;

    const meal = this.mealForm.value as Meal;
    await this.mealService.create(meal);
    
    this.isCreatingMeal = false;
    this.mealForm.reset();
    this.loadMeals();
  }

  async loadMeals() {
    const apiPage = PaginationUtils.uiToApi(this.paginationConfig.page);

    this.activeMealPage = await this.mealService.getMeals(
      this.searchTerm,
      apiPage,
      this.paginationConfig.pageSize
    );

    this.meals = this.activeMealPage.meals;

    this.paginationConfig.total =
      PaginationUtils.totalElements(
        this.activeMealPage.totalPage,
        this.paginationConfig.pageSize
      );

    this.paginationConfig.page =
      PaginationUtils.apiToUi(this.activeMealPage.currentPage);
  }

  onPageChange(event: PageChangeEvent) {
    this.paginationConfig.page = event.page;
    this.paginationConfig.pageSize = event.pageSize;
    this.loadMeals();
  }

  onSearch(term: string) {
    this.searchTerm = term;
    this.paginationConfig.page = 1;
    this.loadMeals();
  }
}
