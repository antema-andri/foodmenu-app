import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { ClientTable } from '../../../models/client-table.models';
import { OrderRequest } from '../../../models/order-request.model';
import { NumberOrder } from '../../../models/number-order';
import { Customer } from '../../../models/customer.model';
import { CustomerPage } from '../../../models/customer-page.model';
import { Meal } from '../../../models/meal.model';
import { OrderService } from '../../../services/order-service';
import { CustomerService } from '../../../services/customer-service';
import { MenuService } from '../../../services/menu-service';
import { Menu } from '../../../models/menu.model';

@Component({
  selector: 'app-home-component',
  standalone: false,
  templateUrl: './home-component.html',
  styleUrl: './home-component.css',
})
export class HomeComponent {
  menuService = inject(MenuService);
  customerService = inject(CustomerService);
  orderService = inject(OrderService);

  searchTerm = '';
  currentPage = 0;
  pageSize = 6;
  totalPage = 0;

  clients: ClientTable[] = [];
  loading = false;

  menu!: Menu;
  meals: Meal[] = [];
  customerPage!: CustomerPage;
  customers: Customer[] = [];

  numberOrder!: NumberOrder;

  isSaving: { [clientId: string]: boolean } = {};
  originalDish: { [clientId: string]: string | null } = {};

  constructor(private cd: ChangeDetectorRef) {}

  async ngOnInit() {
    await this.initDatas();
    await this.loadMenu();
    await this.loadCustomerPage();
    await this.setOrderStat();
  }

  async initDatas() {
    this.menu = { id: '', date: '', active: false, title: '', meals: [] };
    this.numberOrder = { totalCustomersOrdered: 0, totalEligibleCustomers: 0 };
  }

  // -------------------- MENU --------------------

  async loadMenu() {
    try {
      const data = await this.menuService.getLatestActiveMenu();

      if (!data) {
        console.error("Menu non trouvé !");
        return;
      }

      this.menu = data;
      this.meals = this.menu.meals;

      this.cd.detectChanges();
    } catch (error) {
      console.error('Erreur lors du chargement du menu:', error);
    }
  }

  // -------------------- CLIENTS --------------------

  async loadCustomerPage(page: number = 0) {
    if (this.menu.id) {
      try {
        this.loading = true;

        this.customerPage = await this.customerService.getCustomers(
          this.searchTerm,
          page,
          this.pageSize
        );

        this.totalPage = this.customerPage.totalPage;
        this.currentPage = page;
        this.customers = this.customerPage.customers;

        await this.loadClientTable();

      } catch (error) {
        console.error('Erreur lors du chargement des clients:', error);
        this.clients = [];
      } finally {
        this.loading = false;
        this.cd.detectChanges();
      }
    }
  }

  async loadClientTable() {
    this.clients = [];
    this.originalDish = {};

    const tasks = this.customers.map(async (cust) => {
      const order = await this.orderService.getCustomerOrderFromMenu(
        this.menu.id,
        cust.id
      );

      const hasOrder = !!order && !!order.meal;
      const mealId = hasOrder ? order!.meal.id : '';

      // Initialiser originalDish avec la valeur actuelle
      this.originalDish[cust.id] = mealId;

      return {
        id: cust.id,
        name: cust.fullname,
        selectedDishId: mealId,
        saved: hasOrder,
        mealId: mealId,
        menuId: this.menu.id
      } as ClientTable;
    });

    this.clients = await Promise.all(tasks);
    this.cd.detectChanges();
  }

  // -------------------- STATISTIQUE COMMANDE --------------------

  async setOrderStat() {
    if (this.menu.id) {
      try {
        const nOrd = await this.orderService.getOrderStat(this.menu.id);
        this.numberOrder.totalEligibleCustomers = nOrd?.totalEligibleCustomers ?? 0;
        this.numberOrder.totalCustomersOrdered = nOrd?.totalCustomersOrdered ?? 0;

      } catch (error) {
        console.error('Erreur lors du chargement de statistique des commandes:', error);
      } finally {
        this.cd.detectChanges();
      }
    }
  }

  // -------------------- PAGINATION --------------------

  async searchClient() {
    await this.loadCustomerPage(0);
  }

  async nextPage() {
    if (this.currentPage < this.totalPage - 1) {
      await this.loadCustomerPage(this.currentPage + 1);
    }
  }

  async previousPage() {
    if (this.currentPage > 0) {
      await this.loadCustomerPage(this.currentPage - 1);
    }
  }

  async goToPage(page: number) {
    if (page >= 0 && page < this.totalPage) {
      await this.loadCustomerPage(page);
    }
  }

  getPageNumbers(): number[] {
    if (this.totalPage === 0) return [];

    const pages: number[] = [];
    const maxVisiblePages = 5;

    let startPage = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPage - 1, startPage + maxVisiblePages - 1);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(0, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }

    return pages;
  }

  getPageButtonClass(page: number): string {
    const baseClass =
      'px-3 py-1 rounded border shadow-sm transition';

    return page === this.currentPage
      ? `${baseClass} bg-blue-600 text-white`
      : `${baseClass} bg-white hover:bg-gray-100 text-gray-700`;
  }

  // -------------------- COMMANDES --------------------

  selectDish(client: ClientTable, dishId: string) {
    client.selectedDishId = dishId;
  }

  async saveOrder(client: ClientTable) {
    if (!client.selectedDishId) return;

    // Vérifier si le plat a changé
    const hasChanged = this.originalDish[client.id] !== client.selectedDishId;

    if (!hasChanged) {
      // Si aucun changement, simplement revenir en mode "Modifier"
      client.saved = true;
      return;
    }

    this.isSaving[client.id] = true;

    try {
      const orderReq: OrderRequest = {
        menuId: client.menuId,
        customerId: client.id,
        mealId: client.selectedDishId
      };

      const existing = await this.orderService.getCustomerOrderFromMenu(
        orderReq.menuId,
        orderReq.customerId
      );
      
      if (existing) {
        await this.orderService.changeOrder(orderReq);
      } else {
        await this.orderService.saveOrder(orderReq);
        await this.setOrderStat();
      }

      // Mettre à jour l'original après sauvegarde réussie
      this.originalDish[client.id] = client.selectedDishId;
      client.mealId = client.selectedDishId;
      client.saved = true;
      
      // Mettre à jour les statistiques
      await this.setOrderStat();

    } catch (error) {
      console.error('Erreur lors de la sauvegarde de la commande:', error);
      // En cas d'erreur, on garde le mode édition
      client.saved = false;
    } finally {
      this.isSaving[client.id] = false;
      this.cd.detectChanges();
    }
  }

  editOrder(client: ClientTable) {
    // Mémoriser la valeur actuelle avant d'activer l'édition
    this.originalDish[client.id] = client.selectedDishId;
    client.saved = false;
  }
}
