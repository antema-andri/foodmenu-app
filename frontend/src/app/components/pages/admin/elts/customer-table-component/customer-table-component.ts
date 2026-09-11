import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PageChangeEvent, PaginationConfig } from '../../../../../models/pagination.model';
import { Customer } from '../../../../../models/customer.model';

@Component({
  selector: 'app-customer-table-component',
  standalone: false,
  templateUrl: './customer-table-component.html',
  styleUrl: './customer-table-component.css',
})
export class CustomerTableComponent {
  @Input() customers: Customer[] = [];
  @Input() paginationConfig: PaginationConfig = {
    page: 1,
    pageSize: 10,
    total: 0
  };
  @Input() loading = false;

  @Output() pageChange = new EventEmitter<PageChangeEvent>();
  @Output() action = new EventEmitter<{ type: string, data: Customer }>();
  @Output() search = new EventEmitter<string>();

  searchTerm = "";

  onAction(type: string, data: Customer) {
    this.action.emit({ type, data });
  }

  onSearchChange() {
    this.search.emit(this.searchTerm);
  }

  onPageChange(event: PageChangeEvent) {
    this.pageChange.emit(event);
  }
}
