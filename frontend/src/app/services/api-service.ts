import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../environments/environment';

const API_HOST=environment.apiUrl; 

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    observe: 'response' as const
  };

  constructor(private http: HttpClient) {}

  async get<T>(endpoint: string): Promise<{ body: T | null }>;
  async get(endpoint: string, options: { responseType: 'blob' }): Promise<{ body: Blob | null }>;
  async get(endpoint: string, options: { responseType: 'text' }): Promise<{ body: string | null }>;

  /** GET */
  async get<T>(
    endpoint: string,
    options?: { responseType?: 'json' | 'blob' | 'text' }
  ): Promise<{ body: T  | Blob | null | string }> {
    try {
      const url = `${API_HOST}${endpoint}`;
      const responseType = options?.responseType ?? 'json';

      const response = await firstValueFrom(
        this.http.get<T>(url, { observe: 'response', responseType: responseType as any })
      );

      return { body: response.body ?? null };
    } catch (error: any) {
      if (error.status === 404) return { body: null };
      throw error;
    }
  }

  /** POST */
  async post<T>(
    endpoint: string,
    data: any,
    options?: { fullResponse?: boolean }
  ): Promise<{ body: T | null }> {

    const url = `${API_HOST}${endpoint}`;

    try {
      const response = await firstValueFrom(
        this.http.post<T>(url, data, { observe: 'response' })
      );

      return {
        body: options?.fullResponse ? (response as any) : (response.body ?? null)
      };
    } catch (error) {
      throw error;
    }
  }

  /** PUT */
  async update<T>(endpoint: string, data: any): Promise<{ body: T | null }> {
    const url = `${API_HOST}${endpoint}`;

    try {
      const response = await firstValueFrom(
        this.http.put<T>(url, data, { observe: 'response' })
      );

      return { body: response.body ?? null };
    } catch (error) {
      throw error;
    }
  }

  /** Delete */
  async delete<T>(endpoint: string): Promise<{ body: T | null }> {
    const url = `${API_HOST}${endpoint}`;

    try {
      const response = await firstValueFrom(
        this.http.delete<T>(url, { observe: 'response' })
      );

      return { body: (response as any) ?? null };
    } catch (error) {
      throw error;
    }
  }
}
