import { inject, Injectable } from '@angular/core';
import { Order } from '../models/order.model';
import { ApiService } from './api-service';
import { OrderRequest } from '../models/order-request.model';
import { NumberOrder } from '../models/number-order';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private api = inject(ApiService);

  constructor() {}

  /**
   * Récupérer la commande d'un client pour un menu
   * Retourne null si aucune commande n'existe (404)
   */
  async getCustomerOrderFromMenu(menuId: string, customerId: string): Promise<Order | null> {
    try {
      const response = await this.api.get<Order>(`/orders/${menuId}/${customerId}`);
      // Si le backend renvoie 404 -> ApiService renvoie body = null
      if (!response.body) {
        return null;
      }
      return response.body;
    } catch (error) {
      // En cas d'erreur (401, 404, 500, etc.), on considère qu'il n'y a pas de commande
      console.warn(`Commande non trouvée pour ${customerId} :`, error);
      return null;
    }
  }

  /**
   * Créer une commande
   */
  async saveOrder(orderRequest: OrderRequest): Promise<Order> {
    const response = await this.api.update<Order>('/orders', orderRequest);

    if (!response.body) {
      throw new Error("Erreur : la création de commande n'a pas renvoyé de corps.");
    }

    return response.body;
  }

  /**
   * Modifier / remplacer la commande existante
   */
  async changeOrder(orderRequest: OrderRequest): Promise<Order> {
    const response = await this.api.update<Order>('/orders/replace', orderRequest);

    if (!response.body) {
      throw new Error("Erreur : la modification de commande n'a pas renvoyé de corps.");
    }

    return response.body;
  }

  /**
   * Obtenir la statistique de commande pour un menu
   */
  async getOrderStat(menuId: string): Promise<NumberOrder> {
    try {
      const response = await this.api.get<NumberOrder>(`/orders/orderStat/${menuId}`);
      const numberOrder = response.body ? response.body as NumberOrder : null;

      return numberOrder ?? {totalCustomersOrdered:0, totalEligibleCustomers:0};
    } catch (error) {
      console.error('Error fetching numberOrder:', error);
      throw error;
    }
  }
}
