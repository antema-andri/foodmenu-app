import { inject, Injectable } from '@angular/core';
import { ApiService } from './api-service';
import { Customer } from '../models/customer.model';
import { CustomerPage } from '../models/customer-page.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private api = inject(ApiService);

  async getCustomer(menuId: string): Promise<Customer | null> {
    try {
      const response = await this.api.get<Customer>(`/customers/${menuId}`);

      return response.body ?? null;
    } catch (error) {
      console.error('Error fetching customer:', error);
      throw error;
    }
  }
  
  async getCustomers(keyword: string, page: number, size: number): Promise<CustomerPage> {
    try {
      const response = await this.api.get<CustomerPage>(
        `/customers?keyword=${keyword}&page=${page}&size=${size}`
      );

      return (
        response.body ?? {
          customers: [],
          currentPage: 0,
          totalPage: 0,
          pageSize: size
        }
      );
    } catch (error) {
      console.error('Error fetching customer:', error);
      throw error;
    }
  }

  async create(customer: Customer): Promise<Customer> {
    try {
      const response = await this.api.post<Customer>('/customers', customer);
      return response.body ?? customer;
    } catch (error) {
      console.error('Error creating customer:', error);
      throw error;
    }
  }
}
