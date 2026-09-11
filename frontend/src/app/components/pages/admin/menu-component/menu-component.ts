import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MealService } from '../../../../services/meal-service';
import { Meal } from '../../../../models/meal.model';
import { MealPage } from '../../../../models/meal-page.model';
import { MenuService } from '../../../../services/menu-service';
import { Menu } from '../../../../models/menu.model';
import { MenuPage } from '../../../../models/menu-page.model';
import { PageChangeEvent, PaginationConfig } from '../../../../models/pagination.model';
import { MealOrderTotal } from '../../../../models/MealOrderTotal.model';
import { DateUtils } from '../../../../utils/date-utils';
import { OrderService } from '../../../../services/order-service';

@Component({
  selector: 'app-menu-component',
  standalone: false,
  templateUrl: './menu-component.html',
  styleUrl: './menu-component.css',
})
export class MenuComponent implements OnInit {
  private menuService = inject(MenuService);
  private mealService = inject(MealService);
  private orderService = inject(OrderService);
  menuForm!: FormGroup;
  meals: Meal[] = [];
  
  mealPage!: MealPage;
  mealKeyWord: string = '';
  page: number = 0;
  size: number = 6;

  commandes: any[] = [];
  totalCommandes = 0;
  totalCustomers = 0;

  activMenuPage!: MenuPage;
  formatedMenu: Menu[]= [];

   paginationConfig: PaginationConfig = {
    page: 1,
    pageSize: 6,
    total: 0,
    pageSizeOptions: [5, 10, 25, 50]
  };

  ev!:{type: string, data: Menu};

  selectedMenu: any = null;
  selectedMenuDishes: {meal:Meal,total:number}[] = [];
  todayDate!:any;

  loadingPdfMenuId: string | null = null;
  loadingModalMenuId: string | null = null;
  isCreatingMenu:boolean = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.menuForm = this.fb.group({
      title: ['', [Validators.required]],
      meals: [[], [Validators.required, this.maxSelectedItemsValidator(3)]],
      deliver_date: ['', [Validators.required]]
    });
    this.activMenuPage={currentPage:0,pageSize:0,totalPage:0,menus:[]};
    this.todayDate = new Date().toISOString().split('T')[0];

