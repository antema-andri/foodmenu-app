import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { DashboardComponent } from './components/pages/admin/dashboard-component/dashboard-component';
import { AdminLayoutComponent } from './components/layouts/admin-layout-component/admin-layout-component';
import { PublicLayoutComponent } from './components/layouts/public-layout-component/public-layout-component';
import { LoginComponent } from './components/pages/admin/login-component/login-component';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './components/pages/home-component/home-component';
import { MenuComponent } from './components/pages/admin/menu-component/menu-component';
import { NgSelectModule } from '@ng-select/ng-select';
import { PaginationComponent } from './components/pages/admin/elts/pagination-component/pagination-component';
import { MenuTableComponent } from './components/pages/admin/elts/menu-table-component/menu-table-component';
import { ModalListComponent } from './components/pages/admin/elts/modal-list-component/modal-list-component';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { CustomerComponent } from './components/pages/admin/customer-component/customer-component';
import { MealComponent } from './components/pages/admin/meal-component/meal-component';
import { CustomerTableComponent } from './components/pages/admin/elts/customer-table-component/customer-table-component';
import { MealTableComponent } from './components/pages/admin/elts/meal-table-component/meal-table-component';
import { MenuListComponent } from './components/pages/admin/menu-list-component/menu-list-component';
import { StatComponent } from './components/pages/admin/stat-component/stat-component';
import { SettingComponent } from './components/pages/admin/setting-component/setting-component';
import { authInterceptor } from './interceptors/auth-interceptor';

@NgModule({
  declarations: [
    App,
    DashboardComponent,
    AdminLayoutComponent,
    PublicLayoutComponent,
    DashboardComponent,
    LoginComponent,
    HomeComponent,
    MenuComponent,
    PaginationComponent,
    MenuTableComponent,
    ModalListComponent,
    CustomerComponent,
    MealComponent,
    CustomerTableComponent,
    MealTableComponent,
    MenuListComponent,
    StatComponent,
    SettingComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    NgSelectModule,
    DateFormatPipe
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withFetch(),withInterceptors([authInterceptor])),
  ],
  bootstrap: [App]
})
export class AppModule { }
