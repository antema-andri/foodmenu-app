import { Component, inject, ViewEncapsulation, ɵresetIncrementalHydrationEnabledWarnedForTests } from '@angular/core';
import { AuthService } from '../../../../services/auth-service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserData } from '../../../../models/user-data.models';
import { AdminUser } from '../../../../models/admin-user.models';

@Component({
  selector: 'app-login-component',
  standalone: false,
  templateUrl: './login-component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrl: './login-component.css',
})
export class LoginComponent {
  authService=inject(AuthService);
  form: FormGroup;
  passwordVisible = false;
  showError = false;
  isLoading : boolean = false;

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  togglePassword() {
    this.passwordVisible = !this.passwordVisible;
  }

  async login() {
    const { username, password } = this.form.value;

    this.isLoading = true;
    const userData:UserData|null=await this.authService.getToken(username,password);

    if (userData && userData.token) {
      console.log('Connexion réussie !');
      this.isLoading = false;
      
      this.authService.setToken(userData.user,userData.token);
      this.router.navigate(['admin']);
    } else {
      this.showError = true;

      setTimeout(() => {
        this.showError = false;
      }, 5000);
      this.isLoading = false;
    }
  }
}
