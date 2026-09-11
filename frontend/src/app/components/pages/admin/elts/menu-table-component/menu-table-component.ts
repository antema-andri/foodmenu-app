import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PageChangeEvent, PaginationConfig } from '../../../../../models/pagination.model';
import { Menu } from '../../../../../models/menu.model';

@Component({
  selector: 'app-menu-table-component',
  standalone: false,
  templateUrl: './menu-table-component.html',
  styleUrl: './menu-table-component.css',
})
export class MenuTableComponent {
  @Input() menus: Menu[] = [];
  @Input() paginationConfig: PaginationConfig = {
    page: 1,
    pageSize: 10,
    total: 0
  };
  @Input() loading: boolean = false;
  @Input() loadingPdfMenuId: string | null = null;
  @Input() loadingModalMenuId: string | null = null;

  @Output() pageChange = new EventEmitter<PageChangeEvent>();
  @Output() action = new EventEmitter<{type: string, data: Menu}>();
  @Output() openPdfAction = new EventEmitter<{type: string, data: Menu}>();

  onPageChange(event: PageChangeEvent): void {
    this.pageChange.emit(event);
  }

  onAction(event: {type: string, data: Menu}): void {
    this.action.emit({ type:event.type, data:event.data });
  }

  getStatusClass(menu:Menu): string {
    return menu.active 
      ? 'badge bg-warning text-white' 
      : 'badge bg-success text-white';
  }

  openPdfEmitter(event:{type:string, data:Menu}) {
    this.openPdfAction.emit({ type:event.type, data:event.data })
  }

}
