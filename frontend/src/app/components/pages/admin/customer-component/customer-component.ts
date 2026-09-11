import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Customer } from '../../../../models/customer.model';
import { CustomerService } from '../../../../services/customer-service';
import { CustomerPage } from '../../../../models/customer-page.model';
import { PageChangeEvent, PaginationConfig } from '../../../../models/pagination.model';
import { PaginationUtils } from '../../../../utils/PaginationUtils';

@Component({
  selector: 'app-customer-component',
  standalone: false,
  templateUrl: './customer-component.html',
  styleUrl: './customer-component.css',
})
export class CustomerComponent implements OnInit {
  private customerService=inject(CustomerService);
  customerForm!: FormGroup;
  customers!: Customer[];

  activeCustomerPage!: CustomerPage;
  searchTerm: string = '';
  page: number = 0;
  size: number = 6;

  paginationConfig: PaginationConfig = {
    page: 1,
    pageSize: 6,
    total: 0
  };

  isCreatingCustomer:boolean = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.customerForm = this.fb.group({
      fullname: ['', [Validators.required, Validators.minLength(3)]],
      phone: ['', [Validators.required, Validators.pattern(/^0\d{9}$/)]],
    });
    this.loadCustomers();
  }

  async submitCustomer() {
    if (this.customerForm.invalid) return;

    const customer = this.customerForm.value as Customer;
    this.isCreatingCustomer = true;

    try {
      const created = await this.customerService.create(customer);

      // Optionnel : console.log
      // console.log("Customer enregistré :", created);
      this.isCreatingCustomer = false;

      // Vider le formulaire
      this.customerForm.reset();

      // Recharger la liste des clients
      this.loadCustomers();

    } catch (error) {
      console.error("Erreur lors de l'enregistrement du client", error);
    }
  }

  async loadCustomers() {
    const apiPage = PaginationUtils.uiToApi(this.paginationConfig.page);

    this.activeCustomerPage = await this.customerService.getCustomers(
      this.searchTerm,
      apiPage,
      this.paginationConfig.pageSize
    );

    this.customers = this.activeCustomerPage.customers;

    this.paginationConfig.total =
      PaginationUtils.totalElements(
        this.activeCustomerPage.totalPage,
        this.paginationConfig.pageSize
      );

    this.paginationConfig.page = PaginationUtils.apiToUi(this.activeCustomerPage.currentPage);
  }

  // async loadCustomers() {
  //   const pageApi = this.paginationConfig.page - 1;
  //   const size = this.paginationConfig.pageSize;

  //   this.activeCustomerPage = await this.customerService.getCustomers(
  //     this.searchTerm,
  //     pageApi,
  //     size
  //   );

  //   this.customers = this.activeCustomerPage.customers;

  //   this.paginationConfig.total = this.activeCustomerPage.totalPage * size;

  //   this.paginationConfig.page = this.activeCustomerPage.currentPage + 1;
  // }

  onPageChange(event: PageChangeEvent) {
    this.paginationConfig.page = event.page;       // déjà 1-based
    this.paginationConfig.pageSize = event.pageSize;
    this.loadCustomers();
  }

  onSearch(term: string) {
    this.searchTerm = term;
    this.paginationConfig.page = 1; // reset page
    this.loadCustomers();
  }

}
