import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Meal } from '../../../../../models/meal.model';
import { PageChangeEvent, PaginationConfig } from '../../../../../models/pagination.model';

@Component({
  selector: 'app-meal-table-component',
  standalone: false,
  templateUrl: './meal-table-component.html',
  styleUrl: './meal-table-component.css',
})
export class MealTableComponent {
  @Input() meals: Meal[] = [];

  @Input() paginationConfig!: PaginationConfig;

  @Output() pageChange = new EventEmitter<PageChangeEvent>();
  @Output() action = new EventEmitter<{ type: string, data: Meal }>();

  onPageChange(event: PageChangeEvent) {
    this.pageChange.emit(event);
  }

  onAction(type: string, data: Meal) {
    this.action.emit({ type, data });
  }
}
