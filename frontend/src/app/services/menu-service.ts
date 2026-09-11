import { inject, Injectable } from '@angular/core';
import { ApiService } from './api-service';
import { Menu } from '../models/menu.model';
import { MenuPage } from '../models/menu-page.model';
import { MealOrderTotal } from '../models/MealOrderTotal.model';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private api = inject(ApiService);

  async getMenu(menuId: string): Promise<Menu> {
    const data = await this.api.get<Menu>(`/menus/${menuId}`);

    if (!data.body) {
      throw new Error(`Menu ${menuId} introuvable`);
    }

    return {
      ...data.body,
      meals: data.body.meals ?? [] 
    };
  }
  
  async getLatestActiveMenu(): Promise<Menu> {
    try {
      const data = await this.api.get<Menu>(`/menus/lastActive`);

      const menu:Menu|null = data.body;

      return menu ? menu : {id:'',meals:[],title:'',active:true}; 

    } catch (error) {
      console.error('Erreur lors de la récupération du menu:', error);
      return {id:'',meals:[],title:'',active:true};
    }
  }

  async createMenu(newMenu: Menu): Promise<Menu> {
    const data = await this.api.post<Menu>(`/menus`,newMenu);

    if (!data.body) {
      throw new Error(`probleme lors de la creation de Menu`);
    }

    return {
      ...data.body,
      meals: data.body.meals ?? []
    };
  }

  async getMenus(keyword: string, page: number, size: number): Promise<MenuPage> {
      try {
        const response = await this.api.get<MenuPage>(
          `/menus?keyword=${keyword}&page=${page}&size=${size}`
        );
  
        return (
          response.body ?? {
            menus: [],
            currentPage: 0,
            totalPage: 0,
            pageSize: size
          }
        );
      } catch (error) {
        console.error('Error fetching menus page:', error);
        throw error;
      }
  }

  async getMealOrderTotal(menuId:string,mealId:string): Promise<MealOrderTotal> {
    const data = await this.api.get<MealOrderTotal>(`/menus/${menuId}/meals/${mealId}/order-number`);

    if (!data.body) {
      throw new Error(`Error fetching mealOrderTotal`);
    }

    return {
      ...data.body
    };
  }

  async getMenuOrderPdf(menuId:string): Promise<Blob> {
    const { body } = await this.api.get(`/menus/${menuId}/menu-order-pdf`,{
      responseType: 'blob'
    });

    if (!body) {
      throw new Error('PDF introuvable');
    }

    return body as Blob;
  }

  async getMenuOrderPdfFromStr(menuId: string): Promise<Blob> {
    const { body } = await this.api.get(
      `/menus/${menuId}/menu-order-str-pdf`,
      { responseType: 'text' }
    );
    
    if (!body) {
      throw new Error('PDF introuvable');
    }

    const cleaned = body.includes(',')
      ? body.split(',')[1]
      : body;

    const byteCharacters = atob(cleaned);
    const byteNumbers = new Array(byteCharacters.length);

    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);

    return new Blob([byteArray], { type: 'application/pdf' });
  }

  async deleteMenu(menuId:string):Promise<Menu> {
    const data = await this.api.delete<Menu>(`/menus/${menuId}`);

    if (!data.body) {
      throw new Error(`Error deleting menu`);
    }

    return {
      ...data.body
    };
  }

  async validateMenuStatus(menuId: string):Promise<Menu> {
    const menuUpateData = {status:false}; // statut validé
    const data = await this.api.update<Menu>(`/menus/${menuId}/status`,menuUpateData);

    if (!data.body) {
      throw new Error(`Error validating menu`);
    }

    return {
      ...data.body
    };
  }
}
