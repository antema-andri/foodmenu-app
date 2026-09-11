import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PublicLayoutComponent } from './components/layouts/public-layout-component/public-layout-component';
import { LoginComponent } from './components/pages/admin/login-component/login-component';
import { AdminLayoutComponent } from './components/layouts/admin-layout-component/admin-layout-component';
import { adminGuard } from './guards/admin-guard';
import { DashboardComponent } from './components/pages/admin/dashboard-component/dashboard-component';
import { HomeComponent } from './components/pages/home-component/home-component';
import { MenuComponent } from './components/pages/admin/menu-component/menu-component';
import { MealComponent } from './components/pages/admin/meal-component/meal-component';
import { CustomerComponent } from './components/pages/admin/customer-component/customer-component';
import { MenuListComponent } from './components/pages/admin/menu-list-component/menu-list-component';
import { StatComponent } from './components/pages/admin/stat-component/stat-component';
import { SettingComponent } from './components/pages/admin/setting-component/setting-component';
import { loginGuard } from './guards/login-guard';

const routes: Routes = [
  {
    path: '',
    component: PublicLayoutComponent,
    children: [
      { path: '', component: HomeComponent }
    ],
  },
  {
    path: 'admin',
    children: [
      { 
        path: 'login', 
        component: LoginComponent,
        canActivate: [loginGuard] 
      },
      {
        path: '',
        component: AdminLayoutComponent,
        canActivate: [adminGuard],        
        children: [
          // { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
          // { path: 'dashboard', component: DashboardComponent },
          { path: '', redirectTo: 'menu', pathMatch: 'full' },
          { path: 'menu', component: MenuComponent },
          { path: 'plat', component: MealComponent },
          { path: 'client', component: CustomerComponent },
          { path: 'menu-list', component: MenuListComponent },
          { path: 'stats', component: StatComponent },
          { path: 'settings', component: SettingComponent },
        ],
      },
    ],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
