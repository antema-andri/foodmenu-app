import { Component, EventEmitter, Input, Output, TemplateRef } from '@angular/core';
import { ColumnConfig, ListItem, ModalListConfig } from '../../../../../models/modal-list.model';

@Component({
  selector: 'app-modal-list',
  standalone: false,
  templateUrl: './modal-list-component.html',
  styleUrl: './modal-list-component.css',
})
export class ModalListComponent<T extends ListItem> {
  @Input() data: T[] = [];
  @Input() config!: ModalListConfig;
  @Input() isOpen: boolean = false;
  @Input() customTemplates: { [key: string]: TemplateRef<any> } = {};
  
  @Output() closed = new EventEmitter<void>();
  @Output() itemClicked = new EventEmitter<T>();
  @Output() actionClicked = new EventEmitter<{ action: string, item: T }>();
  @Output() searchChanged = new EventEmitter<string>();

  searchTerm: string = '';
  currentPage: number = 1;
  filteredData: T[] = [];

  ngOnInit(): void {
    this.filteredData = [...this.data];
  }

  ngOnChanges(): void {
    this.filteredData = this.filterData(this.data, this.searchTerm);
  }

  closeModal(): void {
    this.isOpen = false;
    this.closed.emit();
  }

  onItemClick(item: T): void {
    if (this.config.itemClickable !== false) {
      this.itemClicked.emit(item);
    }
  }

  onActionClick(action: string, item: T, event: Event): void {
    event.stopPropagation();
    this.actionClicked.emit({ action, item });
  }

  onSearchChange(term: string): void {
    this.searchTerm = term;
    this.filteredData = this.filterData(this.data, term);
    this.searchChanged.emit(term);
    this.currentPage = 1;
  }

  private filterData(data: T[], searchTerm: string): T[] {
    if (!searchTerm) return data;

    return data.filter(item => 
      this.config.colonnes.some(column => {
        const value = item[column.key];
        return value?.toString().toLowerCase().includes(searchTerm.toLowerCase());
      })
    );
  }

  get paginatedData(): T[] {
    if (!this.config.showPagination) {
      return this.filteredData;
    }

    const pageSize = this.config.pageSize || 10;
    const startIndex = (this.currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;

    return this.filteredData.slice(startIndex, endIndex);
  }

  get totalPages(): number {
    const pageSize = this.config.pageSize || 10;
    return Math.ceil(this.filteredData.length / pageSize);
  }

  get displayedCount(): string {
    const start = (this.currentPage - 1) * (this.config.pageSize || 10) + 1;
    const end = Math.min(this.currentPage * (this.config.pageSize || 10), this.filteredData.length);
    return `Affichage de ${start} à ${end} sur ${this.filteredData.length} éléments`;
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  getCellValue(item: T, column: ColumnConfig): any {
    return item[column.key];
  }

  formatValue(value: any, column: ColumnConfig): string {
    if (value == null) return '-';

    switch (column.type) {
      case 'currency':
        return `${value}€`;
      case 'date':
        return new Date(value).toLocaleDateString();
      case 'number':
        return value.toString();
      default:
        return value.toString();
    }
  }

  getBadgeClass(item: T, column: ColumnConfig): string {
    const value = this.getCellValue(item, column);
    const baseClass = 'badge';
    
    // Logique pour déterminer la classe du badge selon la valeur
    if (column.key === 'statut') {
      switch (value) {
        case 'Livrée': return `${baseClass} bg-success`;
        case 'En cours': return `${baseClass} bg-warning text-dark`;
        case 'Annulée': return `${baseClass} bg-danger`;
        case 'Confirmée': return `${baseClass} bg-info`;
        case 'En attente': return `${baseClass} bg-secondary`;
        default: return `${baseClass} bg-light text-dark`;
      }
    }
    
    if (column.key === 'role') {
      switch (value) {
        case 'Administrateur': return `${baseClass} bg-primary`;
        case 'Utilisateur': return `${baseClass} bg-secondary`;
        case 'Modérateur': return `${baseClass} bg-info`;
        default: return `${baseClass} bg-light text-dark`;
      }
    }
    
    if (column.key === 'stock' || column.key === 'disponible') {
      if (value > 0 || value === true) return `${baseClass} bg-success`;
      return `${baseClass} bg-danger`;
    }
    
    // Cas générique pour les valeurs booléennes
    if (typeof value === 'boolean') {
      return value ? `${baseClass} bg-success` : `${baseClass} bg-danger`;
    }
    
    // Cas par défaut
    return `${baseClass} bg-light text-dark`;
  }

  stopPropagation(event: Event): void {
    event.stopPropagation();
  }

  trackByItemId(index: number, item: T): string | number {
    return item.id;
  }

  trackByColumnKey(index: number, column: ColumnConfig): string {
    return column.key;
  }
}
