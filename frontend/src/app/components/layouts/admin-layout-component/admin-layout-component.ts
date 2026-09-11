import { Component, inject, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';
import { AdminUser } from '../../../models/admin-user.models';
import { getInitials2 } from '../../../utils/string-utils';

@Component({
  selector: 'app-admin-layout-component',
  standalone: false,
  templateUrl: './admin-layout-component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrl: './admin-layout-component.css'
})
export class AdminLayoutComponent implements OnInit {
  router=inject(Router);
  authService=inject(AuthService);
  menuOpen = false;
  user !: AdminUser|null;
  initialsName = 'AD';

  ngOnInit(): void {
    this.user = this.authService.getAdminUser();
    this.initialsName = getInitials2(this.user?.fullname as string);
  }

  toggleUserMenu() {
    this.menuOpen = !this.menuOpen;
  }

  logout() {
    this.authService.logout(); // clear storage
    this.router.navigate(['admin/login']);
  }
}