    this.loadMeals();
    this.loadActivMenus();
  }

  maxSelectedItemsValidator(max: number) {
    return (control: FormControl) => {
      const value = control.value;
      if (value && value.length > max) {
        return { maxSelectedItems: true };
      }
      return null;
    };
  }

  async submitMenu() {
    if (this.menuForm.valid) {
      this.isCreatingMenu = true;

      const formValue = this.menuForm.value;

      const newMenu:Menu = {
        id: '',
        title: formValue.title,
        date: formValue.deliver_date,
        // meals: formValue.meals.map((mealId: string) => ({ 
        //   menu:null,
        //   meal:{id:mealId}
        // }))
        meals: formValue.meals
      }
      
      // console.log('Menu à créer:', newMenu);
    
      try {
        const result = await this.menuService.createMenu(newMenu);
        // console.log('Menu créé avec succès:', result);
        // reload menu list
        await this.loadActivMenus();
        
        // Optionnel : Reset du formulaire
        this.menuForm.reset();
        this.menuForm.patchValue({
          meals: []
        });

        this.isCreatingMenu = false;
        
      } catch (error) {
        console.error('Erreur création menu:', error);
        this.isCreatingMenu = false;
      }
    } else {
      console.log('Formulaire invalide');
      // Marquer tous les champs comme touched pour afficher les erreurs
      // this.markFormGroupTouched(this.menuForm);
    }
  }

  async loadMeals() {
    try {
      this.mealPage = await this.mealService.getMeals(this.mealKeyWord, this.page, this.size);
      this.meals = this.mealPage.meals;
    } catch (error) {
      console.error('Erreur chargement repas:', error);
    }
  }

  async loadActivMenus() {
    const menuKeyWord='';
    const pageMenu=0;
    const sizeMenuPage=3;
    try {
      this.activMenuPage = await this.menuService.getMenus(menuKeyWord, pageMenu, sizeMenuPage);
      this.formatedMenu = this.activMenuPage.menus.map(menu => ({
        ...menu,
        createdAt: menu.createdAt ? DateUtils.formatCustomDate(menu.createdAt) : ''
      }));
    } catch (error) {
      console.error('Erreur chargement des menus active:', error);
    }
  }

  async onSearch(term: string) {
    // console.log('Recherche effectuée:', term);
    
    if (term && term.length > 1) {
      this.mealKeyWord = term;
      await this.loadMeals();
    } else if (term === '') {
      this.mealKeyWord = '';
      await this.loadMeals();
    }
  }

  async onPageChange(event: PageChangeEvent) {
    this.paginationConfig.page = event.page;
    this.paginationConfig.pageSize = event.pageSize;
    await this.loadActivMenus();
  }

  async getMealOrderTotal(menuId:string,mealId:string): Promise<MealOrderTotal> {
    const mealOrderTot:MealOrderTotal=await this.menuService.getMealOrderTotal(menuId,mealId);
    return mealOrderTot;
  }

  showAction(action:{type: string, data: Menu}) {
     switch (action.type) {
      case 'view':
        this.viewMenuDishes(action.data);
        break;
      case 'edit':
        this.editMenu(action.data);
        break;
      case 'delete':
        this.deleteMenu(action.data);
        break;
    }
  }

  async viewMenuDishes(menu: Menu) {
    this.selectedMenu = menu;
    this.loadingModalMenuId = menu.id;
    
    this.selectedMenuDishes = await Promise.all(
      menu.meals.map(async m => ({
        meal: m,
        total: (await this.getMealOrderTotal(menu.id, m.id)).totalOrderOfMeal
      }))
    );
    
    const numberOrder = await this.orderService.getOrderStat(menu.id);
    this.totalCommandes = numberOrder?.totalCustomersOrdered;
    this.totalCustomers = numberOrder?.totalEligibleCustomers;
    
    // Ouvrir le modal
    const modal = new (window as any).bootstrap.Modal(document.getElementById('dishesModal'));
    modal.show();
    this.loadingModalMenuId = null;
  }

  private async editMenu(menu: any) {
    // console.log('Édition du menu:', menu);
    const isConfirmed=window.confirm("Etes vous sure de valider ce menu avec ses commandes?");

    if(isConfirmed) {
      await this.menuService.validateMenuStatus(menu.id);
      await this.loadActivMenus();
    }
  }

  private async deleteMenu(menu: any) {
    // console.log('Suppression du menu:', menu);
    const isDeleting = window.confirm("Etes vous sure de supprimer ce menu?")

    try {
      if(isDeleting) {
        await this.menuService.deleteMenu(menu.id);
        await this.loadActivMenus();
      }
    } catch (error) {
      console.error('Erreur lors de la suppression du menu:', error);
    }
  }

  async downloadMenuPdf(menuId: string) {
    try {
      const pdfBlob = await this.menuService.getMenuOrderPdf(menuId);
      const url = window.URL.createObjectURL(pdfBlob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `menu-${menuId}-orders.pdf`;
      a.click();

      window.URL.revokeObjectURL(url);

    } catch (error) {
      console.error('Erreur téléchargement PDF:', error);
    }
  }

  async openMenuPdf(menuId: string) {
    const pdfBlob = await this.menuService.getMenuOrderPdf(menuId);
    const url = window.URL.createObjectURL(pdfBlob);
    window.open(url, '_blank');
  }

  async openPdf(menuId: string) {
    const blob = await this.menuService.getMenuOrderPdfFromStr(menuId);
    const url = window.URL.createObjectURL(blob);
    window.open(url);
  }

  // typeExport  = 0 (if open) and 1 (if download)
  async exportMenuPdf(event:{type: string, data: Menu}, typeExport:number=0) {
    const menuPdf:Menu=event.data;

    this.loadingPdfMenuId = menuPdf.id;

    if (typeExport == 1) {
      await this.downloadMenuPdf(menuPdf.id);
    } else {
      // await this.openMenuPdf(menuPdf.id);
      await this.openPdf(menuPdf.id);
    }

    this.loadingPdfMenuId = null;
  }

  async getOrderNumber(menuId:string,mealId:string): Promise<Meal> {
    const meal:Meal={id:'',name:'',description:'',price:0};
    return meal;
  }

}