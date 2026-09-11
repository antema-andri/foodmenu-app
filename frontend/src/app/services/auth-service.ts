import { inject, Injectable } from '@angular/core';
import { ApiService } from './api-service';
import { UserData } from '../models/user-data.models';
import { environment } from '../../environments/environment';
import { AdminUser } from '../models/admin-user.models';

const HEADER_AUTHORIZATION=environment.headerAuthorization;

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private api=inject(ApiService);
  private userData!:UserData;

  async getToken(username: string, password: string): Promise<UserData|null> {
    try {
      const authUser = { username, password };

      const response = await this.api.post<UserData>(
        `/securities/auth/token`,
        authUser,
        { fullResponse: true }
      );
      
      this.userData = {
        user: (response.body as any).body ?? { id: '', email: '', fullname: '', username: ''},
        token:  (response.body as any)?.headers?.get(HEADER_AUTHORIZATION) ?? ''
      }
      
      return this.userData.token ? this.userData : null;
    } catch (error) {
      console.error('Erreur lors de la récupération du token:', error);
      return null;
    }
  }

  setToken(user:AdminUser,token:string){
    localStorage.setItem('token',token.replace('Bearer ',''));
    localStorage.setItem('user',JSON.stringify(user));
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  getUserDataFromStorage():UserData {
    const userData:UserData = {
      user: JSON.parse(localStorage.getItem('user') as any),
      token: localStorage.getItem('token')
    }
    
    return userData;
  }

  getAdminUser(): AdminUser | null {
    const data = localStorage.getItem('user');
    return data ? JSON.parse(data) as AdminUser : null;
  }

}
