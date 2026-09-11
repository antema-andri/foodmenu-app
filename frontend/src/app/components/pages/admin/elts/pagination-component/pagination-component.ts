import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PageChangeEvent, PaginationConfig } from '../../../../../models/pagination.model';

@Component({
  selector: 'app-pagination',
  standalone: false,
  templateUrl: './pagination-component.html',
  styleUrl: './pagination-component.css',
})
export class PaginationComponent {
  @Input() config: PaginationConfig = {
    page: 1,
    pageSize: 10,
    total: 0,
    pageSizeOptions: [5, 10, 25, 50]
  };

  @Input() showPageSizeOptions: boolean = true;
  @Input() showTotal: boolean = true;
  @Input() alignment: 'start' | 'center' | 'between' | 'end' = 'end';

  @Output() pageChange = new EventEmitter<PageChangeEvent>();

  get totalPages(): number {
    return Math.ceil(this.config.total / this.config.pageSize);
  }

  get startIndex(): number {
    return (this.config.page - 1) * this.config.pageSize + 1;
  }

  get endIndex(): number {
    const end = this.config.page * this.config.pageSize;
    return end > this.config.total ? this.config.total : end;
  }

  get pages(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;
    
    let startPage = Math.max(1, this.config.page - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);
    
    // Ajuster si on est près de la fin
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  isFirstPage(): boolean {
    return this.config.page === 1;
  }

  isLastPage(): boolean {
    return this.config.page === this.totalPages;
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages && page !== this.config.page) {
      this.config.page = page;
      this.emitPageChange();
    }
  }

  previousPage(): void {
    if (!this.isFirstPage()) {
      this.config.page--;
      this.emitPageChange();
    }
  }

  nextPage(): void {
    if (!this.isLastPage()) {
      this.config.page++;
      this.emitPageChange();
    }
  }

  onPageSizeChange(pageSize: number): void {
    this.config.pageSize = pageSize;
    this.config.page = 1; // Retour à la première page quand on change la taille
    this.emitPageChange();
  }

  private emitPageChange(): void {
    this.pageChange.emit({
      page: this.config.page,
      pageSize: this.config.pageSize
    });
  }
}
