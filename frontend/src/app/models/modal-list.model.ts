// models/modal-list.model.ts
export interface ListItem {
  id: string | number;
  [key: string]: any; // Permet d'avoir n'importe quelle propriété
}

export interface ColumnConfig {
  key: string;
  label: string;
  type?: 'text' | 'number' | 'date' | 'currency' | 'badge' | 'custom';
  format?: string;
  class?: string;
  sortable?: boolean;
}

export interface ModalListConfig {
  titre: string;
  sousTitre?: string;
  colonnes: ColumnConfig[];
  actions?: ActionConfig[];
  showSearch?: boolean;
  showPagination?: boolean;
  pageSize?: number;
  emptyMessage?: string;
  itemClickable?: boolean;
}

export interface ActionConfig {
  label: string;
  icon: string;
  class: string;
  action: string;
}