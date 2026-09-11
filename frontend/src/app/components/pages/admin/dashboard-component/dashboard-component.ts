import { Component, inject } from '@angular/core';
import { AuthService } from '../../../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard-component',
  standalone: false,
  templateUrl: './dashboard-component.html',
  styleUrl: './dashboard-component.css',
})
export class DashboardComponent {
  router=inject(Router);
  authService=inject(AuthService);
  menuOpen = false;

  toggleUserMenu() {
    this.menuOpen = !this.menuOpen;
  }

  logout() {
    this.authService.logout(); // clear storage
    this.router.navigate(['admin/login']);
  }
}
